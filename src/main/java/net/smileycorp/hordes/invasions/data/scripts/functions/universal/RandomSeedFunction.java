package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class RandomSeedFunction implements HordeFunction<HordePlayerEvent> {
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        ctx.getRandom().setSeed(RandomSupport.generateUniqueSeed());
    }
    
    public static RandomSeedFunction deserialize(JsonElement json) {
        return new RandomSeedFunction();
    }
    
}
