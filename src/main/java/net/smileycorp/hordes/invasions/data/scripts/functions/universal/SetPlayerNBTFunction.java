package net.smileycorp.hordes.invasions.data.scripts.functions.universal;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.functions.HordeFunction;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class SetPlayerNBTFunction implements HordeFunction<HordePlayerEvent> {

    private final Value<String> getter;

    public SetPlayerNBTFunction(Value<String> getter) {
        this.getter = getter;
    }
    
    @Override
    public void apply(HordeContext<HordePlayerEvent> ctx) {
        String str = getter.get(ctx);
        try {
            CompoundTag nbt = DataRegistry.parseNBT(ctx.getEntity().toString(), str);
            ctx.getPlayer().readAdditionalSaveData(nbt);
        } catch (Exception e) {
            HordesLogger.logError("Failed loading nbt " + str + " for player " + ctx.getEntity(), e);
        }
    }
    
    public static SetPlayerNBTFunction deserialize(JsonElement json) {
        try {
            return new SetPlayerNBTFunction(DataRegistry.readValue(DataType.STRING, json));
        } catch(Exception e) {
            HordesLogger.logError("Incorrect parameters for function hordes:set_player_nbt", e);
        }
        return null;
    }
    
}
