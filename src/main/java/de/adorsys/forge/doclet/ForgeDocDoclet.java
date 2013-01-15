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
	
	private static boolean hasAnnotation(AnnotationDesc[] annotations, Class c) {
		return hasAnnotation(annotations, c.getSimpleName());
	}
	
	private static boolean hasAnnotation(AnnotationDesc[] annotations, String annotationName) {
		System.out.println("Lookup: " + annotationName);
		for (AnnotationDesc annotation : annotations) {
			if (annotation.annotationType().name().equals(annotationName)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean start(RootDoc root) {
		String title = getSingleOption(root, "windowtitle");
		System.out.println(title);
		
		List<ForgeClass> forgeClasses = new ArrayList<ForgeClass>();
		
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; i++) {
			ClassDoc c = classes[i];
			if (!hasAnnotation(c.annotations(), Alias.class)) {
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
		try {
			File out = new File(System.getProperty("user.dir"));
			System.out.println("OUT: " + out.getAbsolutePath());
			File index = new File(out, "index.html");

			Configuration configuration = new Configuration();
			configuration.setClassForTemplateLoading(ForgeDocDoclet.class, "/");
		    Template template = configuration.getTemplate("doc.freemarker.html");
			
		    OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(index));
			template.process(data, output);
		
			File css = new File(out, "css");
			css.mkdirs();
			FileUtils.copyInputStreamToFile(ForgeDocDoclet.class.getResourceAsStream("/bootstrap/css/bootstrap.css"), new File(css, "bootstrap.css"));

			File js = new File(out, "js");
			js.mkdirs();
			FileUtils.copyInputStreamToFile(ForgeDocDoclet.class.getResourceAsStream("/bootstrap/js/jquery-1.9.0.js"), new File(js, "jquery-1.9.0.js"));
			FileUtils.copyInputStreamToFile(ForgeDocDoclet.class.getResourceAsStream("/bootstrap/js/bootstrap.js"), new File(js, "bootstrap.js"));
			
			File img = new File(out, "img");
			img.mkdirs();
			FileUtils.copyInputStreamToFile(ForgeDocDoclet.class.getResourceAsStream("/bootstrap/img/glyphicons-halflings.png"), new File(img, "glyphicons-halflings.png"));
			
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