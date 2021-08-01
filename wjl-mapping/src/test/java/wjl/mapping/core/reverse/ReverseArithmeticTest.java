package wjl.mapping.core.reverse;

import org.junit.Test;
import wjl.mapping.core.display.TemplateToVizTest;
import wjl.mapping.core.model.FormulaForTest;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.model.TemplateForTest;

public class ReverseArithmeticTest {
    @Test
    public void test1() {
        Template tpl = TemplateForTest.distanceSiSo();
        ReverseArithmetic ra = new ReverseArithmetic();
        Template rev = ra.reverse(tpl, FormulaForTest.getRegister());
        TemplateToVizTest.showPositive(tpl, "distanceSiSo");
        TemplateToVizTest.showReverse(rev, "distanceSiSo");
    }

    @Test
    public void test2() {
        Template tpl = TemplateForTest.distanceWithConst();
        ReverseArithmetic ra = new ReverseArithmetic();
        Template rev = ra.reverse(tpl, FormulaForTest.getRegister());
        TemplateToVizTest.showPositive(tpl, "distanceWithConst");
        TemplateToVizTest.showReverse(rev, "distanceWithConst");
    }
}