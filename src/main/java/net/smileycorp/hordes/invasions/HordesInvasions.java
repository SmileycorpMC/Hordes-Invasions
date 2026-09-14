package net.smileycorp.hordes.invasions;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.resource.PathPackResources;
import net.smileycorp.hordes.invasions.client.HordeClientHandler;
import net.smileycorp.hordes.invasions.config.ClientConfigHandler;
import net.smileycorp.hordes.invasions.config.CommonConfigHandler;
import net.smileycorp.hordes.invasions.data.DataGenerator;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.network.HordeEventPacketHandler;

import java.nio.file.Path;

@Mod(value = Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HordesInvasions {

	public HordesInvasions() {
		HordesLogger.clearLog(true);
		HordesLogger.heading("LOADING CONFIGS");
		HordesLogger.blankLine();
		CommonConfigHandler.init();
		ClientConfigHandler.init();
		//generate data files
		HordesLogger.blankLine();
		HordesLogger.heading("CHECKING CONFIG DATA");
		HordesLogger.blankLine();
		if (DataGenerator.shouldGenerateFiles()) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DataGenerator::generateAssets);
			DataGenerator.generateData();
		} else HordesLogger.logInfo("Config data files are up to date, skipping data/asset generation");
		HordesLogger.markVolatile();
	}

	@SubscribeEvent
	public static void constructMod(FMLConstructModEvent event) {
		HordeEventPacketHandler.initPackets();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		DataRegistry.init();
		MinecraftForge.EVENT_BUS.register(new HordeEventHandler());
	}

	@SubscribeEvent
	public static void loadClient(FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(HordeClientHandler.INSTANCE);
	}

	@SubscribeEvent
	public static void addPackFinders(AddPackFindersEvent event) {
		Path path = FMLPaths.CONFIGDIR.get().resolve("hordes");
		event.addRepositorySource(consumer -> consumer.accept(Pack.readMetaAndCreate("hordes-invasions-config", Component.literal("Hordes Config"), true,
				str -> new PathPackResources("hordes-invasions-config", true, path), event.getPackType(), Pack.Position.TOP, PackSource.BUILT_IN)));
	}

}
