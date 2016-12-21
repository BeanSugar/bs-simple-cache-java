package org.beansugar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;
import org.beansugar.cache.core.util.TimeCheckerUtil;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private Map<K, Long> timeoutChecker;
	private Map<K, V> data;
	private final int timeoutSec;
	private final BSCacheMapLoader<K, V> cacheLoader;

	public BSCacheMap(BSCacheMapLoader<K,V> cacheLoader, int timeoutSec) {
		super();
		data = Collections.synchronizedMap(new HashMap<K, V>());
		timeoutChecker = Collections.synchronizedMap(new HashMap<K, Long>());
		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
	}

	//DateTime이 자동생성되는거 없애줄 필요.
	private Random random = new Random(DateTime.now().getMillis());

	public V get(final K key) {
		log.trace("get data - key : {}", key);

		if (data.containsKey(key) && timeoutChecker.containsKey(key) && TimeCheckerUtil.checkExpired(timeoutChecker.get(key), timeoutSec)){
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					put(key, cacheLoader.loadOne(key));
//				}
//			});
			return data.get(key);
		}else{
			try{
				put(key, cacheLoader.loadOne(key));
				return data.get(key);
			}catch (BSCacheLoadFailException e){
				data.remove(key);
				timeoutChecker.remove(key);
				throw e;
			}
		}
	}

	public V put(K key, V value) {
		log.trace("put data - key : {} , value : {}", key, value);
		timeoutChecker.put(key, DateTime.now().plusSeconds(Math.abs(random.nextInt() % timeoutSec)).getMillis());
		return data.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		log.trace("putAll data");
		for (K k : m.keySet()) {
			timeoutChecker.put(k, DateTime.now().plusSeconds(timeoutSec).getMillis());
		}
		data.putAll(m);
	}

	public void postponeOne(K k) {
		timeoutChecker.put(k, DateTime.now().plusSeconds(timeoutSec).getMillis());
	}

	public void postponeAll() {
		for (K k : timeoutChecker.keySet()) {
			postponeOne(k);
		}
	}

	/**
	 * 새로고침 필요한 데이터.. 1초후에 만료되도록 셋
	 *
	 * @param k
	 */
	public void expireOne(K k) {
		timeoutChecker.put(k, DateTime.now().plusSeconds(1).getMillis());
	}

	public void expireAll() {
		for (K k : timeoutChecker.keySet()) {
			expireOne(k);
		}
	}

	public V remove(Object key) {
		timeoutChecker.remove(key);
		return data.remove(key);
	}

	public void removeAll() {
		timeoutChecker.clear();
		data.clear();
	}

	/**
	 * 강제로 모든데이터 교체
	 */
	public void loadAll() {
		try {
			Map<K,V> cacheTmp = cacheLoader.loadAll();
			synchronized (data){
				data.clear();
				data.putAll(cacheTmp);
				postponeAll();
			}
		} catch (BSCacheLoadFailException e) {
//			throw e;
//			e.printStackTrace();
			if (log.isTraceEnabled()) {
				log.trace(e.toString());
			}
			return;
		}
	}

}
