package bg.sofia.uni.fmi.mjt.cache;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MemCacheTest {

    private MemCache<String, Integer> defaultCache;
    private MemCache<String, Integer> capacityCache;
    private String key1 = "First #1";
    private String key2 = "Second #2";
    private String nonKey = "Third #3";
    private Integer value1 = 0b0001;
    private Integer value2 = 0b0010;
    private Integer value3 = 0b0011;
    private LocalDateTime expiresAt1 = LocalDateTime.now().plusHours(1);
    private LocalDateTime expiresAt2 = LocalDateTime.now().minusHours(1);
    private LocalDateTime expiresAt3 = LocalDateTime.now().plusHours(2);

    @Before
    public void initializeMemCache() {
        defaultCache = new MemCache<>();
        capacityCache = new MemCache<>(2);
    }

    @Test
    public void getValueOfKey() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);
        putDataInCache(defaultCache, key2, value2, expiresAt2);

        assertEquals("value should be " + value1, value1, defaultCache.get(key1));
        assertNull("found but expired", defaultCache.get(key2));
        assertNull("key not found", defaultCache.get(nonKey));
        assertEquals("hit should be 1", 1, defaultCache.getHit());
        assertEquals("miss should be 2", 2, defaultCache.getMiss());
    }

    @Test
    public void addDataIfKeyAndValueAreNotNullAndKeyIsNew() {
        capacityCache.set(key1, value1, expiresAt1);

        Map<String, Integer> genericCache = capacityCache.getGenericCache();
        Map<String, LocalDateTime> expirationCache = capacityCache.getExpirationCache();

        assertEquals("key should be " + key1, key1, genericCache.keySet().toArray()[0]);
        assertEquals("value should be " + value1, value1, genericCache.get(key1));
        assertEquals("expiration date should be " + expiresAt1, expiresAt1, expirationCache.get(key1));
    }

    @Test
    public void addDataIfKeyAndValueAreNotNullAndKeyIsAlreadyPresentShouldUpdateValueAndExpiration() {
        capacityCache.set(key1, value1, expiresAt1);
        capacityCache.set(key1, value2, expiresAt2);

        Map<String, Integer> genericCache = capacityCache.getGenericCache();
        Map<String, LocalDateTime> expirationCache = capacityCache.getExpirationCache();

        assertEquals("key should be " + key1, key1, genericCache.keySet().toArray()[0]);
        assertEquals("value should be " + value2, value2, genericCache.get(key1));
        assertEquals("expiration date should be " + expiresAt2, expiresAt2, expirationCache.get(key1));
        assertEquals("there should be only one element", 1, genericCache.size());
    }

    @Test
    public void addDataIfCacheIsFullButThereIsAnExpiredCellShouldUpdateIt() {
        capacityCache.set(key1, value1, expiresAt1);
        capacityCache.set(key2, value2, expiresAt2);

        capacityCache.set(nonKey, value3, expiresAt3);

        Map<String, Integer> genericCache = capacityCache.getGenericCache();
        Map<String, LocalDateTime> expirationCache = capacityCache.getExpirationCache();

        assertNull("there should be no key " + value2, genericCache.get(key2));
        assertEquals("value should be " + value3, value3, genericCache.get(nonKey));
        assertEquals("expiration date should be " + expiresAt3, expiresAt3, expirationCache.get(nonKey));
        assertEquals("there should be 2 elements", 2, genericCache.size());
    }

    @Test(expected = CapacityExceededException.class)
    public void fullMemCacheShouldThrowCapacityExceededException() {
        putDataInCache(capacityCache, key1, value1, expiresAt1);
        putDataInCache(capacityCache, key2, value2, expiresAt1);

        capacityCache.set(nonKey, value3, expiresAt1);
    }

    @Test
    //test to get Inspiration
    public void getExpirationTest() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);

        assertEquals("expiration date should be " + expiresAt1, expiresAt1, defaultCache.getExpiration(key1));
        assertNull("no data with specified key found", defaultCache.getExpiration(nonKey));
    }

    @Test
    public void removeItemTest() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);

        assertTrue("the item exists", defaultCache.remove(key1));
        assertEquals("there should be 0 elements", 0, defaultCache.getGenericCache().size());
        assertFalse("the item does not exist", defaultCache.remove(nonKey));
    }

    @Test
    public void sizeTestIfEmpty() {
        assertEquals("size should be 0", 0, defaultCache.size());
    }

    @Test
    public void sizeTestIfNotEmpty() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);
        putDataInCache(defaultCache, key2, value2, expiresAt2);

        assertEquals("size should be 2", 2, defaultCache.size());
    }

    @Test
    public void clearCacheTest() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);
        putDataInCache(defaultCache, key2, value2, expiresAt2);

        defaultCache.clear();

        assertEquals("genericCache size should be 0", 0, defaultCache.getGenericCache().size());
        assertEquals("expirationCache size should be 0", 0, defaultCache.getExpirationCache().size());
        assertEquals("hit should be 0", 0, defaultCache.getHit());
        assertEquals("miss should be 0", 0, defaultCache.getMiss());
    }

    @Test
    public void getHitRateWhenHitsAreZero() {
        assertEquals("hit rate should be 0.0", 0, defaultCache.getHitRate(), 0.01);
    }

    @Test
    public void getHitRateWhenMissesAreZero() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);
        defaultCache.get(key1);

        assertEquals("hit should be 1", 1, defaultCache.getHit());
        assertEquals("miss should be 0", 0, defaultCache.getMiss());
        assertEquals("hit rate should be 1.0", 1.0, defaultCache.getHitRate(), 0.01);
    }

    @Test
    public void getHitRateTest() {
        putDataInCache(defaultCache, key1, value1, expiresAt1);
        putDataInCache(defaultCache, key2, value2, expiresAt3);
        putDataInCache(defaultCache, nonKey, value3, expiresAt2);

        defaultCache.get(key1);
        defaultCache.get(key2);
        defaultCache.get(nonKey);

        assertEquals("hit should be 2", 2, defaultCache.getHit());
        assertEquals("miss should be 1", 1, defaultCache.getMiss());
        assertEquals("hit ratio should be 0.66", 0.66, defaultCache.getHitRate(), 0.01);
    }


    //hard-coding the data in either defaultCache or capacityCache
    private void putDataInCache(MemCache<String, Integer> cache, String key, Integer value, LocalDateTime expiresAt) {
        cache.getGenericCache().put(key, value);
        cache.getExpirationCache().put(key, expiresAt);
    }
}