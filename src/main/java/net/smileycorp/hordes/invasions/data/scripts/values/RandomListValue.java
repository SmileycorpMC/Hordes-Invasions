package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

import java.util.List;

public class RandomListValue<T extends Comparable<T>> implements Value<T> {

    private final List<Value<T>> values = Lists.newArrayList();

    public RandomListValue(DataType<T> type, JsonArray json) {
        json.forEach(element -> { try {
            values.add(DataRegistry.readValue(type, element));
        } catch (Exception e) {
            HordesLogger.logError("Error loading value hordes:random", e);
        }});
    }

    @Override
    public T get(HordeContext<? extends HordePlayerEvent> ctx) {
        return values.get(ctx.getRandom().nextInt(values.size())).get(ctx);
    }

}
