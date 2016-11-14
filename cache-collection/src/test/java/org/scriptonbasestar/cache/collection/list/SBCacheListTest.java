package org.scriptonbasestar.cache.collection.list;

import org.junit.Test;
import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

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
		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.load(0));
		System.out.println(dataFeed.loadAll());
		System.out.println(dataFeed.load(0));
		System.out.println(dataFeed.loadAll());
	}

	@Test
	public void testLoadCache(){
		final ListDataFeed dataFeed = new ListDataFeed();
		SBCacheSimpleList<String> cacheList = new SBCacheSimpleList<>(new SBCacheListLoader<String>(){
			@Override
			public String load(int index) throws SBCacheLoadFailException {
				return dataFeed.load(index);
			}
			@Override
			public List<String> loadAll() throws SBCacheLoadFailException {
				return dataFeed.loadAll();
			}
		});

		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
	}
}
