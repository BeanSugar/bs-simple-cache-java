package org.beansugar.cache.collection.list;

import lombok.extern.slf4j.Slf4j;
import org.beansugar.cache.collection.stratege.LoadStrategy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author archmagece
 * @with bs-simple-cache
 * @since 2016-11-06
 *
 * LoadStrategy.ONE 적용불가.
 * 다른 스레드에서 값을 수정하고 하면 out of index exception이 수시로 발생할듯..
 */
@Slf4j
public class BSCacheList<E> extends ArrayList<E> {

	private static final Object syncObject = new Object();
	private LocalTime updatedAt = LocalTime.now();
	private final int timeoutSec = 300;
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private final BSCacheListLoader<E> loader;
	private final LoadStrategy loadStrategy;

	public BSCacheList(BSCacheListLoader<E> loader, LoadStrategy loadStrategy) {
		super(Collections.synchronizedList(new ArrayList()));
		log.trace("BSCacheList Constructor - s");
		this.loader = loader;
		this.loadStrategy = loadStrategy;
		super.addAll(this.loader.loadAll());
		log.trace("BSCacheList Constructor - e");
	}

	public BSCacheList(List<? extends E> collection, BSCacheListLoader<E> loader, LoadStrategy loadStrategy) {
		super(Collections.synchronizedList(collection));
		log.trace("BSCacheList Constructor - s");
		this.loader = loader;
		this.loadStrategy = loadStrategy;
		super.addAll(this.loader.loadAll());
		log.trace("BSCacheList Constructor - e");
	}

	static class RunLoader<E> implements Runnable {
		BSCacheList sbCacheList;
		BSCacheListLoader<E> loader;
		public RunLoader(BSCacheList sbCacheList, BSCacheListLoader<E> loader){
			this.sbCacheList = sbCacheList;
			this.loader = loader;
		}
		@Override
		public void run() {
			synchronized (syncObject){
				sbCacheList.clear();
				sbCacheList.addAll(loader.loadAll());
			}
		}
	}

	@Override
	public E get(int index) {
		log.trace("BSCacheList get - s");
		if (updatedAt.plusSeconds(timeoutSec).isBefore(LocalTime.now())) {
			log.trace("BSCacheList get - cache time expired");
			updatedAt.plusSeconds(timeoutSec);

			if(loadStrategy == LoadStrategy.ONE){
				log.trace("BSCacheList get - loadOne");
				super.set(index, loader.loadOne(index));
			}else{
				log.trace("BSCacheList get - loadAll");
				executor.execute(new RunLoader(this, loader));
			}
		}
		return super.get(index);
	}
}
