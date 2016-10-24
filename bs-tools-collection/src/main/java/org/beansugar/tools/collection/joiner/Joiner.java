package org.beansugar.tools.collection.joiner;


import org.beansugar.tools.core.check.Check;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author archmagece
 * @with bs-tools-basic
 * @since 2015-06-10-15
 */
public final class Joiner<ENTITY> {

	private final String separator;

	private final Collection<ENTITY> toAppend;

	private Joiner(String separator) {
		this(separator, new ArrayList<ENTITY>());
	}

	private Joiner(String separator, Collection<ENTITY> toAppend) {
		Check.notNull(separator, "separator should not be null");
		this.separator = separator;
		this.toAppend = toAppend;
	}

	public static Joiner on(String separator) {
		return new Joiner(separator);
	}

	public static Joiner on(char separator) {
		return new Joiner(String.valueOf(separator));
	}

	public Joiner append(ENTITY... entities) {
		for (ENTITY e : entities) {
			this.toAppend.add(e);
		}
		return this;
	}

	public Joiner append(Collection<ENTITY> entities) {
		this.toAppend.addAll(entities);
		return this;
	}

	public String join() {
		StringBuilder sb = new StringBuilder();
		for (Object o : this.toAppend) {
			sb
					.append(o.toString())
					.append(this.separator);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
