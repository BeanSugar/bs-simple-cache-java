package org.scriptonbasestar.cache.collection.strategy;

/**
 * @author archmagece
 * @since 2016-11-14
 * 데이터 초기화 타이밍
 */
public enum InitStrategy {
	//처음 시작시 초기데이터 가져오기
	AT_START,
	//처음 호출시 초기데이터 가져오기
	FIRST_CALL
}
