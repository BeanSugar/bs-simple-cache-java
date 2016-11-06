package org.scriptonbasestar.cache.collection.list;

import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with sb-simple-cache
 * @since 2016-11-06
 *
 */
public interface SBCacheListLoader<E> {
	E load(int index) throws SBCacheLoadFailException;
	List<E> loadAll() throws SBCacheLoadFailException;
}
