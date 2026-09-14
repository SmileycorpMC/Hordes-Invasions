package net.smileycorp.hordes.invasions.data.scripts.functions.spawnentity;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class SetEntityLootTableFunction implements HordeFunction<HordeSpawnEntityEvent> {
    
    private final Value<String> getter;
    
    public SetEntityLootTableFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordeSpawnEntityEvent> ctx) {
        CompoundTag tag = new CompoundTag();
        tag.putString("DeathLootTable", getter.get(ctx));
        ctx.getEntity().readAdditionalSaveData(tag);
    }
    
    public static SetEntityLootTableFunction deserialize(JsonElement json) {
        try {
            return new SetEntityLootTableFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_entity_loot_table", e);
        }
        return null;
    }
    
}
