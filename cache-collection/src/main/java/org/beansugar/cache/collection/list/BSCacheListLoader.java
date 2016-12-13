package org.beansugar.cache.collection.list;

import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with bs-simple-cache
 * @since 2016-11-06
 *
 */
public interface BSCacheListLoader<E> {
	E loadOne(int index) throws BSCacheLoadFailException;
	List<E> loadAll() throws BSCacheLoadFailException;
}
