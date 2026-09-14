package net.smileycorp.hordes.invasions.data.scripts.values;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public abstract class PosValue<T extends Comparable<T>> implements Value<T> {

	private final Value<String> value;
	private final DataType<T> type;

	protected PosValue(Value<String> value, DataType<T> type) {
		this.value = value;
		this.type = type;
	}

	@Override
	public T get(HordeContext<? extends HordePlayerEvent> ctx) {
		if (!type.isNumber()) return null;
		Axis axis = Axis.byName(value.get(ctx));
		LivingEntity entity = getEntity(ctx);
		if (type == DataType.INT || type == DataType.LONG) return type.cast(entity.blockPosition().get(axis));
		return type.cast(entity.position().get(axis));
	}

	protected abstract LivingEntity getEntity(HordeContext<? extends HordePlayerEvent> ctx);
	
}
