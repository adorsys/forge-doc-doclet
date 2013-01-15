package de.adorsys.forge.doclet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ForgeDocDoclet extends Standard {
	public static boolean start(RootDoc root) {
		String title = getSingleOption(root, "windowtitle");
		System.out.println(title);
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; i++) {
			System.out.println(classes[i]);
			MethodDoc[] methods = classes[i].methods();
			for (int j = 0; j < methods.length; j++) {
				MethodDoc m = methods[j];
				if (m.isPublic()) {
					System.out.println("\t" + m.name());
					Parameter[] parameters = m.parameters();
					for (int k = 0; k < parameters.length; k++) {
						Parameter p = parameters[k];
						for (AnnotationDesc annotationDesc : p.annotations()) {
							System.out.println("\t\t\t" + annotationDesc.annotationType().name());
							for (ElementValuePair evp : annotationDesc.elementValues()) {
								System.out.println("\t\t\t\t" + evp.element().name() + " / " + evp.value());
							}
						}
						System.out.println("\t\t" + p.name() + ": " + p.type().qualifiedTypeName());
					}
				}
			}
		}

		
		Map data = new HashMap();
		try {
			File out = new File(System.getProperty("user.dir"));
			System.out.println("OUT: " + out.getAbsolutePath());
			File index = new File(out, "index.html");

			Configuration configuration = new Configuration();
			configuration.setClassForTemplateLoading(ForgeDocDoclet.class, "/");
		    Template template = configuration.getTemplate("doc.freemarker.html");
			
		    OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(index));
			template.process(data, output);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private static String getSingleOption(RootDoc root, String name) {
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