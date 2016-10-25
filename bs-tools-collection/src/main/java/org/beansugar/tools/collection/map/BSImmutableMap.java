package org.beansugar.tools.collection.map;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author archmagece
 * @CreatedAt 2016-08-29 11
 */
@Slf4j
public class BSImmutableMap<K, V>{

	private HashMap<K, V> map;

	public BSImmutableMap(){
		this.map = new HashMap<>();
	}

	public BSImmutableMap(Map<K,V> map){
		this.map = new HashMap<>(map);
	}

	public V get(K key){
		return map.get(key);
	}

}
