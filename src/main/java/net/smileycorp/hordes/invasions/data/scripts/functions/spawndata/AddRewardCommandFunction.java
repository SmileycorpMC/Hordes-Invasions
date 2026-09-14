package net.smileycorp.hordes.invasions.data.scripts.functions.spawndata;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordeBuildSpawnDataEvent;

public class AddRewardCommandFunction implements HordeFunction<HordeBuildSpawnDataEvent> {
    
    private final Value<String> getter;
    
    public AddRewardCommandFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordeBuildSpawnDataEvent> ctx) {
        ctx.getSpawnData().addCommand(getter.get(ctx));
    }
    
    public static AddRewardCommandFunction deserialize(JsonElement json) {
        try {
            return new AddRewardCommandFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:add_reward_command", e);
        }
        return null;
    }
    
}
