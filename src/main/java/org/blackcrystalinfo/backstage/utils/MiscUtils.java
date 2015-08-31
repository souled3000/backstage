package org.blackcrystalinfo.backstage.utils;

import java.math.BigInteger;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MiscUtils {
	public static byte[] fromHex(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static String toHex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		String hex = bi.toString(16);
		int paddingLength = (bytes.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
	
	public static String writeJSON(Object t,String... ss){
		SerializeWriter out = new SerializeWriter();
		try {
			JSONSerializer serializer = new JSONSerializer(out);
//			serializer.config(SerializerFeature.QuoteFieldNames, false);
//			serializer.config(SerializerFeature.UseSingleQuotes, true);
			
			for(String s : ss){
				final String x = s;
				serializer.getPropertyFilters().add(new PropertyFilter() {
					public boolean apply(Object obj, String s, Object obj1) {
						if(s.equals(x))return false;
						return true;
					}
				});
			}
			
			serializer.write(t);
			return out.toString();
		} finally {
			out.close();
		}
	}
	
	public static String writeJSON(Object t) {
		SerializeWriter out = new SerializeWriter();
		try {
			JSONSerializer serializer = new JSONSerializer(out);
			serializer.config(SerializerFeature.QuoteFieldNames, false);
			serializer.config(SerializerFeature.UseSingleQuotes, true);
			serializer.write(t);
			return out.toString();
		} finally {
			out.close();
		}
	}
	
	public static int copyright(String cp){
		String[] ns = cp.split("\\.");
		int n=0;
		int p = 1000000000;
		for(String s : ns){
			System.out.println(s);
			n +=(Integer.parseInt(s)*(p/=100));
			System.out.println(n);
			
		}
		return n;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(copyright("1.30.2"));
	}
}
