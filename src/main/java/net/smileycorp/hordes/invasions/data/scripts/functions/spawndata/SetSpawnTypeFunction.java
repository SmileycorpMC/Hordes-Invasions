package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;
import net.smileycorp.hordes.invasions.data.HordeSpawnType;
import net.smileycorp.hordes.invasions.data.HordeSpawnTypes;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetSpawnTypeFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final HordeSpawnType type;

    public SetSpawnTypeFunction(HordeSpawnType type) {
        this.type = type;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setSpawnType(type);
    }

    public static SetSpawnTypeFunction deserialize(JsonElement json) {
        try {
            HordeSpawnType type = HordeSpawnTypes.fromJson(json);
            if (type == null) throw new NullPointerException();
            return new SetSpawnTypeFunction(type);
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_spawn_type", e);
        }
        return null;
    }
    
}
