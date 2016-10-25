package org.beansugar.tools.collection.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: archmagece
 * @Since: 2013-11-27 15:42
 */
public final class MapBuilder<K, V> {

	private final Map<K, V> map;

	/**
	 * 기본적으로 HashMap 사용
	 */
	private MapBuilder() {
		this.map = new HashMap<>();
	}

	/**
	 * 특정 Map을 쓰려면 입력
	 * @param map
	 */
	private MapBuilder(Map<K, V> map) {
		this.map = map;
	}

	public static <K, V> MapBuilder<K, V> create(Class<K> keyType, Class<V> valType){
		return new MapBuilder<>();
	}
	public static <K, V> MapBuilder<K, V> create(Map<K, V> map) {
		return new MapBuilder<>(map);
	}

	public MapBuilder<K, V> add(K key, V val) {
		this.map.put(key, val);
		return this;
	}

	public MapBuilder<K, V> add(Map<K, V> map) {
		this.map.putAll(map);
		return this;
	}

	public Map<K, V> build() {
		return this.map;
	}
}
