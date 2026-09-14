package net.smileycorp.hordes.invasions.data.scripts.functions;

import com.google.gson.JsonElement;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public interface HordeFunction<T extends HordePlayerEvent> {

	void apply(HordeContext<T> ctx);

	interface Deserializer<T extends HordePlayerEvent> {

		HordeFunction<T> apply(JsonElement element);

	}

}