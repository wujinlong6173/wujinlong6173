package wjl.mapping.display;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import wjl.mapping.model.Template;

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
}