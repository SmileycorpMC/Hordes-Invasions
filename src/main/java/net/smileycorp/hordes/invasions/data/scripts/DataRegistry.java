package net.smileycorp.hordes.invasions.data.scripts;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.smileycorp.atlas.api.data.BinaryOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.data.LogicalOperation;
import net.smileycorp.atlas.api.data.UnaryOperation;
import net.smileycorp.hordes.invasions.Constants;
import net.smileycorp.hordes.invasions.data.HordesParsingException;
import net.smileycorp.hordes.invasions.data.scripts.conditions.*;
import net.smileycorp.hordes.invasions.data.scripts.conditions.astages.PlayerStageCondition;
import net.smileycorp.hordes.invasions.data.scripts.conditions.gamestages.GameStageCondition;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.functions.NestedHordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.functions.spawndata.*;
import net.smileycorp.hordes.invasions.data.scripts.functions.spawnentity.*;
import net.smileycorp.hordes.invasions.data.scripts.functions.universal.*;
import net.smileycorp.hordes.invasions.data.scripts.values.*;
import net.smileycorp.hordes.invasions.config.CommonConfigHandler;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.event.HordeSpawnEntityEvent;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DataRegistry {

	private static final Map<ResourceLocation, BiFunction<JsonObject, DataType, Value>> VALUES = Maps.newHashMap();
	private static final Map<ResourceLocation, Function<JsonElement, Condition>> CONDITIONS = Maps.newHashMap();
	private static final Map<ResourceLocation, Pair<Class<? extends HordePlayerEvent>, HordeFunction.Deserializer<? extends HordePlayerEvent>>> FUNCTIONS = Maps.newHashMap();

	public static void init() {
		registerValueGetters();
		registerConditionDeserializers();
		if (CommonConfigHandler.enableHordeEvent.get()) registerFunctionSerializers();
	}

	private static void registerValueGetters() {
		UnaryOperation.values().forEach(operation -> registerValueGetter(Constants.loc(operation.getName()),
				(obj, type) -> UnaryOperationValue.deserialize(operation, type, obj)));
		BinaryOperation.values().forEach(operation -> registerValueGetter(Constants.loc(operation.getName()),
				(obj, type) -> BinaryOperationValue.deserialize(operation, type, obj)));
		registerValueGetter(Constants.loc("weighted_random"), WeightedRandomValue::deserialize);
		registerValueGetter(Constants.loc("level_nbt"), LevelNBTValue::deserialize);
		registerValueGetter(Constants.loc("player_nbt"), PlayerNBTValue::deserialize);
		registerValueGetter(Constants.loc("player_pos"), PlayerPosValue::deserialize);
		registerValueGetter(Constants.loc("entity_nbt"), EntityNBTValue::deserialize);
		registerValueGetter(Constants.loc("entity_pos"), EntityPosValue::deserialize);
		registerValueGetter(Constants.loc("day"), EventDayValue::deserialize);
		registerValueGetter(Constants.loc("spawn_table"), SpawnTableValue::deserialize);
		registerValueGetter(Constants.loc("get_variable"), VariableValue::deserialize);
		registerValueGetter(Constants.loc("get_global"), GlobalValue::deserialize);
	}

	public static void registerConditionDeserializers() {
		for (LogicalOperation operation : LogicalOperation.values())
			registerConditionDeserializer(Constants.loc(operation.getName()), e -> LogicalCondition.deserialize(operation, e));
		registerConditionDeserializer(Constants.loc("not"), NotCondition::deserialize);
		registerConditionDeserializer(Constants.loc("comparison"), ComparisonCondition::deserialize);
		registerConditionDeserializer(Constants.loc("biome"), BiomeCondition::deserialize);
		registerConditionDeserializer(Constants.loc("day"), DayCondition::deserialize);
		registerConditionDeserializer(Constants.loc("player_day"), DayCondition::deserialize);
		registerConditionDeserializer(Constants.loc("local_difficulty"), LocalDifficultyCondition::deserialize);
		registerConditionDeserializer(Constants.loc("game_difficulty"), GameDifficultyCondition::deserialize);
		registerConditionDeserializer(Constants.loc("random"), RandomCondition::deserialize);
		registerConditionDeserializer(Constants.loc("advancement"), AdvancementCondition::deserialize);
		registerConditionDeserializer(Constants.loc("entity_type"), EntityTypeCondition::deserialize);
		registerConditionDeserializer(Constants.loc("is_called"), IsCalledCondition::deserialize);
		ModList mods = ModList.get();
		if (mods.isLoaded("gamestages")) registerConditionDeserializer(
				new ResourceLocation("gamestages:gamestage"), GameStageCondition::deserialize);
		if (mods.isLoaded("astages")) registerConditionDeserializer(
				new ResourceLocation("astages:player_stage"), PlayerStageCondition::deserialize);
	}

	public static Value readValue(DataType type, JsonObject json) {
		if (json.has("name") && json.has("value")) {
			try {
				ResourceLocation loc = new ResourceLocation(json.get("name").getAsString());
				BiFunction<JsonObject, DataType, Value> getter = VALUES.get(loc);
				if (getter == null) throw new NullPointerException("value getter " + loc + " is not registered");
				return getter.apply(json, type);
			} catch (Exception e) {
				HordesLogger.logError("Failed to read value " + json, e);
			}
		}
		return null;
	}

	public static Condition readCondition(JsonObject json) {
		if (json.has("name") && json.has("value")) {
			try {
				ResourceLocation loc = new ResourceLocation(json.get("name").getAsString());
				Function<JsonElement, Condition> deserializer = CONDITIONS.get(loc);
				if (deserializer == null) throw new NullPointerException("condition " + loc + " is not registered");
				return deserializer.apply(json.get("value"));
			} catch (Exception e) {
				HordesLogger.logError("Failed to read condition " + json, e);
			}
		}
		return null;
	}

	public static void registerValueGetter(ResourceLocation name, BiFunction<JsonObject, DataType, Value> getter) {
		VALUES.put(name, getter);
	}

	public static void registerConditionDeserializer(ResourceLocation name, Function<JsonElement, Condition> serializer) {
		CONDITIONS.put(name, serializer);
	}

    public static CompoundTag parseNBT(String name, String nbtstring) {
        CompoundTag nbt = null;
        try {
            CompoundTag parsed = TagParser.parseTag(nbtstring);
            if (parsed != null) nbt = parsed;
            else throw new NullPointerException("Parsed NBT is null.");
        } catch (Exception e) {
            HordesLogger.logError("Failed to read config, " + e.getCause() + " " + e.getMessage(), e);
            HordesLogger.logError("Error parsing nbt for entity " + name + " " + e.getMessage(), e);
        }
        return nbt;
    }

	public static void registerFunctionSerializers() {
		//universal functions
		registerNestedFunction(Constants.loc("multiple"), MultipleFunction::deserialize);
		registerNestedFunction(Constants.loc("random"), RandomFunction::deserialize);
		registerNestedFunction(Constants.loc("weighted_random"), WeightedRandomFunction::deserialize);
		registerFunction(Constants.loc("set_player_nbt"), HordePlayerEvent.class, SetPlayerNBTFunction::deserialize);
		registerFunction(Constants.loc("set_variable"), HordePlayerEvent.class, SetVariableFunction::deserialize);
		registerFunction(Constants.loc("set_global"), HordePlayerEvent.class, SetGlobalFunction::deserialize);
		registerFunction(Constants.loc("call_script"), HordePlayerEvent.class, CallScriptFunction::deserialize);
		registerFunction(Constants.loc("set_seed"), HordePlayerEvent.class, SetSeedFunction::deserialize);
		registerFunction(Constants.loc("random_seed"), HordePlayerEvent.class, RandomSeedFunction::deserialize);
		registerFunction(Constants.loc("advance_random"), HordePlayerEvent.class, AdvanceRandomFunction::deserialize);
		registerInstructionFunction(Constants.loc("break"), HordeContext::breakScript);
		registerInstructionFunction(Constants.loc("return"), HordeContext::returnScript);
		registerInstructionFunction(Constants.loc("cancel"), HordeContext::cancelEvent);

		//build spawndata functions
		registerFunction(Constants.loc("set_spawntable"), HordeBuildSpawnDataEvent.class, SetSpawntableFunction::deserialize);
		registerFunction(Constants.loc("set_spawn_type"), HordeBuildSpawnDataEvent.class, SetSpawnTypeFunction::deserialize);
		registerFunction(Constants.loc("set_spawn_sound"), HordeBuildSpawnDataEvent.class, SetSpawnSoundFunction::deserialize);
		registerFunction(Constants.loc("set_start_message"), HordeBuildSpawnDataEvent.class, SetStartMessageFunction::deserialize);
		registerFunction(Constants.loc("set_end_message"), HordeBuildSpawnDataEvent.class, SetEndMessageFunction::deserialize);
		registerFunction(Constants.loc("set_spawn_duration"), HordeBuildSpawnDataEvent.class, SetSpawnDurationFunction::deserialize);
		registerFunction(Constants.loc("set_spawn_interval"), HordeBuildSpawnDataEvent.class, SetSpawnIntervalFunction::deserialize);
		registerFunction(Constants.loc("set_spawn_amount"), HordeBuildSpawnDataEvent.class, SetSpawnAmountFunction::deserialize);
		registerFunction(Constants.loc("set_entity_speed"), HordeBuildSpawnDataEvent.class, SetEntitySpeedFunction::deserialize);
		registerFunction(Constants.loc("add_reward_command"), HordeBuildSpawnDataEvent.class, AddRewardCommandFunction::deserialize);

		//spawn entity functions
		registerFunction(Constants.loc("set_entity_type"), HordeSpawnEntityEvent.class, SetEntityTypeFunction::deserialize);
		registerFunction(Constants.loc("set_entity_nbt"), HordeSpawnEntityEvent.class, SetEntityNBTFunction::deserialize);
		registerFunction(Constants.loc("set_entity_x"), HordeSpawnEntityEvent.class, SetEntityXFunction::deserialize);
		registerFunction(Constants.loc("set_entity_y"), HordeSpawnEntityEvent.class, SetEntityYFunction::deserialize);
		registerFunction(Constants.loc("set_entity_z"), HordeSpawnEntityEvent.class, SetEntityZFunction::deserialize);
		registerFunction(Constants.loc("set_entity_loot_table"), HordeSpawnEntityEvent.class, SetEntityLootTableFunction::deserialize);
	}

	public static <T extends HordePlayerEvent> Pair<Class<T>, HordeFunction<T>> readFunction(JsonObject json) throws Exception {
		if (!(json.has("function"))) return Pair.of(null, null);
		ResourceLocation loc = new ResourceLocation(json.get("function").getAsString());
		Pair<Class<? extends HordePlayerEvent>,HordeFunction.Deserializer<? extends HordePlayerEvent>> pair
				= FUNCTIONS.get(loc);
		if (pair == null) throw new HordesParsingException("function " + loc + " is not registered");
		HordeFunction<T> function = (HordeFunction<T>) pair.getSecond().apply(json.has("value") ?
				json.get("value") : new JsonNull());
		return Pair.of(function instanceof NestedHordeFunction ? ((NestedHordeFunction<T>) function).getEventClass()
				: (Class<T>) pair.getFirst(), function);
	}

	public static void registerNestedFunction(ResourceLocation name, NestedHordeFunction.Deserializer<?> serializer) {
		FUNCTIONS.put(name, Pair.of(null, serializer));
	}

	public static <T extends HordePlayerEvent> void registerInstructionFunction(ResourceLocation name, HordeFunction<HordePlayerEvent> function) {
		FUNCTIONS.put(name, Pair.of(HordePlayerEvent.class, json -> function));
	}

	public static <T extends HordePlayerEvent> void registerFunction(ResourceLocation name, Class<T> clazz, HordeFunction.Deserializer<T> serializer) {
		if (clazz == null) return;
		FUNCTIONS.put(name, Pair.of(clazz, serializer));
	}

	public static <T extends Comparable<T>> Value<T> readValue(DataType<T> type, JsonElement value) throws Exception {
		if (value instanceof JsonNull) throw new HordesParsingException("No value present");
		if (value.isJsonObject()) {
			return readValue(type, value.getAsJsonObject());
		} else if (value.isJsonArray()) {
			return new RandomValue(type, value.getAsJsonArray());
		}
		T v = type.readFromJson(value);
		return ctx -> v;
	}
}
