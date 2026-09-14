package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class RandomCondition implements Condition {

	protected Value<Double> chance;

	public RandomCondition(Value<Double> chance) {
		this.chance = chance;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getRandom().nextFloat() <= chance.get(ctx);
	}

	public static RandomCondition deserialize(JsonElement json) {
		try {
			return new RandomCondition(DataRegistry.readValue(DataType.DOUBLE, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:random", e);
		}
		return null;
	}

}
