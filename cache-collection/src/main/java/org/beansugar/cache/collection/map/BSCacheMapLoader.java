package org.beansugar.cache.collection.map;

import org.beansugar.cache.core.exception.BSCacheLoadFailException;

import java.util.Map;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2015-08-26 11
 */
public interface BSCacheMapLoader<K,V> {
	V loadOne(K key) throws BSCacheLoadFailException;
	Map<K, V> loadAll() throws BSCacheLoadFailException;
}
