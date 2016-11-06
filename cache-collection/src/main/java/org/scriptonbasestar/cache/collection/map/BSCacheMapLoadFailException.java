package org.scriptonbasestar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.scriptonbasestar.cache.core.exception.BSBaseException;

/**
 * @Author archmagece
 * @CreatedAt 2016-10-21 17
 */
@Slf4j
public class BSCacheMapLoadFailException extends BSBaseException {
	public BSCacheMapLoadFailException(String message) {
		super(message);
	}
}
