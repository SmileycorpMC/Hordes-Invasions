package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class PlayerPosValue<T extends Comparable<T>> extends PosValue<T> {

	private PlayerPosValue(Value<String> value, DataType<T> type) {
		super(value, type);
	}

	@Override
	protected LivingEntity getEntity(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getPlayer();
	}
	
	public static <T extends Number & Comparable<T>> Value deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new PlayerPosValue(DataRegistry.readValue(DataType.STRING, object.get("value")), type);
		} catch (Exception e) {
			HordesLogger.logError("invalid value for hordes:player_pos", e);
		}
		return null;
	}
	
}
