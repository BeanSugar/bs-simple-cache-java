package org.beansugar.cache.collection.list;


import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author archmagece
 * @since 2016-11-07
 */
@Slf4j
public class BSCacheListFeedingLoader implements BSCacheListLoader<String> {
	private static final List<String> sampleData = new ArrayList<>();

	static {
		for (int i = 0; i < 30; i++) {
			log.debug("add item {}", i);
			sampleData.add("" + i + i + i + i + i);
		}
	}

	@Override
	public String loadOne(int index) throws BSCacheLoadFailException {
		try {
			log.debug("loadOne thread sleep 3000");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sampleData.get(index);
	}

	@Override
	public List loadAll() throws BSCacheLoadFailException {
		try {
			log.debug("loadAll thread sleep 3000");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sampleData;
	}
}
