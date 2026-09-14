package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.values.Value;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public class IsCalledCondition implements Condition {

	protected Value<Boolean> isCalled;

	public IsCalledCondition(Value<Boolean> isCalled) {
		this.isCalled = isCalled;
	}

	@Override
	public boolean apply(HordeContext<? extends HordePlayerEvent> ctx) {
		return ctx.isCalled() == isCalled.get(ctx);
	}

	public static IsCalledCondition deserialize(JsonElement json) {
		try {
			return new IsCalledCondition(DataRegistry.readValue(DataType.BOOLEAN, json));
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition hordes:is_called", e);
		}
		return null;
	}

}
