package org.scriptonbasestar.cache.collection.list;

import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2016-11-07
 *
 * 외부 데이터에 엑세스하는기능. DB Redis,memcached, api..
 */
public interface SBCacheListDAO<E> {
	E load(int index) throws SBCacheLoadFailException;
	List<E> loadAll() throws SBCacheLoadFailException;
}
