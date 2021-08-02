package wjl.mapping.core.reverse;

import org.junit.Test;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;
import wjl.mapping.core.model.FormulaForTest;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.SiSoTemplate;
import wjl.mapping.core.model.SimplePath;
import wjl.mapping.core.model.Template;
import wjl.mapping.core.utils.MyAssert;
import wjl.mapping.core.utils.YamlLoader;

public class ReverseSimpleDataTest {
    private FormulaRegister register = FormulaForTest.getRegister();
    private TemplateToData comparator = new TemplateToData();

    /**
     * y1 = x1
     * y2 = (x1 + x2) * 3
     */
    private Template case1_a() {
        SiSoTemplate tpl = new SiSoTemplate();

        // y1 = x1
        tpl.addDataPorter(new SimplePath("x1"), new SimplePath("y1"));

        // add1 = x1 + x2
        FormulaCall add1 = register.makeNewCall("add.1", "sum");
        tpl.addFormulaCall(add1);
        tpl.addDataPorter(tpl.getInput(), add1.getInput("x"),
            new SimplePath("x1"), SimplePath.EMPTY);
        tpl.addDataPorter(tpl.getInput(), add1.getInput("y"),
            new SimplePath("x2"), SimplePath.EMPTY);

        // mul1 = add1 * 3
        FormulaCall mul2 = register.makeNewCall("mul.2", "product");
        tpl.addFormulaCall(mul2);
        tpl.addDataPorter(add1.getOutput(), mul2.getInput("x"),
            SimplePath.EMPTY, SimplePath.EMPTY);
        mul2.getInput("y").setConstant(3);

        // y2 = mul1
        tpl.addDataPorter(mul2.getOutput(), tpl.getOutput(),
            SimplePath.EMPTY, new SimplePath("y2"));

        return tpl;
    }

    /**
     * case1_a的反转模板。
     * x1 = y1
     */
    private Template case1_b() {
        Template tpl = new Template("output", "input");
        DataProvider input = tpl.getInput("output");
        DataRecipient output = tpl.getOutput("input");

        // y1 = x1
        tpl.addDataPorter(input, output, new SimplePath("y1"), new SimplePath("x1"));

        // mul2 = y2 / 3
        FormulaCall mul2 = register.makeNewCall("mul.2", "x");
        tpl.addFormulaCall(mul2);
        tpl.addDataPorter(input, mul2.getInput("product"),
            new SimplePath("y2"), SimplePath.EMPTY);
        mul2.getInput("y").setConstant(3);

        // add1 = mul2 - y1
        FormulaCall add1 = register.makeNewCall("add.1", "y");
        tpl.addFormulaCall(add1);
        tpl.addDataPorter(mul2.getOutput(), add1.getInput("sum"),
            SimplePath.EMPTY, SimplePath.EMPTY);
        tpl.addDataPorter(input, add1.getInput("x"),
            new SimplePath("y1"), SimplePath.EMPTY);

        // x2 = add1
        tpl.addDataPorter(add1.getOutput(), output,
            SimplePath.EMPTY, new SimplePath("x2"));

        return tpl;
    }

    @Test
    public void test1() {
        Template a = case1_a();
        Template b = case1_b();
        ReverseArithmetic reverse = new ReverseArithmetic();
        Template ra = reverse.reverse(a, FormulaForTest.getRegister());
        Template rb = reverse.reverse(b, FormulaForTest.getRegister());
        Object aData = comparator.templateToData(a);
        Object bData = comparator.templateToData(b);
        Object raData = comparator.templateToData(ra);
        Object rbData = comparator.templateToData(rb);
        System.out.println(YamlLoader.objToStr(raData));
        MyAssert.assertEquals(raData, bData);
        MyAssert.assertEquals(rbData, aData);
        //TemplateToVizTest.showPositive(tpl, "ReverseSimpleDataTest.1");
        //TemplateToVizTest.showReverse(rev, "ReverseSimpleDataTest.1");
    }
}