package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class AdvancementCondition implements Condition {

	protected Value<String> getter;

	public AdvancementCondition(Value<String> getter) {
		this.getter = getter;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		ResourceLocation advancement = new ResourceLocation(getter.get(ctx));
		ServerPlayer player = ctx.getPlayer();
		return player.getAdvancements().getOrStartProgress(player.getServer().getAdvancements().getAdvancement(advancement)).isDone();
	}

	public static AdvancementCondition deserialize(JsonElement json) {
		try {
			return new AdvancementCondition(DataRegistry.readValue(DataType.STRING, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:advancement", e);
		}
		return null;
	}

}
