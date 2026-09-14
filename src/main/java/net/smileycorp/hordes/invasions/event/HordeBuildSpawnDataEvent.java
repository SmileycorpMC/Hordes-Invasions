package net.smileycorp.hordes.invasions.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.smileycorp.hordes.invasions.data.HordeSpawnData;
import net.smileycorp.hordes.invasions.capability.HordeEvent;

@Cancelable
public class HordeBuildSpawnDataEvent extends HordePlayerEvent {
	
	private final HordeSpawnData spawnData;

	public HordeBuildSpawnDataEvent(ServerPlayer player, HordeEvent horde) {
		super(player, horde);
		spawnData = new HordeSpawnData(horde);
	}

	@Override
	public HordeSpawnData getSpawnData() {
		return spawnData;
	}
	
}
