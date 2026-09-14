package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.BinaryOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.HordesParsingException;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class BinaryOperationValue<T extends Number & Comparable<T>> implements Value<T> {
    
    private final BinaryOperation operation;
    private final Value<T> value1, value2;
    
    private BinaryOperationValue(BinaryOperation operation, Value<T> value1, Value<T> value2) {
        this.operation = operation;
        this.value1 = value1;
        this.value2 = value2;
    }
    
    @Override
    public T get(HordeContext<? extends HordePlayerEvent> ctx) {
        return (T) operation.apply(value1.get(ctx), value2.get(ctx));
    }
    
    public static <T extends Number & Comparable<T>> BinaryOperationValue deserialize(BinaryOperation operation, DataType<T> type, JsonObject element) {
        try {
            Value<T> getter1 = DataRegistry.readValue(type, element.get("value1"));
            Value<T> getter2 = DataRegistry.readValue(type, element.get("value2"));
            if (getter1 == null || getter2 == null |! type.isNumber()) {
                HordesLogger.logError("invalid values for hordes:" + operation.getName(), new HordesParsingException(
                        "value1: " + element.get("value1") + ", value2: " + element.get("value2")));
                return null;
            }
            return new BinaryOperationValue(operation, getter1, getter2);
        } catch (Exception e) {
            HordesLogger.logError("invalid values for hordes:" + operation.getName(), new HordesParsingException("missing values"));
            return null;
        }
    }
    
}
