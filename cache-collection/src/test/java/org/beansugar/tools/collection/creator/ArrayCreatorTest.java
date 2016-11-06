package org.scriptonbasestar.cache.collection.creator;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author archmagece
 * @since 2015-06-07
 */
public class ArrayCreatorTest {
	@Test
	public void test(){
		String[] result = ArrayCreator.create("야야");
		System.out.println(result);
		System.out.println(Arrays.toString(result));
	}
	@Test
	public void test2(){
		String[] result = ArrayCreator.create("야야", "똥깨요");
		System.out.println(result);
		System.out.println(Arrays.toString(result));
	}
}
