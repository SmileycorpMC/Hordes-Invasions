package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class AdvanceRandomFunction implements HordeFunction<HordePlayerEvent> {

    private final Value<Integer> getter;

    public AdvanceRandomFunction(Value<Integer> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        ctx.getRandom().consumeCount(getter.get(ctx));
    }
    
    public static AdvanceRandomFunction deserialize(JsonElement json) {
        try {
            return new AdvanceRandomFunction(DataRegistry.readValue(DataType.INT, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:advance_random", e);
        }
        return null;
    }
    
}
