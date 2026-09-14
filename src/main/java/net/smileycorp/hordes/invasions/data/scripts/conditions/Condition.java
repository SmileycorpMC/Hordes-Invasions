package net.smileycorp.hordes.invasions.data.scripts.conditions;

import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public interface Condition {

	boolean apply(HordeContext<? extends HordePlayerEvent> ctx);

}
