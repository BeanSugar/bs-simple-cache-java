package org.beansugar.cache.collection.list;

import org.junit.Test;
import org.beansugar.cache.collection.stratege.LoadStrategy;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @since 2016-11-07
 */
public class SBCacheListTest {

	@Test
	public void testLoadDAO(){
		final ListDataFeed dataFeed = new ListDataFeed();

		System.out.println(dataFeed.load(0));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.load(0));
//		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.load(0));
//		System.out.println(dataFeed.loadAll());
	}

	@Test
	public void testLoadCache(){
		final ListDataFeed dataFeed = new ListDataFeed();
		BSCacheList<String> cacheList = new BSCacheList<>(new BSCacheListLoader<String>(){
			@Override
			public String loadOne(int index) throws BSCacheLoadFailException {
				return dataFeed.load(index);
			}
			@Override
			public List<String> loadAll() throws BSCacheLoadFailException {
				return dataFeed.loadAll();
			}
		}, LoadStrategy.ALL);

		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
	}
}
