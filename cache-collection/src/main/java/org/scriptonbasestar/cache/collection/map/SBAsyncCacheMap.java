package org.scriptonbasestar.cache.collection.map;

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
 */
public class SBAsyncCacheMap<K, V> {

	//last used
	private Map<K, DateTime> timeCheckerExpire = new HashMap<>();
	//first put
	private Map<K, DateTime> timeCheckerFirstPut = new HashMap<>();
	private Map<K, V> data = new HashMap<>();

	/**
	 * cacheLoader 호출실패시 구 데이터를 보존할지 보존할 경우 캐시타임을 일정시간 추가해줌. 추가되는 시간.
	 */
	private boolean allowExpiredData;
	private int addTimeOutSec;
	//사이즈 제한 필요. 선입선출
//	private K[] queue;

	private final SBCacheMapLoader<K, V> cacheLoader;
	private final int timeoutSec;

	//async
	private final int NUMBER_OF_THREAD = 5;
	private ExecutorService executor;

	public SBAsyncCacheMap(SBCacheMapLoader cacheLoader, int timeoutSec) {
		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
		this.addTimeOutSec = timeoutSec;
		this.allowExpiredData = false;
		//async
		this.executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
	}

//	private DateTime currTime = new DateTime();
//	private DateTime getCurrTime() {
//		return currTime.withMillis(System.currentTimeMillis());
//	}

	//DateTime이 자동생성되는거 없애줄 필요.
	private Random random = new Random(DateTime.now().getMillis());
	public synchronized void put(K key, V val) {
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

	public synchronized V get(K key) {
		if (!data.containsKey(key) || !timeCheckerExpire.containsKey(key) || DateTime.now().minusSeconds(timeoutSec).isAfter(timeCheckerExpire.get(key))) {
//			works.add(key);
			executor.execute(new FutureRunner(key));
		}
		return data.get(key);
	}


	private final Object syncObj1 = new Object();
	private final Object syncObj2 = new Object();
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
					synchronized (syncObj1){
						timeCheckerExpire.get(key).plusSeconds(addTimeOutSec);
					}
				}else{
					synchronized (syncObj2){
						data.remove(key);
						timeCheckerFirstPut.remove(key);
						timeCheckerExpire.remove(key);
					}
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
