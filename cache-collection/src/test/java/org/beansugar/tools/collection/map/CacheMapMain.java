package org.scriptonbasestar.cache.collection.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2015-08-26-12
 */
public class CacheMapMain {
	public static void main(String[] args) throws InterruptedException {
		//=========================== Sample ===========================
		BSCacheMap<Long, String> cacheMap = new BSCacheMap<>(new SBCacheMapLoader<Long, String>() {
			@Override
			public String loadOne(Long id) {
				return ""+id;
			}

			@Override
			public Map<Long, String> loadAll() {
				Map<Long, String> map = new HashMap<>();
				map.put(3L, "i333");
				map.put(5L, "i555");
				map.put(6L, "i666");
				map.put(7L, "i777");
				return map;
			}
		}, 10);
		//=========================== /샘플 ===========================
		cacheMap.put(3L, "333");
		cacheMap.put(5L, "555");
		cacheMap.put(6L, "666");
		cacheMap.put(7L, "777");

		System.out.println(cacheMap.get(3L));
		System.out.println(cacheMap.get(5L));
		System.out.println(cacheMap.get(6L));
		System.out.println(cacheMap.get(7L));

		Thread.sleep(1000*10);

		System.out.println(cacheMap.get(3L));
		System.out.println(cacheMap.get(5L));
		System.out.println(cacheMap.get(6L));
		System.out.println(cacheMap.get(7L));

		Thread.sleep(1000*3);

		cacheMap.putAll();

		System.out.println(cacheMap.get(3L));
		System.out.println(cacheMap.get(5L));
		System.out.println(cacheMap.get(6L));
		System.out.println(cacheMap.get(7L));

	}
}
