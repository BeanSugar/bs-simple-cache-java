package org.beansugar.cache.collection.list;

import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2016-11-07
 *
 * 외부 데이터에 엑세스하는기능. DB Redis,memcached, api..
 */
public interface BSCacheListDAO<E> {
	E load(int index) throws BSCacheLoadFailException;
	List<E> loadAll() throws BSCacheLoadFailException;
}
