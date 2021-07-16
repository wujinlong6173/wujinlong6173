package wjl.mapping.core.display;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.junit.Test;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.model.TemplateForTest;

import java.io.File;
import java.io.IOException;

public class TemplateToVizTest {
    @Test
    public void testDisplay() throws IOException {
        Template tpl = TemplateForTest.buildWithConst();
        String viz = new TemplateToViz().display(tpl);
        System.out.print(viz);
        Graphviz.fromString(viz).render(Format.PNG)
            .toFile(new File("example/tpl1.png"));
    }
}