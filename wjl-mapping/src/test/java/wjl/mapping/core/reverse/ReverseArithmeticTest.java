package wjl.mapping.core.reverse;

import org.junit.Test;
import wjl.mapping.core.model.FormulaForTest;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.model.TemplateForTest;

public class ReverseArithmeticTest {
    @Test
    public void test() {
        Template tpl = TemplateForTest.distanceSiSo();
        ReverseArithmetic ra = new ReverseArithmetic();
        ra.reverse(tpl, FormulaForTest.getRegister());
    }
}