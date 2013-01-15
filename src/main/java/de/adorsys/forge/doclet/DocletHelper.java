package de.adorsys.forge.doclet;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.RootDoc;

public class DocletHelper {
	public static boolean hasAnnotation(AnnotationDesc[] annotations, Class c) {
		return hasAnnotation(annotations, c.getSimpleName());
	}
	
	public static boolean hasAnnotation(AnnotationDesc[] annotations, String annotationName) {
		System.out.println("Lookup: " + annotationName);
		for (AnnotationDesc annotation : annotations) {
			if (annotation.annotationType().name().equals(annotationName)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getSingleOption(RootDoc root, String name) {
		String value = null;
		for (String[] options : root.options()) {
			String key = options[0];
			System.out.println("KEY: " + key);
			if (key.startsWith("-")) {
				key = key.substring(1);
			}
			if (key != null && key.equals(name) && options.length > 1) {
				value = options[1];
			}
		}
		return value;
	}


}
