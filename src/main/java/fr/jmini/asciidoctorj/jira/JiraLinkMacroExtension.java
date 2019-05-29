package fr.jmini.asciidoctorj.jira;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.jruby.extension.spi.ExtensionRegistry;

public class JiraLinkMacroExtension implements ExtensionRegistry {

    @Override
    public void register(Asciidoctor asciidoctor) {
        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("content_model", ":attributes");
        options.put("pos_attrs", new ArrayList<String>(Arrays.asList(":text", ":type", ":server")));
        JiraLinkMacro macro = new JiraLinkMacro("jira", options);

        javaExtensionRegistry.inlineMacro(macro);
    }
}
