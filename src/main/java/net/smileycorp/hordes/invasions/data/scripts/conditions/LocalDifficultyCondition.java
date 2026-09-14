package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class LocalDifficultyCondition implements Condition {

	protected Value<Double> difficulty;

	public LocalDifficultyCondition(Value<Double> difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.getWorld().getCurrentDifficultyAt(ctx.getPlayer().blockPosition()).getSpecialMultiplier() > difficulty.get(ctx);
	}

	public static LocalDifficultyCondition deserialize(JsonElement json) {
		try {
			return new LocalDifficultyCondition(DataRegistry.readValue(DataType.DOUBLE, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:local_difficulty", e);
		}
		return null;
	}

}
