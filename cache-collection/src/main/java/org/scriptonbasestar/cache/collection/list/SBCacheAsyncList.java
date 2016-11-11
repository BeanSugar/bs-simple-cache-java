package org.scriptonbasestar.cache.collection.list;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author archmagece
 * @with sb-simple-cache
 * @since 2016-11-06
 */
public class SBCacheAsyncList<E> extends ArrayList<E> {

	private DateTime updatedAt = DateTime.now();
	//	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private final SBCacheListLoader<E> loader;

	public SBCacheAsyncList(SBCacheListLoader<E> loader) {
		super(Collections.synchronizedList(new ArrayList()));
		this.loader = loader;
		super.addAll(this.loader.loadAll());
	}

	public SBCacheAsyncList(List<? extends E> collection, SBCacheListLoader<E> loader) {
		super(Collections.synchronizedList(collection));
		this.loader = loader;
		super.addAll(this.loader.loadAll());
	}

	@Override
	public E get(int index) {
		if (updatedAt.plusMinutes(30).isBeforeNow()) {
			updatedAt.plusMinutes(30);
//			loader.load(index);
			super.addAll(loader.loadAll());
		}
		return super.get(index);
	}
}
