package wjl.mapping.core.reverse;

import org.junit.Test;
import wjl.mapping.core.display.TemplateToVizTest;
import wjl.mapping.core.model.FormulaForTest;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.model.TemplateForTest;

public class ReverseArithmeticTest {
    @Test
    public void test() {
        Template tpl = TemplateForTest.distanceSiSo();
        ReverseArithmetic ra = new ReverseArithmetic();
        Template rev = ra.reverse(tpl, FormulaForTest.getRegister());
        TemplateToVizTest.showPositive(tpl, "distanceSiSo");
        TemplateToVizTest.showReverse(rev, "distanceSiSo");
    }
}