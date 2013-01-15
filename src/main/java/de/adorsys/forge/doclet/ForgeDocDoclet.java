package de.adorsys.forge.doclet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jboss.forge.shell.plugins.Alias;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

import de.adorsys.forge.doclet.model.ForgeClass;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ForgeDocDoclet extends Standard {
	
	public static boolean start(RootDoc root) {
		String title = getSingleOption(root, "windowtitle");
		System.out.println(title);
		
		List<ForgeClass> forgeClasses = new ArrayList<ForgeClass>();
		
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; i++) {
			ClassDoc c = classes[i];
			if (!DocletHelper.hasAnnotation(c.annotations(), Alias.class)) {
				continue;
			}
			ForgeClass forgeClass = new ForgeClass();
			forgeClasses.add(forgeClass);
			forgeClass.setName(c.name());
			
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
		data.put("title", title);
		data.put("forgeClasses", forgeClasses);

		DocumentationWriter.write(data);
		
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