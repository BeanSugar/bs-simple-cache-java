package org.scriptonbasestar.cache.collection.joiner;

import org.junit.Test;

/**
 * @author archmagece
 * @with bs-tools-basic
 * @since 2015-06-10-15
 */
public class JoinerTest {
	@Test
	public void test(){

		String result = Joiner.on(",").append("feef", "jkjk", "jiowfjioe").join();
		System.out.println(result);
	}
}
