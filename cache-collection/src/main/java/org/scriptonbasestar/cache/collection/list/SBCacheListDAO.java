package org.scriptonbasestar.cache.collection.list;

import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2016-11-07
 */
public interface SBCacheListDAO<E> {
	E load(int index) throws SBCacheLoadFailException;
	List<E> loadAll() throws SBCacheLoadFailException;
}
