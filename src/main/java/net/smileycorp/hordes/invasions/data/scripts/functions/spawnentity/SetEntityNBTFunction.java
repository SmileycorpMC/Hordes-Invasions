package net.smileycorp.hordes.invasions.data.scripts.functions.spawnentity;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetEntityNBTFunction implements HordeFunction<HordeSpawnEntityEvent> {
    
    private final Value<String> getter;
    
    public SetEntityNBTFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordeSpawnEntityEvent> ctx) {
        String str = getter.get(ctx);
        try {
            CompoundTag nbt = DataRegistry.parseNBT(ctx.getEntity().toString(), str);
            ctx.getEntity().readAdditionalSaveData(nbt);
        } catch (Exception e) {
            HordesLogger.logError("Failed loading nbt " + str + " for entity " + ctx.getEntity(), e);
        }
    }
    
    public static SetEntityNBTFunction deserialize(JsonElement json) {
        try {
            return new SetEntityNBTFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_nbt", e);
        }
        return null;
    }
    
}
