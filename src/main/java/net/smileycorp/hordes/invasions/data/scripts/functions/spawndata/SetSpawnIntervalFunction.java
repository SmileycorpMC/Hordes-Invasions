package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;

public class SetSpawnIntervalFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final Value<Integer> getter;

    public SetSpawnIntervalFunction(Value<Integer> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setSpawnInterval(getter.get(ctx));
    }

    public static SetSpawnIntervalFunction deserialize(JsonElement json) {
        try {
            return new SetSpawnIntervalFunction(DataRegistry.readValue(DataType.INT, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_spawn_interval", e);
        }
        return null;
    }
    
}
