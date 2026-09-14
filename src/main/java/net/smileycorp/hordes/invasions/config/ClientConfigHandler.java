package net.smileycorp.hordes.invasions.config;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.smileycorp.hordes.invasions.data.HordesLogger;

import java.awt.*;
import java.util.List;


public class ClientConfigHandler {

	//horde event
	public static ForgeConfigSpec.ConfigValue<Integer> eventNotifyMode;
	public static ForgeConfigSpec.ConfigValue<Integer> eventNotifyDuration;
	public static ForgeConfigSpec.ConfigValue<Boolean> hordeSpawnSound;
	private static ForgeConfigSpec.ConfigValue<List<? extends Integer>> configHordeMessageColour;
	public static ForgeConfigSpec.ConfigValue<Boolean> hordeEventTintsSky;
	public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> configHordeEventSkyColour;
	public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> configHordeEventMoonColour;

	private static TextColor hordeMessageColour = null;
	private static Color hordeEventSkyColour = null;
	private static Color hordeEventMoonColour = null;

	//load config properties
	public static void init() {
		HordesLogger.logInfo("Trying to load client config");
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		//horde event
		builder.push("Horde Event");
		eventNotifyMode = builder.comment("How do players get notified of a horde event. 0: Off, 1: Chat, 2:Action Bar, 3:Title").define("eventNotifyMode", 2);
		eventNotifyDuration = builder.comment("How long in ticks does the horde notification appear? (Only applies to modes 2 and 3)").define("eventNotifyDuration", 60);
		hordeSpawnSound = builder.comment("Play a sound when a horde wave spawns.").define("hordeSpawnSound", true);
		configHordeMessageColour = builder.comment("Colour of horde notification messages in the rgb format.")
				.defineList("hordeMessageColour", Lists.newArrayList(135, 0, 0), (x) -> (int)x >= 0 && (int)x < 256);
		hordeEventTintsSky = builder.comment("Whether the sky and moon should be tinted on a horde night").define("hordeEventTintsSky", true);
		configHordeEventSkyColour = builder.comment("Colour of horde notification messages in the rgb format.")
				.defineList("hordeEventSkyColour", Lists.newArrayList(102, 0, 0), (x) -> (int)x >= 0 && (int)x < 256);
		configHordeEventMoonColour = builder.comment("Colour of horde notification messages in the rgb format.")
				.defineList("hordeEventMoonColour", Lists.newArrayList(193, 57, 15), (x) -> (int)x >= 0 && (int)x < 256);

		builder.pop();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
	}

	public static TextColor getHordeMessageColour() {
		if (hordeMessageColour == null) {
			List<? extends Integer> rgb = configHordeMessageColour.get();
			if (rgb.size() >= 3) hordeMessageColour = TextColor.fromRgb((rgb.get(0) << 16) + (rgb.get(1) << 8) + rgb.get(2));
			else hordeMessageColour = TextColor.fromRgb(0);
		}
		return hordeMessageColour;
	}

	public static Color getHordeSkyColour() {
		if (hordeEventSkyColour == null) {
			List<? extends Integer> rgb = configHordeEventSkyColour.get();
			if (rgb.size() >= 3) hordeEventSkyColour = new Color(rgb.get(0), rgb.get(1), + rgb.get(2));
			else hordeEventSkyColour = new Color(102, 0, 0);
		}
		return hordeEventSkyColour;
	}

	public static Color getHordeMoonColour() {
		if (hordeEventMoonColour == null) {
			List<? extends Integer> rgb = configHordeEventMoonColour.get();
			if (rgb.size() >= 3) hordeEventMoonColour = new Color(rgb.get(0), rgb.get(1), + rgb.get(2));
			else hordeEventMoonColour = new Color(193, 57, 15);
		}
		return hordeEventMoonColour;
	}

}
