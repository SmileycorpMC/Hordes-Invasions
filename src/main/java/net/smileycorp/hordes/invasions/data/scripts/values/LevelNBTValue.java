package net.smileycorp.hordes.invasions.data.scripts.values;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.invasions.data.HordesLogger;
import net.smileycorp.hordes.invasions.data.scripts.DataRegistry;
import net.smileycorp.hordes.invasions.event.HordePlayerEvent;
import net.smileycorp.hordes.invasions.data.scripts.HordeContext;

public class LevelNBTValue<T extends Comparable<T>> extends NBTValue<T> {

	private LevelNBTValue(Value<String> value, DataType<T> type) {
		super(value, type);
	}

	@Override
	protected CompoundTag getNBT(HordeContext<? extends HordePlayerEvent> ctx)  {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server.getWorldData().createTag(server.registryAccess(), new CompoundTag());
	}
	
	public static <T extends Comparable<T>> Value deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new LevelNBTValue<T>(DataRegistry.readValue(DataType.STRING, object.get("value")), type);
		} catch (Exception e) {
			HordesLogger.logError("invalid value for hordes:level_nbt", e);
		}
		return null;
	}
	
}
