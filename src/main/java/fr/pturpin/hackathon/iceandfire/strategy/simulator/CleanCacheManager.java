package fr.pturpin.hackathon.iceandfire.strategy.simulator;

import java.util.ArrayList;
import java.util.List;

public class CleanCacheManager implements CleanedCache {

    private static final fr.pturpin.hackathon.iceandfire.strategy.simulator.CleanCacheManager INSTANCE = new fr.pturpin.hackathon.iceandfire.strategy.simulator.CleanCacheManager();

    private final List<CleanedCache> caches = new ArrayList<>();

    public static fr.pturpin.hackathon.iceandfire.strategy.simulator.CleanCacheManager getInstance() {
        return INSTANCE;
    }

    public void addCache(CleanedCache cache) {
        caches.add(cache);
    }

    @Override
    public void clean() {
        caches.forEach(CleanedCache::clean);
    }
}
