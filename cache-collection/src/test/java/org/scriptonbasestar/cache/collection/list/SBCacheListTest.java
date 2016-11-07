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
	public void test(){
		SBCacheList<String> cacheList = new SBCacheList<>(new SBCacheListLoader<String>(){
			@Override
			public String load(int index) throws SBCacheLoadFailException {
				return ListDataFeed.getData(index);
			}
			@Override
			public List<String> loadAll() throws SBCacheLoadFailException {
				return ListDataFeed.getDataList();
			}
		});

		System.out.println(cacheList.get(0));
		System.out.println(cacheList);
	}
}
