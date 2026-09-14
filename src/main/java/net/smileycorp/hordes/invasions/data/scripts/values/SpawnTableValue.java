package net.smileycorp.hordes.invasions.data.scripts.values;


import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.HordesParsingException;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.HordeSpawnData;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class SpawnTableValue implements Value<String> {

	@Override
	public String get(HordeContext<? extends HordePlayerEvent> ctx) {
		HordeSpawnData data = ctx.getSpawnData();
		return data == null ? null : data.getTable() == null ? null : data.getTable().getName().toString();
	}
	
	public static <T extends Comparable<T>> SpawnTableValue deserialize(JsonObject object, DataType<T> type) {
		if (type != DataType.STRING) {
			HordesLogger.logError("invalid value for hordes:spawn_table", new HordesParsingException("Expected type" + type + " is not a string"));
			return null;
		}
		return new SpawnTableValue();
	}
	
}
