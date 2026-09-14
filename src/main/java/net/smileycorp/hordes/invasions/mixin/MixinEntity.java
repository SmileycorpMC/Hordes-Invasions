package net.smileycorp.hordes.invasions.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.smileycorp.hordes.invasions.capability.HordeEvent;
import net.smileycorp.hordes.invasions.capability.HordeSavedData;
import net.smileycorp.hordes.invasions.capability.HordeSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(at =@At("HEAD"), method = "remove")
    protected void hordes$remove(Entity.RemovalReason reason, CallbackInfo callback) {
        ServerPlayer player = HordeSpawn.getHordePlayer((Entity) (Object) this);
        if (player == null) return;
        HordeEvent horde = HordeSavedData.getData((ServerLevel) player.level()).getEvent(player);
        if (horde != null) horde.removeEntity((Mob) (Object) this);
    }

}
