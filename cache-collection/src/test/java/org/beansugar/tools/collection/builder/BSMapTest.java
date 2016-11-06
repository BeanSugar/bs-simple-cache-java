package org.scriptonbasestar.cache.collection.builder;

import org.junit.Test;

import java.util.Map;

/**
 * @author archmagece
 * @since 2014. 9. 11.
 */
public class BSMapTest {

	public enum SupportType{
		ELECTRIC, LIGHT
	}

	@Test
	public void test1(){
		Map<SupportType, String> map = MapBuilder.create(SupportType.class, String.class).add(SupportType.ELECTRIC, "전력있음 10000kw").add(SupportType.LIGHT, "50000lux").build();
	}

	@Test
	public void test2(){
		Map<SupportType, String> map = MapBuilder.create(SupportType.class, String.class).add(SupportType.ELECTRIC, "전력있음 10000kw").add(SupportType.LIGHT, "50000lux").build();
	}

}
