package org.beansugar.tools.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.tools.core.exception.BSRuntimeBaseException;

/**
 * @Author archmagece
 * @CreatedAt 2016-10-21 17
 */
@Slf4j
public abstract class BSCacheMapBaseRuntimeException extends BSRuntimeBaseException {
	public BSCacheMapBaseRuntimeException(String message) {
		super(message);
	}
}
