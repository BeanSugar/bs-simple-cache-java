package org.beansugar.tools.collection.builder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author archmagece
 * @since 2014. 9. 11.
 */
public final class SetBuilder<ENTITY> {

	private Set<ENTITY> set;

	/**
	 * 기본적으로 HashSet 사용
	 */
	private SetBuilder() {
		this.set = new HashSet<>();
	}

	/**
	 * 특정 Set을 쓰려면 입력
	 * @param set
	 */
	private SetBuilder(Set<ENTITY> set) {
		this.set = set;
	}

	public static <ENTITY> SetBuilder<ENTITY> create(Class<ENTITY> type) {
		return new SetBuilder<>();
	}

	public static <ENTITY> SetBuilder<ENTITY> create(Set<ENTITY> set) {
		return new SetBuilder<>(set);
	}

	public SetBuilder<ENTITY> add(ENTITY entity) {
		this.set.add(entity);
		return this;
	}

	public SetBuilder<ENTITY> add(Set<ENTITY> set) {
		this.set.addAll(set);
		return this;
	}

	public SetBuilder<ENTITY> a(ENTITY... entities) {
		for (ENTITY e : entities) {
			this.set.add(e);
		}
		return this;
	}

	public Set<ENTITY> build() {
		return this.set;
	}
}
