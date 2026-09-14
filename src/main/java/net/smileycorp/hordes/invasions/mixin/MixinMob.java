package net.smileycorp.hordes.invasions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.smileycorp.atlas.api.util.DataUtils;
import net.smileycorp.hordes.invasions.capability.HordeEvent;
import net.smileycorp.hordes.invasions.capability.HordeSavedData;
import net.smileycorp.hordes.invasions.capability.HordeSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

	public MixinMob(Level level) {
		super(null, level);
	}

	//copy horde data to converted entities after conversion before capabilities are cleared
	@WrapOperation(method = "convertTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"))
	private Entity hordes$convertTo$create(EntityType instance, Level level, Operation<Entity> original) {
		Entity entity = original.call(instance, level);
		if (!(entity instanceof Mob)) return entity;
		Mob converted = (Mob) entity;
		LazyOptional<HordeSpawn> beforeOptional = getCapability(HordeSpawn.CAPABILITY);
		LazyOptional<HordeSpawn> afterOptional = converted.getCapability(HordeSpawn.CAPABILITY);
		if (!(beforeOptional.isPresent() || afterOptional.isPresent())) return converted;
		if (!beforeOptional.orElseGet(null).isHordeSpawned()) return converted;
		String uuid = beforeOptional.orElseGet(null).getPlayerUUID();
		if (!DataUtils.isValidUUID(uuid)) return converted;
		afterOptional.orElseGet(null).setPlayerUUID(uuid);
		beforeOptional.orElseGet(null).setPlayerUUID("");
		HordeEvent horde = HordeSavedData.getData((ServerLevel) level()).getEvent(UUID.fromString(uuid));
		if (horde != null) {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(UUID.fromString(uuid));
			horde.registerEntity(converted, player);
			horde.removeEntity((Mob) (LivingEntity) this);
			converted.targetSelector.getRunningGoals().forEach(WrappedGoal::stop);
			if (converted instanceof PathfinderMob) converted.targetSelector.addGoal(1, new HurtByTargetGoal((PathfinderMob) converted));
			converted.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(converted, Player.class, true));
		}
		return converted;
	}

	//add horde ai to converted mobs
	@Inject(at=@At("TAIL"), method = "convertTo")
	public void hordes$convertTo$TAIL(EntityType<?> type, boolean keepEquipment, CallbackInfoReturnable<Mob> callback) {
		Mob converted = callback.getReturnValue();
		LazyOptional<HordeSpawn> optional = converted.getCapability(HordeSpawn.CAPABILITY);
		if (!optional.isPresent()) return;
		if (!optional.orElseGet(null).isHordeSpawned()) return;
		String uuid = optional.orElseGet(null).getPlayerUUID();
		if (DataUtils.isValidUUID(uuid)) {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(UUID.fromString(uuid));
			if (player == null) return;
			HordeEvent horde = HordeSavedData.getData((ServerLevel)level()).getEvent(player);
			if (horde == null) return;
			if (!horde.isActive()) return;
			horde.registerEntity((Mob)(LivingEntity)this, player);
		}
	}

}
