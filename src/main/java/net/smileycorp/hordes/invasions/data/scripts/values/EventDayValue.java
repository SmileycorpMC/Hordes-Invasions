package net.smileycorp.hordes.invasions.data.scripts.values;


import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.HordesParsingException;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class EventDayValue implements Value<Integer> {
	
	@Override
	public Integer get(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getDay();
	}
	
	public static <T extends Comparable<T>> Value deserialize(JsonObject object, DataType<T> type) {
		if (!type.isNumber()) {
			HordesLogger.logError("invalid value for hordes:day", new HordesParsingException("Expected type" + type + " is not a number"));
			return null;
		}
		return new EventDayValue();
	}
	
}
