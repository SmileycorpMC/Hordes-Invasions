package net.smileycorp.hordes.invasions.data.scripts.conditions.astages;

import com.alessandro.astages.infrastructure.capability.PlayerStageWrapper;
import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.conditions.Condition;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

import java.util.Locale;

public class PlayerStageCondition implements Condition {

	protected Value<String> stage;

	public PlayerStageCondition(Value<String> stage) {
		this.stage = stage;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return PlayerStageWrapper.getStages(ctx.getPlayer()).contains(stage.get(ctx).toLowerCase(Locale.US));
	}

	public static PlayerStageCondition deserialize(JsonElement json) {
		try {
			return new PlayerStageCondition(DataRegistry.readValue(DataType.STRING, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition astages:player_stage", e);
		}
		return null;
	}

}
