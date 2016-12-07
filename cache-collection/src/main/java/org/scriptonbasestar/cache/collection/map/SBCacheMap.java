package org.scriptonbasestar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.scriptonbasestar.cache.core.exception.SBCacheLoadFailException;

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
 * 		SBCacheMap<Long, String> cacheMap = new SBCacheMap<>(new SBCacheMapLoader<Long, String>() {
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
public class SBCacheMap<K, V> extends HashMap<K,V> {

	private static final Object syncObject = new Object();
	private Map<K, Long> timeoutChecker;
	private final int timeoutSec;
	private final SBCacheMapLoader<K, V> cacheLoader;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	public SBCacheMap(SBCacheMapLoader cacheLoader, int timeoutSec) {
		super();
		timeoutChecker = Collections.synchronizedMap(new HashMap<K, Long>());
		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
	}

	//DateTime이 자동생성되는거 없애줄 필요.
	private Random random = new Random(DateTime.now().getMillis());

	@Override
	public V get(final Object key) {
		log.trace("get data");
		if (super.containsKey(key) && super.containsKey(key)
				&& DateTime.now().minusSeconds(timeoutSec).isAfter(timeoutChecker.get(key))){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					put((K) key, cacheLoader.loadOne((K)key));
				}
			});
		}else{
			try{
				put((K) key, cacheLoader.loadOne((K)key));
			}catch (SBCacheLoadFailException e){
				super.remove(key);
				timeoutChecker.remove(key);
				throw e;
			}
		}
		return super.get(key);
	}

	@Override
	public V put(K key, V value) {
		log.trace("put data");
		timeoutChecker.put(key, DateTime.now().plusSeconds(Math.abs(random.nextInt() % timeoutSec)).getMillis());
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		log.trace("putAll data");
		for (K k : m.keySet()) {
			timeoutChecker.put(k, DateTime.now().plusSeconds(timeoutSec).getMillis());
		}
		super.putAll(m);
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

	@Override
	public V remove(Object key) {
		timeoutChecker.remove(key);
		return super.remove(key);
	}

	public void removeAll() {
		timeoutChecker.clear();
		super.clear();
	}

	/**
	 * 강제로 모든데이터 교체
	 */
	public synchronized void loadAll() {
		try {
			synchronized (syncObject){
				Map<K,V> cacheTmp = cacheLoader.loadAll();
				super.clear();
				super.putAll(cacheTmp);
				postponeAll();
			}
		} catch (SBCacheLoadFailException e) {
//			throw e;
//			e.printStackTrace();
			log.trace(e.toString());
			return;
		}
	}

}
