package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class SetVariableFunction<T extends Comparable<T>> implements HordeFunction<HordePlayerEvent> {

    private final Value<String> variable;
    private final Value<T> value;

    public SetVariableFunction(Value<String> variable, Value<T> value) {
        this.variable =  variable;
        this.value = value;
    }
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        ctx.setValue(variable.get(ctx), value.get(ctx));
    }

    public static SetVariableFunction deserialize(JsonElement json) {
        try {
            JsonObject obj = (JsonObject) json;
            DataType type = DataType.of(obj.get("type").getAsString());
            return new SetVariableFunction(DataRegistry.readValue(DataType.STRING, obj.get("key")), DataRegistry.readValue(type, obj.get("value")));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_variable", e);
        }
        return null;
    }

}
