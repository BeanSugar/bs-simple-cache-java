package org.beansugar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;
import org.beansugar.cache.core.util.TimeCheckerUtil;
import org.joda.time.DateTime;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2015-08-26 11
 *
 *		@Autowired
 *		UserRepository repository;
 *
 * 		BSCacheMap<Long, String> cacheMap = new BSCacheMap<>(new BSCacheMapLoader<Long, String>() {
 * 		@Override
 * 		public String loadOne(Long id) {
 * 			return repository.findOne(id);
 * 		}
 * 		@Override
 * 		public Map<Long, String> loadAll() {
 * 			List<User> repository.findAll();
 * 				Map<Long, String> map = new HashMap<>();
 * 				map.put(3L, "i333");
 * 				map.put(5L, "i555");
 * 				map.put(6L, "i666");
 * 				map.put(7L, "i777");
 * 				return map;
 * 			}
 * 		}, expireSecond);
 *
 * 	TODO forced timeout 기능이 있어야함. 자주 조회하더라도 일정시간 지나면 무조건 폐기할 필요 있음.
 */
@Slf4j
public class BSCacheMap<K, V> {

	private Random random = new Random(DateTime.now().getMillis());
	private Map<K, Long> timeoutChecker;
	private Map<K, V> data;
	private final int timeoutSec;
	private final BSCacheMapLoader<K, V> cacheLoader;

	public BSCacheMap(BSCacheMapLoader<K,V> cacheLoader, int timeoutSec) {
		timeoutChecker = Collections.synchronizedMap(new HashMap<K, Long>());
		data = Collections.synchronizedMap(new HashMap<K, V>());

		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
	}

	public V put(K key, V val) {
		log.trace("put data - key : {} , value : {}", key, val);
		timeoutChecker.put(key, DateTime.now().plusSeconds(Math.abs(random.nextInt() % timeoutSec)).getMillis());
		return data.put(key, val);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		log.trace("putAll data");
		for (K key : m.keySet()) {
			timeoutChecker.put(key, DateTime.now().plusSeconds(timeoutSec).getMillis());
		}
		data.putAll(m);
	}

	public V get(final K key) {
		log.trace("get data - key : {}", key);
		synchronized (BSCacheMap.class){
			if (data.containsKey(key) && timeoutChecker.containsKey(key) && TimeCheckerUtil.checkExpired(timeoutChecker.get(key), timeoutSec)){
				return data.get(key);
			}
		}
		try{
			V val = cacheLoader.loadOne(key);
			synchronized (BSCacheMap.class){
				put(key, val);
				return data.get(key);
			}
		}catch (BSCacheLoadFailException e){
			synchronized (BSCacheMap.class) {
				data.remove(key);
				timeoutChecker.remove(key);
			}
			throw e;
		}
	}

	/**
	 * 필요해보이면 구현예정
	 */
	public void getAll() {
		throw new NotImplementedException();
	}

	public void postponeOne(K key) {
		synchronized (BSCacheMap.class) {
			timeoutChecker.put(key, DateTime.now().plusSeconds(timeoutSec).getMillis());
		}
	}

	public void postponeAll() {
		synchronized (BSCacheMap.class) {
			for (K key : timeoutChecker.keySet()) {
				postponeOne(key);
			}
		}
	}

	/**
	 * 새로고침 필요한 데이터.. 1초후에 만료되도록 셋
	 *
	 * @param key
	 */
	public void expireOne(K key) {
		synchronized (BSCacheMap.class) {
			timeoutChecker.put(key, DateTime.now().plusSeconds(1).getMillis());
		}
	}

	public void expireAll() {
		synchronized (BSCacheMap.class) {
			for (K key : timeoutChecker.keySet()) {
				expireOne(key);
			}
		}
	}

	public V remove(Object key) {
		synchronized (BSCacheMap.class) {
			timeoutChecker.remove(key);
			return data.remove(key);
		}
	}

//	public void removeAll() {
//		synchronized (BSCacheMap.class) {
//			timeoutChecker.clear();
//			data.clear();
//		}
//	}

	/**
	 * 만료된 값을 자동으로 지우는 규칙은 없는 상태.
	 * 이걸 굳이 따로 호출할 필요가 있는지도 좀 의문.
	 */
	public void removeExpired() {
		for(K key : timeoutChecker.keySet()){
			if(TimeCheckerUtil.checkExpired(timeoutChecker.get(key), timeoutSec)){
				synchronized (BSCacheMap.class) {
					timeoutChecker.remove(key);
					data.remove(key);
				}
			}
		}
	}

	/**
	 * 강제로 모든데이터 교체
	 * 이게 있는게 맞나? 쓰이게 되면 생각해봄
	 */
//	public void loadAll() {
//		try {
//			Map<K,V> cacheTmp = cacheLoader.loadAll();
//			synchronized (BSCacheMap.class) {
//				data.clear();
//				data.putAll(cacheTmp);
//				postponeAll();
//			}
//		} catch (BSCacheLoadFailException e) {
//			log.error("loadAll 처리중 오류 {}", e.getMessage(), e);
//		}
//	}

	public Set<K> keySet(){
		return data.keySet();
	}

	public int size(){
		return data.size();
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
