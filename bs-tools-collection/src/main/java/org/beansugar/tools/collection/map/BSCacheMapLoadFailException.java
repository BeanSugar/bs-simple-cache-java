package org.beansugar.tools.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.tools.core.exception.BSBaseException;

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