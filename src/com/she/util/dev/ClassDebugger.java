package com.she.util.dev;

import java.lang.reflect.Field;

import android.util.Log;

public class ClassDebugger {
	public static void showClassInfo(Class<?> inClass, Object inObject, String debugTitle) {
		
		
		Field[] fields = inClass.getFields();
		
		for(Field f : fields)
		{
			try {
				Log.d(debugTitle, f.getName() + " " + f.get(inObject));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getClassInfo(Class<?> inClass, Object inObject) {
		
		
		Field[] fields = inClass.getFields();
		StringBuilder sb = new StringBuilder();
		
		for(Field f : fields)
		{
			try {
				sb.append(f.getName() + " = " + f.get(inObject) + " / ");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}

