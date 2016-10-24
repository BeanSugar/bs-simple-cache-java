package org.beansugar.tools.collection.extractor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author archmagece
 * @with bs-tools-java
 * @since 2014-12-10-11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Extractor {

	/**
	 * Collection &gt; ENTITY &lt; 에서 ENTITY.field를 List로 가져옴.
	 *
	 * @param collection
	 * @param fieldName
	 * @param <ENTITY>
	 * @param <RESULT>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static <ENTITY, RESULT> List<RESULT> extract(Collection<ENTITY> collection, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		Iterator<ENTITY> itr = collection.iterator();
		Class<?> clazz = null;
		Method method = null;
		ENTITY entity1 = itr.next();
		clazz = entity1.getClass();
		method = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
		List<RESULT> resultList = new ArrayList<>();
		//1번 Entity 먼저 넣고.
		resultList.add((RESULT) method.invoke(entity1));
		while(itr.hasNext()){
			resultList.add((RESULT) method.invoke(itr.next()));
		}
		return resultList;
	}

}
