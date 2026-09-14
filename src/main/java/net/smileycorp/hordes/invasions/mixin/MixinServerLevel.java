package net.smileycorp.hordes.invasions.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.smileycorp.hordes.invasions.config.CommonConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void hordes$tick(BooleanSupplier bool, CallbackInfo callback) {
        if (CommonConfigHandler.pauseEventServer.get() && ServerLifecycleHooks.getCurrentServer().getPlayerCount() <= 0) callback.cancel();
    }

}
