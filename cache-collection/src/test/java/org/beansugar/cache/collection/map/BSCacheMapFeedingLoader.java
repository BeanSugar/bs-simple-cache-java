package org.beansugar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author archmagece
 * @CreatedAt 2016-12-07 18
 */
@Slf4j
public class BSCacheMapFeedingLoader implements BSCacheMapLoader<Long,String> {
	private static final Map<Long,String> sampleData = new HashMap<>();

	static {
		for (int i = 0; i < 30; i++) {
			log.debug("add item {}", i);
			sampleData.put((long) i, "item"+i);
		}
	}

	@Override
	public String loadOne(Long key) throws BSCacheLoadFailException {
		try {
			log.debug("loadOne thread sleep 3000");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sampleData.get(key);
	}

	@Override
	public Map<Long, String> loadAll() throws BSCacheLoadFailException {
		try {
			log.debug("loadAll thread sleep 3000");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sampleData;
	}
}
