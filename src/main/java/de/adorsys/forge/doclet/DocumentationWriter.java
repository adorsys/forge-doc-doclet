package de.adorsys.forge.doclet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DocumentationWriter {
	
	public static final void write(Map data) {
		try {
			File out = new File(System.getProperty("user.dir"));
			System.out.println("OUT: " + out.getAbsolutePath());
			File indexHtml = new File(out, "index.html");
			File indexMarkdown = new File(out, "index.md");

			Configuration configuration = new Configuration();
			configuration.setClassForTemplateLoading(ForgeDocDoclet.class, "/");
		    
			Template htmlTemplate = configuration.getTemplate("doc.freemarker.html");
			htmlTemplate.process(data, new OutputStreamWriter(new FileOutputStream(indexHtml)));
		
			Template mdTemplate = configuration.getTemplate("doc.freemarker.md");
			mdTemplate.process(data, new OutputStreamWriter(new FileOutputStream(indexMarkdown)));
		
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
	}
}
