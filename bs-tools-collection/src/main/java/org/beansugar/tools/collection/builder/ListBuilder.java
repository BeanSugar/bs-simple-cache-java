package org.beansugar.tools.collection.builder;

import java.util.ArrayList;
import java.util.List;

public final class ListBuilder<ENTITY> {

	private final List<ENTITY> list;

	/**
	 * 기본적으로 array extractor 사용
	 */
	private ListBuilder() {
		this.list = new ArrayList<>();
	}

	/**
	 * 특정 List를 쓰려면 입력
	 * @param list
	 */
	private ListBuilder(List<ENTITY> list) {
		this.list = list;
	}

	/**
	 * 스테틱 생성자
	 * List의 ENTITY 타입을 입력
	 * @param entityType
	 * @param <ENTITY>
	 * @return
	 */
	public static <ENTITY> ListBuilder<ENTITY> create(Class<ENTITY> entityType) {
		return new ListBuilder<>();
	}

	/**
	 * 스테틱 생성자
	 * 특정 List를 입력
	 * @param list
	 * @param <ENTITY>
	 * @return
	 */
	public static <ENTITY> ListBuilder<ENTITY> create(List<ENTITY> list) {
		return new ListBuilder<>(list);
	}

	/**
	 * 스테틱 생성자
	 * 한개짜리로 생성
	 * @param entity
	 * @param <ENTITY>
	 * @return
	 */
	public static <ENTITY> ListBuilder<ENTITY> create(ENTITY ... entity) {
		ListBuilder<ENTITY> listBuilder = new ListBuilder<>();
		listBuilder.add(entity);
		return listBuilder;
	}

	/**
	 * ENTITY 추가
	 * @param entities
	 * @return
	 */
	public ListBuilder<ENTITY> add(ENTITY ... entities) {
		for(ENTITY e : entities){
			this.list.add(e);
		}
		return this;
	}

	/**
	 * ENTITY 추가
	 * @param list
	 * @return
	 */
	public ListBuilder<ENTITY> add(List<ENTITY> list) {
		this.list.addAll(list);
		return this;
	}

	/**
	 * 완료버튼 
	 * @return
	 */
	public List<ENTITY> build() {
		return this.list;
	}
}
