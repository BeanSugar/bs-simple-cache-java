package org.beansugar.tools.collection.map;

import java.util.Map;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2015-08-26 11
 */
public interface BSCacheLoader<K,V> {
	V loadOne(K key) throws BSCacheMapLoadFailException;
	Map<K, V> loadAll() throws BSCacheMapLoadFailException;
}
