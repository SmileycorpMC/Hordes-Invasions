package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.HordeScript;
import net.smileycorp.hordes.invasions.data.scripts.HordeScriptLoader;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;

public class CallScriptFunction implements HordeFunction<HordePlayerEvent> {

    private final Value<String> getter;

    public CallScriptFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        HordeScript script = HordeScriptLoader.INSTANCE.getScript(new ResourceLocation(getter.get(ctx)));
        if (script.getType() != ctx.getClass()) return;
        if (!script.shouldApply(ctx)) return;
        ctx.setCalled(true);
        script.apply(ctx);
        ctx.setCalled(false);
    }
    
    public static CallScriptFunction deserialize(JsonElement json) {
        try {
            return new CallScriptFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:call_script", e);
        }
        return null;
    }
    
}
