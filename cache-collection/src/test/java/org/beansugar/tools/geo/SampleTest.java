package org.scriptonbasestar.cache.geo;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: archmagece
 * @Since: 2014-02-10 15:48
 */
public class SampleTest {
	public static void main(String[] args){
		Map map = new HashMap<>();
		map.remove(1);
		System.out.println(map.get(3));


		System.out.println(DateTime.now().isBefore(DateTime.now().minusMinutes(10)));
	}
}
