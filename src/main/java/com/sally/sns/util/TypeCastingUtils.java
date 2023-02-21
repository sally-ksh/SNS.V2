package com.sally.sns.util;

public class TypeCastingUtils {
	public static <E, T> E fromAndSecTo(Object obj, Class<T> clazz, Class<E> secClazz) {
		return secClazz.cast(fromAndTo(obj, clazz));
	}

	public static <T> T fromAndTo(Object obj, Class<T> clazz) {
		return clazz.cast(obj);  // need to object,  TODO ClassCastException
	}
}
