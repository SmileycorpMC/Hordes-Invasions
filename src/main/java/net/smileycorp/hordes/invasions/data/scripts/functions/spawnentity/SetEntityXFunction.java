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

public class SetEntityXFunction implements HordeFunction<HordeSpawnEntityEvent> {
    
    private final Value<Double> getter;
    
    public SetEntityXFunction(Value<Double> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordeSpawnEntityEvent> ctx) {
        HordeSpawnEntityEvent event = ctx.getEvent();
        Vec3 pos = event.getPos();
        event.setPos(new Vec3(getter.get(ctx), pos.y(), pos.z()));
    }
    
    public static SetEntityXFunction deserialize(JsonElement json) {
        try {
            return new SetEntityXFunction(DataRegistry.readValue(DataType.DOUBLE, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_x", e);
        }
        return null;
    }
    
}
