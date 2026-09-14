package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.data.tables.HordeTableLoader;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;

public class SetSpawntableFunction implements HordeFunction<HordeBuildSpawnDataEvent> {

    private final Value<String> getter;

    public SetSpawntableFunction(Value<String> getter) {
        this.getter = getter;
    }

    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().setTable(HordeTableLoader.INSTANCE.getTable(new ResourceLocation(getter.get(ctx))));
    }

    public static SetSpawntableFunction deserialize(JsonElement json) {
        try {
            return new SetSpawntableFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_spawntable", e);
        }
        return null;
    }
    
}
