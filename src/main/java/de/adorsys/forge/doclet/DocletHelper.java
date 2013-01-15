package de.adorsys.forge.doclet;

import com.sun.javadoc.AnnotationDesc;

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
	

}
