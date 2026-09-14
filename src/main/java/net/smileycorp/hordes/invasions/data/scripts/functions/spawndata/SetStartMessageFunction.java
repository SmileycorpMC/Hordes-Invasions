package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetStartMessageFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final Value<String> getter;

    public SetStartMessageFunction(Value<String> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setStartMessage(getter.get(ctx));
    }

    public static SetStartMessageFunction deserialize(JsonElement json) {
        try {
            return new SetStartMessageFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_start_message", e);
        }
        return null;
    }
    
}
