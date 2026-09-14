package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class GlobalValue<T extends Comparable<T>> implements Value<T> {

	protected final Value<String> variable;
	private final DataType<T> type;

	public GlobalValue(Value<String> variable, DataType<T> type) {
		this.variable = variable;
        this.type = type;
    }

	@Override
	public T get(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getGlobal(variable.get(ctx), type);
	}

	public static <T extends Comparable<T>> GlobalValue<T> deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new GlobalValue<>(DataRegistry.readValue(DataType.STRING, object.get("value")), type);
		} catch (Exception e) {
			HordesLogger.logError("invalid value for hordes:get_global", e);
		}
		return null;
	}

}
