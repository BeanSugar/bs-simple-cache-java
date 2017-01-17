package org.beansugar.cache.collection.list;

import org.junit.Test;
import org.beansugar.cache.collection.stratege.LoadStrategy;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @since 2016-11-07
 */
public class BSCacheListTest {

	@Test
	public void testLoad(){
		final BSCacheListFeedingLoader dataFeed = new BSCacheListFeedingLoader();

		System.out.println(dataFeed.loadOne(0));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.loadOne(0));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.loadOne(0));
//		System.out.println(dataFeed.loadAll());
	}

	@Test
	public void testLoadCache(){
		final BSCacheListFeedingLoader dataFeed = new BSCacheListFeedingLoader();
		BSCacheList<String> cacheData = new BSCacheList<>(new BSCacheListLoader<String>(){
			@Override
			public String loadOne(int index) throws BSCacheLoadFailException {
				return dataFeed.loadOne(index);
			}
			@Override
			public List<String> loadAll() throws BSCacheLoadFailException {
				return dataFeed.loadAll();
			}
		}, LoadStrategy.ALL);

		System.out.println(cacheData.get(0));
		System.out.println(cacheData);
		System.out.println(cacheData.get(0));
		System.out.println(cacheData);
		System.out.println(cacheData.get(0));
		System.out.println(cacheData);
	}
}
