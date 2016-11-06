package org.scriptonbasestar.cache.collection.map;

import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

import java.util.Map;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2015-08-26 11
 */
public interface SBCacheMapLoader<K,V> {
	V loadOne(K key) throws SBCacheLoadFailException;
	Map<K, V> loadAll() throws SBCacheLoadFailException;
}
