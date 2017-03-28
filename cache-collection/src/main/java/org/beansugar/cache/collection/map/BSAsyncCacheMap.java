package org.beansugar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.util.TimeCheckerUtil;

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
 * 들만들어짐
 *
 * ConcurrentMap 참고.
 *
 */
@Slf4j
public class BSAsyncCacheMap<K, V> {

	private Random random = new Random(System.currentTimeMillis());
	private Map<K, Long> timeoutChecker;
	private Map<K, V> data;
	private final int timeoutSec;
	private final BSCacheMapLoader<K, V> cacheLoader;

	private ExecutorService executor;
	//async
	private final int NUMBER_OF_THREAD = 5;

	//if fail throw exception?? or use old data
	private boolean isDataDurable;


	public BSAsyncCacheMap(BSCacheMapLoader cacheLoader, int timeoutSec) {
		timeoutChecker = Collections.synchronizedMap(new HashMap<K, Long>());
		data = Collections.synchronizedMap(new HashMap<K, V>());

		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;

		//async
		this.isDataDurable = false;
		this.executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
	}

	public V put(K key, V val) {
		log.trace("put data - key : {} , val : {}", key, val);
		timeoutChecker.put(key, System.currentTimeMillis() + 1000 * Math.abs(random.nextInt() % timeoutSec));
		return data.put(key, val);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		log.trace("putAll data - size : {}", m.size());
		for (K key : m.keySet()) {
			timeoutChecker.put(key, System.currentTimeMillis() + 1000 * timeoutSec);
		}
		data.putAll(m);
	}

	public V get(K key) {
		//sync
		if (!data.containsKey(key) || timeoutChecker.containsKey(key) && TimeCheckerUtil.checkExpired(timeoutChecker.get(key), timeoutSec)){
//			works.add(key);
			executor.execute(new FutureRunner(key));
		}
		return data.get(key);
	}

	/**
	 * 새로고침 필요한 데이터.. 1초후에 만료되도록 셋
	 *
	 * @param key
	 */
	public void expireOne(K key) {
		timeoutChecker.put(key, System.currentTimeMillis() + 1000 * 1);
	}

	public void expireAll() {
		for (K key: timeoutChecker.keySet()) {
			expireOne(key);
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

//	private final Queue<K> works = new ConcurrentLinkedQueue<>();
	private class FutureRunner implements Runnable {
		private final K key;
		public FutureRunner(K key){
			this.key = key;
		}
		@Override
		public void run() {
			try {
				put(key, cacheLoader.loadOne(key));
//				data.put(k, cacheLoader.loadOne(k));
			} catch (Exception e) {
//				if(allowExpiredData){
//					//sync
//					timeCheckerExpire.get(key).plusSeconds(addTimeOutSec);
//				}else{
//					//sync
//					data.remove(key);
//					timeCheckerFirstPut.remove(key);
//					timeCheckerExpire.remove(key);
//				}
			}
		}
	}

}
