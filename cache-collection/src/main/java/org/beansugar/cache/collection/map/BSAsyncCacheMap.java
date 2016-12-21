package org.beansugar.cache.collection.map;

import org.joda.time.DateTime;

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
public class BSAsyncCacheMap<K, V> {

	private Map<K, Long> timeCheckerExpire = new HashMap<>();
	private Map<K, V> data = new HashMap<>();
	private final int timeoutSec;
	private final BSCacheMapLoader<K, V> cacheLoader;
	private ExecutorService executor;
	//async
	private final int NUMBER_OF_THREAD = 5;

	//if fail throw exception?? or use old data
	private boolean isDataDurable;


	public BSAsyncCacheMap(BSCacheMapLoader cacheLoader, int timeoutSec) {
		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
		this.isDataDurable = false;
		//async
		this.executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
	}

//	private DateTime currTime = new DateTime();
//	private DateTime getCurrTime() {
//		return currTime.withMillis(System.currentTimeMillis());
//	}

	//DateTime이 자동생성되는거 없애줄 필요.
	private Random random = new Random(DateTime.now().getMillis());
	public void put(K key, V val) {
		//sync
		timeCheckerFirstPut.put(key, DateTime.now());
		timeCheckerExpire.put(key, DateTime.now().plusSeconds(Math.abs(random.nextInt() % timeoutSec)));
		data.put(key, val);
	}

	/**
	 * 새로고침 필요한 데이터.. 1초후에 만료되도록 셋
	 * @param key
	 */
	public void expireIt(K key){
		timeCheckerExpire.put(key, DateTime.now().plusSeconds(1));
	}

	public V get(K key) {
		//sync
		if (!data.containsKey(key) || !timeCheckerExpire.containsKey(key) || DateTime.now().minusSeconds(timeoutSec).isAfter(timeCheckerExpire.get(key))) {
//			works.add(key);
			executor.execute(new FutureRunner(key));
		}
		return data.get(key);
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
				if(allowExpiredData){
					//sync
					timeCheckerExpire.get(key).plusSeconds(addTimeOutSec);
				}else{
					//sync
					data.remove(key);
					timeCheckerFirstPut.remove(key);
					timeCheckerExpire.remove(key);
				}
			}
			try {
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}

}
