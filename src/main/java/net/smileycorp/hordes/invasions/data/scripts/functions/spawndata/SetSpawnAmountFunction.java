package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetSpawnAmountFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final Value<Integer> getter;

    public SetSpawnAmountFunction(Value<Integer> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setSpawnAmount(getter.get(ctx));
    }

    public static SetSpawnAmountFunction deserialize(JsonElement json) {
        try {
            return new SetSpawnAmountFunction(DataRegistry.readValue(DataType.INT, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_spawn_amount", e);
        }
        return null;
    }
    
}
