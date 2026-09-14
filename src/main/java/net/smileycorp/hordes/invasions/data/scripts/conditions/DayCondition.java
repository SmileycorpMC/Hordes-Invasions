package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class DayCondition implements Condition {

	protected Value<Integer> day;

	public DayCondition(Value<Integer> day) {
		this.day = day;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getDay() > day.get(ctx);
	}

	public static DayCondition deserialize(JsonElement json) {
		try {
			return new DayCondition(DataRegistry.readValue(DataType.INT, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:day", e);
		}
		return null;
	}

}
