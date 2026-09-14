package net.smileycorp.hordes.invasions.data.scripts.conditions.gamestages;

import com.google.gson.JsonElement;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.conditions.Condition;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class GameStageCondition implements Condition {

	protected Value<String> stage;

	public GameStageCondition(Value<String> stage) {
		this.stage = stage;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return GameStageSaveHandler.getPlayerData(ctx.getPlayer().getUUID()).hasStage(stage.get(ctx));
	}

	public static GameStageCondition deserialize(JsonElement json) {
		try {
			return new GameStageCondition(DataRegistry.readValue(DataType.STRING, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition gamestages:gamestage", e);
		}
		return null;
	}

}
