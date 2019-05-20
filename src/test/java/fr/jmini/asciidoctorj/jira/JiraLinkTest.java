package fr.jmini.asciidoctorj.jira;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Asciidoctor.Factory;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.jupiter.api.Test;

public class JiraLinkTest {

    @Test
    public void testInlineNamedParam() throws Exception {
        runTest("test_inline_named_param");
    }

    @Test
    public void testInlineOrderedParam() throws Exception {
        runTest("test_inline_ordered_param");
    }

    @Test
    public void testMissingServer() throws Exception {
        runTest("test_missing_server");
    }

    @Test
    public void testSimple() throws Exception {
        runTest("test_simple");
    }

    @Test
    public void testSpecificServers() throws Exception {
        runTest("test_specific_servers");
    }

    @Test
    public void testText() throws Exception {
        runTest("test_text");
    }

    private void runTest(String fileName) throws IOException, URISyntaxException {
        Path exampleFolder = Paths.get("src/test/resources/")
                .toAbsolutePath();
        String content = new String(Files.readAllBytes(exampleFolder.resolve(fileName + ".adoc")), StandardCharsets.UTF_8);
        String expected = new String(Files.readAllBytes(exampleFolder.resolve(fileName + ".html")), StandardCharsets.UTF_8);

        Asciidoctor asciidoctor = Factory.create();

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
    }
}
