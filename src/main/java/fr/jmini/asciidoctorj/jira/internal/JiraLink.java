package fr.jmini.asciidoctorj.jira.internal;

public class JiraLink {
    private String url;
    private String text;
    private String window;

    public JiraLink(String url, String text, String window) {
        super();
        this.url = url;
        this.text = text;
        this.window = window;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public Object getWindow() {
        return window;
    }
}
