package wjl.mapping.core.display;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.junit.Test;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.formula.TemplateForTest;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class TemplateToVizTest {
    public static void showPositive(Template tpl, String name) {
        try {
            String filename = String.format(Locale.ENGLISH, "example/%s.png", name);
            String viz = new TemplateToViz().display(tpl, false);
            Graphviz.fromString(viz).render(Format.PNG).toFile(new File(filename));
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static void showReverse(Template tpl, String name) {
        try {
            String filename = String.format(Locale.ENGLISH, "example/%s_rev.png", name);
            String viz = new TemplateToViz().display(tpl, true);
            Graphviz.fromString(viz).render(Format.PNG).toFile(new File(filename));
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    @Test
    public void showDistanceMiSo() throws IOException {
        Template tpl = TemplateForTest.distanceMiSo();
        String viz = new TemplateToViz().display(tpl, false);
        System.out.print(viz);
        Graphviz.fromString(viz).render(Format.PNG)
            .toFile(new File("example/distanceMiSo.png"));
    }

    @Test
    public void showDistanceSiSo() throws IOException {
        Template tpl = TemplateForTest.distanceSiSo();
        String viz = new TemplateToViz().display(tpl, false);
        System.out.print(viz);
        Graphviz.fromString(viz).render(Format.PNG)
            .toFile(new File("example/distanceSiSo.png"));
    }

    @Test
    public void showDistanceWithConst() throws IOException {
        Template tpl = TemplateForTest.distanceWithConst();
        String viz = new TemplateToViz().display(tpl, false);
        System.out.print(viz);
        Graphviz.fromString(viz).render(Format.PNG)
            .toFile(new File("example/distanceWithConst.png"));
    }

    @Test
    public void showManyParam() throws IOException {
        Template tpl = TemplateForTest.manyParam();
        String viz = new TemplateToViz().display(tpl, false);
        System.out.print(viz);
        Graphviz.fromString(viz).render(Format.PNG)
            .toFile(new File("example/manyParam.png"));
    }
}