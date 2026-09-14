package net.smileycorp.hordes.invasions.data.scripts.functions.spawnentity;

import com.google.gson.JsonElement;
import net.minecraft.world.phys.Vec3;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeSpawnEntityEvent;

public class SetEntityYFunction implements HordeFunction<HordeSpawnEntityEvent> {
    
    private final Value<Double> getter;
    
    public SetEntityYFunction(Value<Double> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeSpawnEntityEvent> ctx) {
        HordeSpawnEntityEvent event = ctx.getEvent();
        Vec3 pos = event.getPos();
        event.setPos(new Vec3(pos.x(), getter.get(ctx), pos.z()));
    }
    
    public static SetEntityYFunction deserialize(JsonElement json) {
        try {
            return new SetEntityYFunction(DataRegistry.readValue(DataType.DOUBLE, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_y", e);
        }
        return null;
    }
    
}
