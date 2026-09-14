package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.minecraft.world.Difficulty;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class GameDifficultyCondition implements Condition {

	protected Value<?> difficulty;

	public GameDifficultyCondition(Value<?> difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		Comparable<?> value = difficulty.get(ctx);
		return ctx.getWorld().getDifficulty() == (value instanceof String ? Difficulty.byName((String) value) : Difficulty.byId((Integer) value));
	}

	public static GameDifficultyCondition deserialize(JsonElement json) {
		try {
			Value<?> getter;
			try {
				getter = DataRegistry.readValue(DataType.STRING, json);
			} catch (Exception e) {
				getter = DataRegistry.readValue(DataType.INT, json);
			}
			return new GameDifficultyCondition(getter);
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:game_difficulty", e);
		}
		return null;
	}

}
