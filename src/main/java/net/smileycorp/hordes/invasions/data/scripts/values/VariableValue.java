package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class VariableValue<T extends Comparable<T>> implements Value<T> {

	protected final Value<String> variable;

	public VariableValue(Value<String> variable) {
		this.variable = variable;
	}

	@Override
	public T get(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getValue(variable.get(ctx));
	}

	public static <T extends Comparable<T>> VariableValue<T> deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new VariableValue<>(DataRegistry.readValue(DataType.STRING, object.get("value")));
		} catch (Exception e) {
			HordesLogger.logError("invalid value for hordes:get_variable", e);
		}
		return null;
	}

}
