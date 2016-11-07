package org.scriptonbasestar.cache.collection.list;

import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

import java.util.List;

/**
 * @author archmagece
 * @with sb-simple-cache
 * @since 2016-11-06
 *
 */
public class SBAsyncLoader implements SBCacheListLoader {
	@Override
	public Object load(int index) throws SBCacheLoadFailException {
		return null;
	}

	@Override
	public List loadAll() throws SBCacheLoadFailException {
		return null;
	}
}
