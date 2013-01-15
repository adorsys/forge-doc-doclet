package de.adorsys.forge.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

public class ForgeDocDoclet extends Standard {
	public static boolean start(RootDoc root) {
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
						System.out.println("\t\t" + p.name() + ": " + p.type().qualifiedTypeName());
					}
				}
			}
		}
		return true;
	}

	public static boolean validOptions(String[][] options, DocErrorReporter reporter) {
		return true;
	}
}