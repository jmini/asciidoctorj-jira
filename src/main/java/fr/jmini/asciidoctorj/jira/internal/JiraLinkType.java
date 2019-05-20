package fr.jmini.asciidoctorj.jira.internal;

public enum JiraLinkType {
    TEXT, LINK, LINK_NEW_WINDOW;

    public static JiraLinkType toType(Object type) {
        if (type == null) {
            return LINK;
        } else {
            String typeAsString = type.toString()
                    .trim();
            if ("text".equalsIgnoreCase(typeAsString)) {
                return TEXT;
            } else if ("link-new-window".equalsIgnoreCase(typeAsString) || "link_new_window".equalsIgnoreCase(typeAsString)) {
                return LINK_NEW_WINDOW;
            }
        }
        return LINK;
    }
}
