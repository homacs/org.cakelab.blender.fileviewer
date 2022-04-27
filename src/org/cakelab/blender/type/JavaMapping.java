package org.cakelab.blender.type;

public class JavaMapping {

	public static boolean isScalar(Class<?> type) {
		return (
				type.equals(byte.class)
				|| type.equals(char.class)
				|| type.equals(short.class)
				|| type.equals(int.class)
				|| type.equals(long.class)
				|| type.equals(float.class)
				|| type.equals(double.class)
				|| type.equals(boolean.class)
				|| type.equals(Byte.class)
				|| type.equals(Character.class)
				|| type.equals(Short.class)
				|| type.equals(Integer.class)
				|| type.equals(Long.class)
				|| type.equals(Float.class)
				|| type.equals(Double.class)
				|| type.equals(Boolean.class)
				);
	}

	public static Class<?> getPrimitiveTypeUnboxed(Class<?> type) {
		if (type.equals(byte.class)
				|| type.equals(char.class)
				|| type.equals(short.class)
				|| type.equals(int.class)
				|| type.equals(long.class)
				|| type.equals(float.class)
				|| type.equals(double.class)
				|| type.equals(boolean.class))
		{
			return type;
		}
		if (type.equals(Byte.class)) return byte.class;
		if (type.equals(Character.class)) return char.class;
		if (type.equals(Short.class)) return short.class;
		if (type.equals(Integer.class)) return int.class;
		if (type.equals(Long.class)) return long.class;
		if (type.equals(Float.class)) return float.class;
		if (type.equals(Double.class)) return double.class;
		if (type.equals(Boolean.class)) return boolean.class;
		if (type.equals(String.class)) return String.class;
		throw new IllegalArgumentException("not a primitive type: " + type.getSimpleName());
	}

}
