package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public interface Value<T extends Comparable<T>> {
    
    T get(HordeContext<? extends HordePlayerEvent> ctx);

    interface Deserializer {

        <T extends Comparable<T>> Value<T> apply(JsonObject obj, DataType<T> type);

    }

}
