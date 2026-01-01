package me.xarta.xserverdiag.event;

import me.xarta.xserverdiag.XServerDiag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = XServerDiag.MODID)
public final class WorldStatsTracker {
    public static final class Stats {
        public int chunks;
        public int entities;
        public int tiles;
    }

    private static final Map<ResourceKey<Level>, Stats> STATS = new ConcurrentHashMap<>();

    private WorldStatsTracker() {}

    private static Stats stats(Level level) {
        return STATS.computeIfAbsent(level.dimension(), k -> new Stats());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        if (!(e.getChunk() instanceof LevelChunk lc)) return;
        Level level = lc.getLevel();
        Stats s = stats(level);
        s.chunks++;
        s.tiles += lc.getBlockEntities().size();
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload e) {
        if (!(e.getChunk() instanceof LevelChunk lc)) return;
        Level level = lc.getLevel();
        Stats s = stats(level);
        if (s.chunks > 0) s.chunks--;
        int be = lc.getBlockEntities().size();
        s.tiles = Math.max(0, s.tiles - be);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent e) {
        var level = e.getLevel();
        if (level.isClientSide()) return;
        stats(level).entities++;
    }

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent e) {
        var level = e.getLevel();
        if (level.isClientSide()) return;
        WorldStatsTracker.Stats s = stats(level);
        if (s.entities > 0) s.entities--;
    }

    public static Stats snapshot(Level level) {
        Stats s = stats(level);
        Stats copy = new Stats();
        copy.chunks = s.chunks;
        copy.entities = s.entities;
        copy.tiles = s.tiles;
        return copy;
    }
}