package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetSeedFunction implements HordeFunction<HordePlayerEvent> {

    private final Value<Long> getter;

    public SetSeedFunction(Value<Long> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        ctx.getRandom().setSeed(getter.get(ctx));
    }
    
    public static SetSeedFunction deserialize(JsonElement json) {
        try {
            return new SetSeedFunction(DataRegistry.readValue(DataType.LONG, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_seed", e);
        }
        return null;
    }
    
}
