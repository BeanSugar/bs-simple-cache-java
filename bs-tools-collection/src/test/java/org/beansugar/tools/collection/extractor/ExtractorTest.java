package org.beansugar.tools.collection.extractor;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author archmagece
 * @since 2015-07-04
 */
public class ExtractorTest {

	@Data
	@AllArgsConstructor
	public class SampleEntity {
		private String name;
		private String value;
	}

	private List<SampleEntity> collection;
	
	@Before
	public void before(){
		collection = new ArrayList<>();
		collection.add(new SampleEntity("name1", "value1"));
		collection.add(new SampleEntity("name2", "value3"));
		collection.add(new SampleEntity("name3", "value4"));
	}

	@Test
	public void test() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		List<String> result = Extractor.extract(collection, "name");
//		System.out.println(result);
		for(int i=0;i<this.collection.size();i++){
			Assert.assertTrue(result.get(i).equals(collection.get(i).getName()));
			Assert.assertEquals(result.get(i), collection.get(i).getName());
		}
	}
}
