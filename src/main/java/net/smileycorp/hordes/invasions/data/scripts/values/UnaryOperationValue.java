package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.data.UnaryOperation;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.HordesParsingException;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class UnaryOperationValue<T extends Number & Comparable<T>> implements Value<T> {
    
    private final UnaryOperation operation;
    private final Value<T> value;
    
    private UnaryOperationValue(UnaryOperation operation, Value<T> value) {
        this.operation = operation;
        this.value = value;
    }
    
    @Override
    public T get(HordeContext<? extends HordePlayerEvent> ctx) {
        return (T) operation.apply(value.get(ctx));
    }
    
    public static <T extends Number & Comparable<T>> UnaryOperationValue deserialize(UnaryOperation operation, DataType<T> type, JsonObject element) {
        try {
            Value<T> getter = DataRegistry.readValue(type, element.get("value"));
            if (getter == null |! type.isNumber()) {
                HordesLogger.logError("invalid value for hordes:" + operation.getName(), new HordesParsingException(element.get("value").toString()));
                return null;
            }
            return new UnaryOperationValue(operation, getter);
        } catch (Exception e) {
            HordesLogger.logError("invalid values for hordes:" + operation.getName(), new HordesParsingException("missing value"));
            return null;
        }

    }
    
}
