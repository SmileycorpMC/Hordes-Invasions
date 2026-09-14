package net.smileycorp.hordes.invasions.data.scripts.values;

import net.minecraft.nbt.CompoundTag;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.data.NBTExplorer;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;

public abstract class NBTValue<T extends Comparable<T>> implements Value<T> {

	protected final Value<String> value;
	private final DataType<T> type;
	
	public NBTValue(Value<String> value, DataType<T> type) {
		this.value = value;
		this.type = type;
	}

	@Override
	public T get(HordeContext<? extends HordePlayerEvent> ctx) {
		try {
			return new NBTExplorer<>(value.get(ctx), type).findValue(getNBT(ctx));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract CompoundTag getNBT(HordeContext<? extends HordePlayerEvent> ctx);

}
