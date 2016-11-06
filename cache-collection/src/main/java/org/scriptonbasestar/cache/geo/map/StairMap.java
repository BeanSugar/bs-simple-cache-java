package org.scriptonbasestar.cache.geo.map;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: archmagece
 * @Since: 2013-12-09 14:43
 *
 * 연속으로 더하기 지우기 가능
 * 재귀형태로 호출
 * 몇번째 하위에 있는 데이터 검색 같은 기능 추가
 * 일단 퍼포먼스 생각안함. 코딩편의성
 * 스레드 데인저
 */
public class StairMap <K extends Serializable, V extends Serializable> extends HashMap<K, StairMap>{

	//맨 꽁지는 맵을 안가지고있으니까 그냥 값을 들고있으라고... 5단계 4단계 등.. 단계를 제한하는 경우 씀
	public V value;
	public V getValue(){
		return this.value;
	}

	public StairMap(){}

	public StairMap(K key, StairMap value){
		this.put(key, value);
	}

	public static<K extends Serializable, V extends Serializable> StairMap<K,StairMap> create(K key, StairMap value){
		return new StairMap<K, StairMap>(key, value);
	}

	/**
	 * value는 널일수없게.. 막아놓기
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public StairMap put(K key, StairMap value) {
		if (value == null){
			value = new StairMap();
		}
		return super.put(key, value);
	}

	/**
	 * 따라가면서 키 가진놈 다 지우기
	 * @param key
	 * @return
	 */
	public StairMap removeAllRecursively(K key){
		this.remove(key);
		for (StairMap sMap : this.values()){
			sMap.removeAllRecursively(key);
		}
		return this;
	}

}
