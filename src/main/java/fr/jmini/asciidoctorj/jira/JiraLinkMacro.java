package fr.jmini.asciidoctorj.jira;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;

import fr.jmini.asciidoctorj.jira.internal.JiraLink;
import fr.jmini.asciidoctorj.jira.internal.JiraLinkUtility;

public class JiraLinkMacro extends InlineMacroProcessor {

    private static final Pattern JIRA_ISSUE_PATTERN = Pattern.compile("([^\\-]+)-[0-9]+");

    public JiraLinkMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    public Object process(ContentNode parent, String target, Map<String, Object> attributes) {
        Consumer<String> logger = (String message) -> log(new LogRecord(Severity.WARN, message));

        Object linkText = searchAttribute(attributes, "text", "1", parent, null);
        Object type = searchAttribute(attributes, "type", "2", parent, "jira-type");
        Object server = searchServerAttribute(parent, target, attributes);
        JiraLink link = JiraLinkUtility.compute(logger, target, linkText, type, server);

        if (link.getUrl() == null) {
            return createPhraseNode(parent, "quoted", link.getText(), attributes, new HashMap<String, Object>());
        } else {
            // Define options for an 'anchor' element:
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("type", ":link");
            options.put("target", link.getUrl());

            // Define attribute for an 'anchor' element:
            if (link.getWindow() != null) {
                attributes.put("window", link.getWindow());
            }

            // Create the 'anchor' node:
            return createPhraseNode(parent, "anchor", link.getText(), attributes, options);
        }
    }

    private Object searchServerAttribute(ContentNode parent, String target, Map<String, Object> attributes) {
        if (target != null) {
            Matcher matcher = JIRA_ISSUE_PATTERN.matcher(target);
            if (matcher.matches()) {
                String key = "jira-server-" + matcher.group(1)
                        .trim()
                        .toLowerCase();
                if (parent.getDocument()
                        .hasAttribute(key)) {
                    return parent.getDocument()
                            .getAttribute(key);
                }
            }
        }
        return searchAttribute(attributes, "server", "3", parent, "jira-server");
    }

    private Object searchAttribute(Map<String, Object> attributes, String attrKey, String attrPosition, ContentNode parent, String attrDocumentKey) {
        Object result;
        //Try to get the attribute by key:
        result = attributes.get(attrKey);
        if (result != null) {
            return result;
        }
        //Try to get the attribute by position:
        result = attributes.get(attrPosition);
        if (result != null) {
            return result;
        }
        //Try to get the attribute in the document:
        if (attrDocumentKey != null) {
            return parent.getDocument()
                    .getAttribute(attrDocumentKey);
        }
        //Not found:
        return null;
    }
}
