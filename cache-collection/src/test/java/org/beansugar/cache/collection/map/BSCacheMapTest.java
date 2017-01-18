package org.beansugar.cache.collection.map;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.core.exception.BSCacheLoadFailException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author archmagece
 * @since 2016-11-07
 */
@Slf4j
public class BSCacheMapTest {

	private final BSCacheMapDataFeeder dataFeeder = new BSCacheMapDataFeeder();
	private BSCacheMap<Long, String> cacheData = new BSCacheMap<>(new BSCacheMapLoader<Long, String>() {
		@Override
		public String loadOne(Long key) throws BSCacheLoadFailException {
			return dataFeeder.loadOne(key);
		}

		@Override
		public Map<Long, String> loadAll() throws BSCacheLoadFailException {
			return dataFeeder.loadAll();
		}
	}, 10);

	//로딩 딜레이 확인
	@Test
	public void subLoadOne로딩딜레이확인() {
		int 반복횟수 = 10;

		DateTime start1 = DateTime.now();
		for (int i = 0; i < 반복횟수; i++) {
			System.out.println(dataFeeder.loadOne(0L));
		}
		DateTime end1 = DateTime.now();
		long result1 = end1.getMillis() - start1.getMillis();
		System.out.println(result1);

		DateTime start2 = DateTime.now();
		for (int i = 0; i < 반복횟수; i++) {
			System.out.println(cacheData.get(0L));
		}
		DateTime end2 = DateTime.now();
		long result2 = end2.getMillis() - start2.getMillis();
		System.out.println(result2);

		System.out.println("결과시간 비교(ms) result1 : " + result1);
		System.out.println("결과시간 비교(ms) result2 : " + result2);
		System.out.println("! 결과시간이 resutl1 > result2인게 정상");
		Assert.assertTrue(result1 > result2);
	}

	@Test
	public void subLoadAll로딩딜레이확인() {
		int 반복횟수 = 5;

		DateTime start1 = DateTime.now();
		for (int i = 0; i < 반복횟수; i++) {
			System.out.println(dataFeeder.loadAll());
		}
		DateTime end1 = DateTime.now();
		long result1 = end1.getMillis() - start1.getMillis();
		System.out.println(result1);

		DateTime start2 = DateTime.now();
		for (int i = 0; i < 반복횟수; i++) {
			System.out.println(cacheData.toString());
		}
		DateTime end2 = DateTime.now();
		long result2 = end2.getMillis() - start2.getMillis();
		System.out.println(result2);

		System.out.println("결과시간 비교(ms) result1 : " + result1);
		System.out.println("결과시간 비교(ms) result2 : " + result2);
		System.out.println("! 결과시간이 resutl1 > result2인게 정상");
		Assert.assertTrue(result1 > result2);
	}

	@Test
	public void test타임아웃이후_가져오기() {
		long now;
		long timeSpent;

		now = DateTime.now().getMillis();
		System.out.println("first move");
		for(int i=0;i<5;i++){
			cacheData.get((long) i);
		}
		timeSpent = DateTime.now().getMillis() - now;
		System.out.println("시간소요 first move : " + timeSpent);

		now = DateTime.now().getMillis();
		System.out.println("second move");
		for(long key : cacheData.keySet()){
			cacheData.get(key);
		}
		timeSpent = DateTime.now().getMillis() - now;
		System.out.println("시간소요 second move : " + timeSpent);

		now = DateTime.now().getMillis();
		System.out.println("third move");
		for(long key : cacheData.keySet()){
			cacheData.get(key);
		}
		timeSpent = DateTime.now().getMillis() - now;
		System.out.println("시간소요 third move : " + timeSpent);

		System.out.println("========================");
		System.out.println("쉬는시간 10초");
		try {
			Thread.sleep(1000*20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		now = DateTime.now().getMillis();
		System.out.println("last move");
		for(long key : cacheData.keySet()){
			cacheData.get(key);
		}
		timeSpent = DateTime.now().getMillis() - now;
		System.out.println("시간소요 last move : " + timeSpent);

	}

	private ExecutorService executor;
	@Test
	public void test멀티쓰레드환경에서싸이코_IN올OUT() throws InterruptedException {
		int maxThreadCount = 100;
		executor = Executors.newFixedThreadPool(maxThreadCount);


		Callable<Long> runnable = new Callable<Long>() {
			Random random = new Random();
			@Override
			public Long call() throws Exception {
//				long key = Math.abs(random.nextLong() % 30);
				long key = 3;
				String value0 = cacheData.get(key);
				cacheData.put(key, value0+"0");
				String value1 = cacheData.get(key);
				System.out.println(key + "  " + value0 + "  " + value1 + "  " + value0.equals(value1));
				Assert.assertEquals(value0, value1);
				Assert.assertTrue(value1.equals(value0+"0"));
				return null;
			}
		};
//		Runnable runnable = new Runnable() {
//			Random random = new Random();
//			@Override
//			public void run() {
////				long key = Math.abs(random.nextLong() % 30);
//				long key = 3;
//				String value0 = cacheData.get(key);
//				cacheData.put(key, value0+"0");
//				String value1 = cacheData.get(key);
//				System.out.println(key + "  " + value0 + "  " + value1 + "  " + value0.equals(value1));
//				Assert.assertEquals(value0, value1);
//				Assert.assertTrue(value1.equals(value0+"0"));
//			}
//		};
		for(int i=0;i<maxThreadCount;i++){
			executor.submit(runnable);
		}
		executor.awaitTermination(3, TimeUnit.SECONDS);
	}

}
