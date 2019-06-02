package fr.jmini.asciidoctorj.jira.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class JiraLinkUtilityTest {

    @Test
    void testCompute() throws Exception {
        MessageCollector collector1 = new MessageCollector();
        JiraLink link1 = JiraLinkUtility.compute(collector1, "TASK-123", null, "link", "https://jira.company.com");
        assertThat(link1).isNotNull();
        assertThat(link1.getUrl()).isEqualTo("https://jira.company.com/browse/TASK-123");
        assertThat(link1.getText()).isEqualTo("TASK-123");
        assertThat(link1.getWindow()).isNull();
        assertThat(collector1.getMessages()).isEmpty();

        MessageCollector collector2 = new MessageCollector();
        JiraLink link2 = JiraLinkUtility.compute(collector2, "TASK-456", "this issue", "LINK", "https://jira.company.com/");
        assertThat(link2).isNotNull();
        assertThat(link2.getUrl()).isEqualTo("https://jira.company.com/browse/TASK-456");
        assertThat(link2.getText()).isEqualTo("this issue");
        assertThat(link2.getWindow()).isNull();
        assertThat(collector2.getMessages()).isEmpty();

        MessageCollector collector3 = new MessageCollector();
        JiraLink link3 = JiraLinkUtility.compute(collector3, "TASK-789", "", "LINK-new-window", "https://dev.company.com/jira");
        assertThat(link3).isNotNull();
        assertThat(link3.getUrl()).isEqualTo("https://dev.company.com/jira/browse/TASK-789");
        assertThat(link3.getText()).isEqualTo("TASK-789");
        assertThat(link3.getWindow()).isEqualTo("_blank");
        assertThat(collector3.getMessages()).isEmpty();

        MessageCollector collector4 = new MessageCollector();
        JiraLink link4 = JiraLinkUtility.compute(collector4, "JENKINS-57098", null, "text", "https://dev.company.com/jira");
        assertThat(link4).isNotNull();
        assertThat(link4.getUrl()).isNull();
        assertThat(link4.getText()).isEqualTo("JENKINS-57098");
        assertThat(link4.getWindow()).isNull();
        assertThat(collector4.getMessages()).isEmpty();

        MessageCollector collector5 = new MessageCollector();
        JiraLink link5 = JiraLinkUtility.compute(collector5, "JENKINS-57098", "this issue", "TEXT", "https://dev.company.com/jira");
        assertThat(link5).isNotNull();
        assertThat(link5.getUrl()).isNull();
        assertThat(link5.getText()).isEqualTo("this issue");
        assertThat(link5.getWindow()).isNull();
        assertThat(collector5.getMessages()).isEmpty();

        MessageCollector collector6 = new MessageCollector();
        JiraLink linkMissingServer = JiraLinkUtility.compute(collector6, "TEST-1234", "this issue", "LINK", null);
        assertThat(linkMissingServer).isNotNull();
        assertThat(linkMissingServer.getUrl()).isNull();
        assertThat(linkMissingServer.getText()).isEqualTo("this issue");
        assertThat(linkMissingServer.getWindow()).isNull();
        assertThat(collector6.getMessages()).containsExactly("jira: server is not defined");

        MessageCollector collector7 = new MessageCollector();
        JiraLink linkEmptyServer = JiraLinkUtility.compute(collector7, "TEST-1234", null, "LINK", "");
        assertThat(linkEmptyServer).isNotNull();
        assertThat(linkEmptyServer.getUrl()).isNull();
        assertThat(linkEmptyServer.getText()).isEqualTo("TEST-1234");
        assertThat(linkEmptyServer.getWindow()).isNull();
        assertThat(collector7.getMessages()).containsExactly("jira: server is empty");
    }

    @Test
    void testGetType() throws Exception {
        assertThat(JiraLinkType.toType("link")).isEqualTo(JiraLinkType.LINK);
        assertThat(JiraLinkType.toType("Link")).isEqualTo(JiraLinkType.LINK);
        assertThat(JiraLinkType.toType("LINK")).isEqualTo(JiraLinkType.LINK);
        assertThat(JiraLinkType.toType(" link ")).isEqualTo(JiraLinkType.LINK);

        assertThat(JiraLinkType.toType("text")).isEqualTo(JiraLinkType.TEXT);
        assertThat(JiraLinkType.toType("Text")).isEqualTo(JiraLinkType.TEXT);
        assertThat(JiraLinkType.toType("TEXT")).isEqualTo(JiraLinkType.TEXT);
        assertThat(JiraLinkType.toType(" text ")).isEqualTo(JiraLinkType.TEXT);

        assertThat(JiraLinkType.toType("link_new_window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("Link_new_window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("Link_New_Window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("LINK_NEW_WINDOW")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType(" link_new_window ")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);

        assertThat(JiraLinkType.toType("link-new-window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("Link-new-window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("Link-New-Window")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType("LINK-NEW-WINDOW")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);
        assertThat(JiraLinkType.toType(" link-new-window ")).isEqualTo(JiraLinkType.LINK_NEW_WINDOW);

        assertThat(JiraLinkType.toType(null)).isEqualTo(JiraLinkType.LINK);
        assertThat(JiraLinkType.toType("")).isEqualTo(JiraLinkType.LINK);
        assertThat(JiraLinkType.toType("xxx")).isEqualTo(JiraLinkType.LINK);
    }
}
