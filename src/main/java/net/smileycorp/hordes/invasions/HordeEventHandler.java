package net.smileycorp.hordes.invasions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.smileycorp.hordes.invasions.capability.Playtime;
import net.smileycorp.hordes.invasions.config.CommonConfigHandler;
import net.smileycorp.hordes.invasions.capability.HordeEvent;
import net.smileycorp.hordes.invasions.capability.HordeSavedData;
import net.smileycorp.hordes.invasions.capability.HordeSpawn;
import net.smileycorp.hordes.invasions.data.scripts.HordeScriptLoader;
import net.smileycorp.hordes.invasions.data.tables.HordeTableLoader;
import net.smileycorp.hordes.invasions.data.HordesLogger;

import java.util.List;
import java.util.Optional;

public class HordeEventHandler {

	//attach required entity capabilities for event to function
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Mob) event.addCapability(Constants.loc("HordeSpawn"), new HordeSpawn.Provider());
	}

	//register data listeners
	@SubscribeEvent
	public void addResourceReload(AddReloadListenerEvent event ) {
		event.addListener(HordeTableLoader.INSTANCE);
		event.addListener(HordeScriptLoader.INSTANCE);
	}

	//send error messages if the logger has errors
	@SubscribeEvent
	public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() == null) return;
		if (event.getEntity().level().isClientSide()) return;
		if (HordesLogger.hasErrors()) {
			List<ResourceLocation> scripts = HordesLogger.getErroredScripts();
			MutableComponent message = scripts.isEmpty() ? Component.translatable("message.hordes.DataError", HordesLogger.getFiletext()) :
					Component.translatable("message.hordes.DataErrorScripts", scripts, HordesLogger.getFiletext());
			event.getEntity().sendSystemMessage(message);
		}
	}

	//clear errors on world leave
	@SubscribeEvent
	public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.getEntity() == null) return;
		if (event.getEntity().level().isClientSide()) return;
		if (ServerLifecycleHooks.getCurrentServer().isDedicatedServer()) return;
		HordesLogger.clearLog(false);
		HordesLogger.clearErrors();
	}

	//spawn the horde at the correct time
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		if (event.phase != Phase.END || !(event.player instanceof ServerPlayer player) || event.player instanceof FakePlayer) return;
		Playtime pt = (Playtime) player;
		pt.setPlaytime(pt.getPlaytime() + 1);
        ServerLevel level = player.serverLevel();
		if (level.dimension() != Level.OVERWORLD) return;
		HordeEvent horde = HordeSavedData.getData(level).getEvent(player);
		if (horde == null) return;
		int time = Math.round(level.getDayTime() % CommonConfigHandler.dayLength.get());
		int day = horde.getCurrentDay(player);
		if (!horde.hasSynced(day)) horde.sync(player, day);
		if (horde.isActive()) {
			horde.update(player);
			return;
		}
		if (time >= CommonConfigHandler.hordeStartTime.get() && time <= CommonConfigHandler.hordeStartTime.get() + CommonConfigHandler.hordeStartBuffer.get()
				&& day >= horde.getNextDay() && (day > 0 || CommonConfigHandler.spawnFirstDay.get()))
			horde.tryStartEvent(player, -1, false);
	}
	
	@SubscribeEvent
	public void logIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer)) return;
		ServerPlayer player = (ServerPlayer) event.getEntity();
		HordeEvent horde = HordeSavedData.getData(player.serverLevel()).getEvent(player);
		if (horde != null) horde.setPlayer(player);
	}
	
	//prevent despawning of entities in an active horde
	@SubscribeEvent
	public void tryDespawn(MobSpawnEvent.AllowDespawn event) {
		ServerPlayer player = HordeSpawn.getHordePlayer(event.getEntity());
		if (player == null) return;
		HordeEvent horde = HordeSavedData.getData((ServerLevel) player.level()).getEvent(player);
		if (horde != null && horde.isActive()) event.setResult(Result.DENY);
	}

	//sync entity capabilities when added to level
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void update(LivingTickEvent event) {
		ServerPlayer player = HordeSpawn.getHordePlayer(event.getEntity());
		if (player == null) return;
		HordeSpawn cap = event.getEntity().getCapability(HordeSpawn.CAPABILITY).orElseGet(null);
		if (cap.isSynced()) return;
		Mob entity = (Mob) event.getEntity();
		entity.targetSelector.getRunningGoals().forEach(WrappedGoal::stop);
		if (entity instanceof PathfinderMob) entity.targetSelector.addGoal(1, new HurtByTargetGoal((PathfinderMob) entity));
		entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Player.class, true));
		HordeEvent horde = HordeSavedData.getData((ServerLevel) player.level()).getEvent(player);
		if (horde != null) if (horde.isActive()) horde.registerEntity(entity, player);
		cap.setSynced();
	}

	//prevent sleeping on horde nights
	@SubscribeEvent
	public void trySleep(PlayerSleepInBedEvent event) {
		if (CommonConfigHandler.canSleepDuringHorde.get() || !(event.getEntity() instanceof ServerPlayer)) return;
		ServerPlayer player = (ServerPlayer) event.getEntity();
		ServerLevel level = player.serverLevel();
		if (level.isDay() |! level.dimensionType().bedWorks()) return;
		HordeSavedData data = HordeSavedData.getData((ServerLevel) player.level());
		if (data.isHordeNight(player)) {
			event.setResult(BedSleepingProblem.OTHER_PROBLEM);
			player.displayClientMessage(Component.translatable(Constants.hordeTrySleep), true);
			return;
		}
		if (!CommonConfigHandler.hordePreventsOtherPlayersSleeping.get()) return;
		Optional<ServerPlayer> optional = data.getPlayersWithHorde().findAny();
		if (optional.isEmpty()) return;
		event.setResult(BedSleepingProblem.OTHER_PROBLEM);
		player.displayClientMessage(Component.translatable(Constants.otherPlayerTrySleep, optional.get().getName()), true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void finishSleeping(SleepFinishedTimeEvent event) {
		long timePassed = event.getNewTime() - event.getLevel().dayTime();
		for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
			Playtime pt = (Playtime) player;
			pt.setPlaytime(pt.getPlaytime() + timePassed);
		}
	}

}
