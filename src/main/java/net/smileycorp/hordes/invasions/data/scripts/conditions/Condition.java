package net.smileycorp.hordes.invasions.data.scripts.conditions;

import com.google.gson.JsonElement;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public interface Condition {

	boolean apply(HordeContext<? extends HordePlayerEvent> ctx);

	interface Deserializer {

		Condition apply(JsonElement obj);

	}

}
