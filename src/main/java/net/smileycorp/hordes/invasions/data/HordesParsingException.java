package net.smileycorp.hordes.invasions.data;

import net.minecraft.resources.ResourceLocation;
import net.smileycorp.hordes.invasions.data.scripts.HordeScriptLoader;

public class HordesParsingException extends Exception {

    private ResourceLocation script;

    public HordesParsingException(String message) {
        super(message);
        script = HordeScriptLoader.INSTANCE.getCurrentScript();
    }

    public ResourceLocation getScript() {
        return script;
    }

    public void setScript(ResourceLocation script) {
        this.script = script;
    }

}
