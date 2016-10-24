package org.beansugar.tools.collection.list;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2014-12-10-11
 */
public class Extractor {
	/**
	 * List&lt;Dto&gt; 형태에서 Dto.name 을 얻어서 List&lt;Dto.name&gt;을 만든다.
	 * @param list
	 * @param fieldName
	 * @param <T>
	 * @param <R>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static <T, R> List<R> ext(List<T> list, String fieldName, Class<R> resultType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		Class<?> clazz = list.get(0).getClass();
		Method method = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
		List<R> resultList = new ArrayList<>();
		for(T t : list){
			resultList.add((R) method.invoke(t));
		}
		return resultList;
	}
}
