package org.scriptonbasestar.cache.geo.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: archmagece
 * @Since: 2013-11-27 15:42
 */
public class BSMap<K, V> extends HashMap<K, V>{

	public static <A,B> Map<A,B> create(A key, B val){
		Map map = new HashMap<A, B>();
		map.put(key, val);
		return map;
	}

	public Map<K,V> createNew(K key, V val){
		return BSMap.create(key, val);
	}
}
