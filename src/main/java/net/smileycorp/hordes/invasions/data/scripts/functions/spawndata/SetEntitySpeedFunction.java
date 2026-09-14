package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;

public class SetEntitySpeedFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final Value<Double> getter;

    public SetEntitySpeedFunction(Value<Double> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setEntitySpeed(getter.get(ctx));
    }

    public static SetEntitySpeedFunction deserialize(JsonElement json) {
        try {
            return new SetEntitySpeedFunction(DataRegistry.readValue(DataType.DOUBLE, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_speed", e);
        }
        return null;
    }
    
}
