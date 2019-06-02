package fr.jmini.asciidoctorj.jira.internal;

import java.util.function.Consumer;

public final class JiraLinkUtility {

    public static JiraLink compute(Consumer<String> logger, String target, Object linkText, Object type, Object server) {
        JiraLinkType linkType = JiraLinkType.toType(type);

        String text;
        if (linkText == null) {
            text = target;
        } else {
            String linkTextTrimmed = linkText.toString()
                    .trim();
            text = (linkTextTrimmed.isEmpty()) ? target : linkTextTrimmed;
        }

        if (linkType == JiraLinkType.TEXT) {
            return new JiraLink(null, text, null);
        }

        if (server == null) {
            logger.accept("jira: server is not defined");
            return new JiraLink(null, text, null);
        }

        String serverValue = server.toString()
                .trim();
        if (serverValue.isEmpty()) {
            logger.accept("jira: server is empty");
            return new JiraLink(null, text, null);
        }
        String serverUrl = (serverValue.endsWith("/") ? serverValue : serverValue + "/") + "browse/" + target;

        String window = (linkType == JiraLinkType.LINK_NEW_WINDOW) ? "_blank" : null;

        return new JiraLink(serverUrl, text, window);
    }

    private JiraLinkUtility() {
    }
}
