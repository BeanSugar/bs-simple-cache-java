package org.beansugar.cache.collection.map;

import org.beansugar.cache.core.exception.BSCacheLoadFailException;
import org.junit.Test;

import java.util.Map;

/**
 * @author archmagece
 * @since 2016-11-07
 */
public class BSCacheMapTest {

	@Test
	public void testLoad(){
		final BSCacheMapFeedingLoader dataFeed = new BSCacheMapFeedingLoader();

		System.out.println(dataFeed.loadOne(0L));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.loadOne(0L));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.loadOne(0L));
//		System.out.println(dataFeed.loadAll());
	}

	@Test
	public void testLoadCache(){
		final BSCacheMapFeedingLoader dataFeed = new BSCacheMapFeedingLoader();
		BSCacheMap<Long, String> cacheData = new BSCacheMap<>(new BSCacheMapLoader<Long, String>(){
			@Override
			public String loadOne(Long key) throws BSCacheLoadFailException {
				return dataFeed.loadOne(key);
			}
			@Override
			public Map<Long, String> loadAll() throws BSCacheLoadFailException {
				return dataFeed.loadAll();
			}
		}, 10);

		System.out.println(cacheData.get(0L));
		System.out.println(cacheData);
		System.out.println(cacheData.get(0L));
		System.out.println(cacheData);
		System.out.println(cacheData.get(0L));
		System.out.println(cacheData);
	}
}
