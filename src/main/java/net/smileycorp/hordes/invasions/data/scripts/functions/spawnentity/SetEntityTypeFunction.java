package net.smileycorp.hordes.invasions.data.scripts.functions.spawnentity;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeSpawnEntityEvent;

public class SetEntityTypeFunction implements HordeFunction<HordeSpawnEntityEvent> {
    
    private final Value<String> getter;
    
    public SetEntityTypeFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordeSpawnEntityEvent> ctx) {
        String str = getter.get(ctx);
        try {
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
            ctx.getEvent().setEntity((Mob) type.create(ctx.getWorld()));
        } catch (Exception e) {
            HordesLogger.logError("Failed changing entity " + ctx.getEntity() + " to type " + str, e);
        }
    }
    
    public static SetEntityTypeFunction deserialize(JsonElement json) {
        try {
            return new SetEntityTypeFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_type", e);
        }
        return null;
    }
    
}
