package fr.jmini.asciidoctorj.jira;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Asciidoctor.Factory;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;
import org.junit.jupiter.api.Test;

public class JiraLinkTest {

    @Test
    public void testInlineNamedParam() throws Exception {
        List<LogRecord> logs = runTest("test_inline_named_param");
        assertThat(logs).isEmpty();
    }

    @Test
    public void testInlineOrderedParam() throws Exception {
        List<LogRecord> logs = runTest("test_inline_ordered_param");
        assertThat(logs).isEmpty();
    }

    @Test
    public void testMissingServer() throws Exception {
        List<LogRecord> logs = runTest("test_missing_server");
        assertThat(logs).hasSize(2);

        LogRecord log1 = logs.get(0);
        assertThat(log1.getSeverity()).isEqualTo(Severity.WARN);
        assertThat(log1.getMessage()).isEqualTo("jira: server is not defined");

        LogRecord log2 = logs.get(1);
        assertThat(log2.getSeverity()).isEqualTo(Severity.WARN);
        assertThat(log2.getMessage()).isEqualTo("jira: server is not defined");
    }

    @Test
    public void testSimple() throws Exception {
        List<LogRecord> logs = runTest("test_simple");
        assertThat(logs).isEmpty();
    }

    @Test
    public void testSpecificServers() throws Exception {
        List<LogRecord> logs = runTest("test_specific_servers");
        assertThat(logs).isEmpty();
    }

    @Test
    public void testText() throws Exception {
        List<LogRecord> logs = runTest("test_text");
        assertThat(logs).isEmpty();
    }

    private List<LogRecord> runTest(String fileName) throws IOException, URISyntaxException {
        Path exampleFolder = Paths.get("src/test/resources/")
                .toAbsolutePath();
        String content = new String(Files.readAllBytes(exampleFolder.resolve(fileName + ".adoc")), StandardCharsets.UTF_8);
        String expected = new String(Files.readAllBytes(exampleFolder.resolve(fileName + ".html")), StandardCharsets.UTF_8);

        Asciidoctor asciidoctor = Factory.create();
        InMemoryLogHanlder logHandler = new InMemoryLogHanlder();
        asciidoctor.registerLogHandler(logHandler);

        AttributesBuilder attributesBuilder = AttributesBuilder.attributes()
                .setAnchors(false)
                .sectionNumbers(false);

        OptionsBuilder optionsBuilder = OptionsBuilder.options()
                .attributes(attributesBuilder)
                .baseDir(exampleFolder.toFile())
                .docType("article")
                .safe(SafeMode.UNSAFE);
        String html = asciidoctor.convert(content, optionsBuilder);

        assertThat(html).isEqualTo(expected);

        return logHandler.getLogs();
    }
}
