package org.beansugar.tools.collection.creator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author archmagece
 * @since 2015-06-07
 *
 * 더 편할줄알았는데... new Object[]{"",""} 하면 생성되는거라서 그닥 쓸일이 있을지
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayCreator {

	public static String[] create(String ... vals){
		String[] result = new String[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static int[] create(int ... vals){
		int[] result = new int[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static long[] create(long ... vals){
		long[] result = new long[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static float[] create(float ... vals){
		float[] result = new float[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static double[] create(double ... vals){
		double[] result = new double[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static char[] create(char ... vals){
		char[] result = new char[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static byte[] create(byte ... vals){
		byte[] result = new byte[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
	public static Class[] create(Class ... vals){
		Class[] result = new Class[vals.length];
		for(int i=0;i<vals.length;i++){
			result[i]=vals[i];
		}
		return result;
	}
}
