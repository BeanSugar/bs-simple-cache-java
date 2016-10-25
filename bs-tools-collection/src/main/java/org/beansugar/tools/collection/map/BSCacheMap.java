package org.beansugar.tools.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Collection;
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
 * 		BSCacheMap<Long, String> cacheMap = new BSCacheMap<>(new BSCacheLoader<Long, String>() {
 * 		@Override
 * 		public String load(Long id) {
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

	private Map<K, DateTime> timeChecker = new HashMap<>();
	private Map<K, V> data = new HashMap<>();
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	/**
	 * cacheLoader 호출실패시 구 데이터를 보존할지 보존할 경우 캐시타임을 일정시간 추가해줌. 추가되는 시간.
	 */
	private boolean allowExpiredData;
	//사이즈 제한 필요. 선입선출
//	private K[] queue;

	private final BSCacheLoader<K, V> cacheLoader;
	private final int timeoutSec;

	public BSCacheMap(BSCacheLoader cacheLoader, int timeoutSec) {
		this.cacheLoader = cacheLoader;
		this.timeoutSec = timeoutSec;
		this.allowExpiredData = false;
	}

	//DateTime이 자동생성되는거 없애줄 필요.
	private Random random = new Random(DateTime.now().getMillis());

	public synchronized void put(K k, V v) {
		timeChecker.put(k, DateTime.now().plusSeconds(Math.abs(random.nextInt() % timeoutSec)));
		data.put(k, v);
	}

	public void postponeIt(K k) {
		timeChecker.put(k, DateTime.now().plusSeconds(timeoutSec));
	}

	public void postponeAll() {
		for (K k : timeChecker.keySet()) {
			timeChecker.put(k, DateTime.now().plusSeconds(timeoutSec));
		}
	}

	/**
	 * 새로고침 필요한 데이터.. 1초후에 만료되도록 셋
	 *
	 * @param k
	 */
	public void expireIt(K k) {
		timeChecker.put(k, DateTime.now().plusSeconds(1));
	}

	public void expireAll() {
		for (K k : timeChecker.keySet()) {
			expireIt(k);
		}
	}

	/**
	 * 유효시간 만료 체크 후 (만료시 로딩) 후 출력
	 * @param k
	 * @return
	 */
	public synchronized V get(final K k) {
		if (!data.containsKey(k) || !timeChecker.containsKey(k)) {
			load(k);
		}else if (!data.containsKey(k) || !timeChecker.containsKey(k) || DateTime.now().minusSeconds(timeoutSec).isAfter(timeChecker.get(k))){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					load(k);
				}
			});
		}
		return data.get(k);
	}

	/**
	 * 데이터 로드 한개씩
	 * @param k
	 * @return
	 */
	public synchronized void load(K k) {
		V v = null;
		try {
			v = cacheLoader.loadOne(k);
		} catch (BSCacheMapLoadFailException e) {
//			e.printStackTrace();
			log.trace(e.toString());
			return;
		}
		if(v!=null){
			try {
				data.put(k, v);
				timeChecker.put(k, DateTime.now());
			} catch (Exception e) {
				if (allowExpiredData) {
					timeChecker.get(k).plusSeconds(timeoutSec);
				} else {
					data.remove(k);
					timeChecker.remove(k);
				}
			}
		}
	}

	/**
	 * 강제로 모든데이터 교체
	 */
	public synchronized void putAll() {
		Map<K,V> cacheTmp = null;
		try {
			cacheTmp = cacheLoader.loadAll();
		} catch (BSCacheMapLoadFailException e) {
//			e.printStackTrace();
			log.trace(e.toString());
			return;
		}
		if(cacheTmp!=null){
			data.clear();
			data.putAll(cacheTmp);
//			data = new HashMap<>(cacheLoader.loadAll());
			expireAll();
		}
	}

	public synchronized Collection<V> getValues(){
		return data.values();
	}

}
