package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class PlayerNBTValue<T extends Comparable<T>> extends NBTValue<T> {

	private PlayerNBTValue(Value<String> value, DataType<T> type) {
		super(value, type);
	}

	@Override
	protected CompoundTag getNBT(HordeContext<? extends HordePlayerEvent> ctx) {
		return NbtPredicate.getEntityTagToCompare(ctx.getPlayer());
	}
	
	public static <T extends Comparable<T>> Value deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new PlayerNBTValue<T>(DataRegistry.readValue(DataType.STRING, object.get("value")), type);
		} catch (Exception e) {
			HordesLogger.logError("invalid value for hordes:player_nbt", e);
		}
		return null;
	}

}
