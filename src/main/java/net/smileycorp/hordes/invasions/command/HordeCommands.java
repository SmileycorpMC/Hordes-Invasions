package net.smileycorp.hordes.invasions.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.smileycorp.hordes.invasions.Constants;

@EventBusSubscriber(modid= Constants.MODID)
public class HordeCommands {

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("hordes");
		CommandListEntities.register(command);
		CommandSpawnWave.register(command);
		CommandStartHordeEvent.register(command);
		CommandStopHordeEvent.register(command);
		CommandDebugHordeEvent.register(command);
		CommandResetHordeEvent.register(command);
		dispatcher.register(command);
	}

}
