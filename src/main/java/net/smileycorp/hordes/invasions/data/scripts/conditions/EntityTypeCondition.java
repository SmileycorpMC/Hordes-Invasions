package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class EntityTypeCondition implements Condition {

	protected Value<String> getter;

	public EntityTypeCondition(Value<String> getter) {
		this.getter = getter;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		ResourceLocation type = new ResourceLocation(getter.get(ctx));
		return ForgeRegistries.ENTITY_TYPES.getKey(ctx.getEntity().getType()).equals(type);
	}

	public static EntityTypeCondition deserialize(JsonElement json) {
		try {
			return new EntityTypeCondition(DataRegistry.readValue(DataType.STRING, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:entity_type", e);
		}
		return null;
	}

}
