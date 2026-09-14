package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.ComparableOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class ComparisonCondition<T extends Comparable<T>> implements Condition {

	protected final Value<T> value1;
	protected final ComparableOperation operation;
	protected final Value<T> value2;

	private ComparisonCondition(Value<T> value1, ComparableOperation operation, Value<T> value2) {
		this.value1 = value1;
		this.operation = operation;
		this.value2 = value2;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return operation.apply(value1.get(ctx), value2.get(ctx));
	}

	public static <T extends Comparable<T>> ComparisonCondition<T> deserialize(JsonElement json) {
		try {
			JsonObject obj = json.getAsJsonObject();
			DataType<T> type = (DataType<T>) DataType.of(obj.get("type").getAsString());
			ComparableOperation operation = ComparableOperation.of(obj.get("operation").getAsString());
			Value<T> value1 = DataRegistry.readValue(type,  obj.get("value1"));
			Value<T> value2 = DataRegistry.readValue(type,  obj.get("value2"));
			return new ComparisonCondition<>(value1, operation, value2);
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:comparison", e);
		}
		return null;
	}

}
