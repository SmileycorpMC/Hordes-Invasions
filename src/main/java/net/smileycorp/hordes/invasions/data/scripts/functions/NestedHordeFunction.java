package net.smileycorp.hordes.invasions.data.scripts.functions;

import com.google.gson.JsonElement;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.data.scripts.conditions.Condition;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

import java.util.List;

public interface NestedHordeFunction<T extends HordePlayerEvent> extends HordeFunction<T> {

    Class<T> getEventClass();

    default boolean canApply(List<Condition> conditions, HordeContext<T> ctx) {
        for (Condition condition : conditions) if (!condition.apply(ctx)) return false;
        return true;
    }

    interface Deserializer<T extends HordePlayerEvent> extends HordeFunction.Deserializer<T> {

        NestedHordeFunction<T> apply(JsonElement element);

    }

}
