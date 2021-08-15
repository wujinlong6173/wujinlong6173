package wjl.mapping.reverse;

import org.junit.Test;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.MiSoTemplate;
import wjl.mapping.model.SiSoTemplate;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;
import wjl.mapping.utils.MyAssert;
import wjl.utils.YamlUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        FormulaCall add1 = register.createCall("add.1", "sum");
        tpl.addFormulaCall(add1);
        tpl.addDataPorter(tpl.getInput(), add1.getInput("x"),
            new SimplePath("x1"), SimplePath.EMPTY);
        tpl.addDataPorter(tpl.getInput(), add1.getInput("y"),
            new SimplePath("x2"), SimplePath.EMPTY);

        // mul1 = add1 * 3
        FormulaCall mul2 = register.createCall("mul.2", "product");
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
        FormulaCall mul2 = register.createCall("mul.2", "x");
        tpl.addFormulaCall(mul2);
        tpl.addDataPorter(input, mul2.getInput("product"),
            new SimplePath("y2"), SimplePath.EMPTY);
        mul2.getInput("y").setConstant(3);

        // add1 = mul2 - y1
        FormulaCall add1 = register.createCall("add.1", "y");
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
        ReverseArithmetic reverse = new ReverseArithmetic(FormulaForTest.getRegister());
        Template ra = reverse.reverse(a);
        Template rb = reverse.reverse(b);
        Object aData = comparator.templateToData(a);
        Object bData = comparator.templateToData(b);
        Object raData = comparator.templateToData(ra);
        Object rbData = comparator.templateToData(rb);
        MyAssert.assertEquals(raData, bData);
        MyAssert.assertEquals(rbData, aData);
    }

    private Template case2_a() {
        MiSoTemplate tpl = new MiSoTemplate("input1", "input2");

        FormulaCall copy1 = register.createCall("copy.1", "out");
        tpl.addFormulaCall(copy1);
        tpl.addDataPorter(tpl.getInput("input1"), copy1.getInput("in"),
            new SimplePath("desc"), new SimplePath(0));
        tpl.addDataPorter(tpl.getInput("input2"), copy1.getInput("in"),
            new SimplePath("desc"), new SimplePath(1));

        tpl.addDataPorter(copy1.getOutput(), tpl.getOutput(),
            SimplePath.EMPTY, new SimplePath("label"));
        return tpl;
    }

    private Template case2_b() {
        Template tpl = new Template(Collections.singletonList("output"), Arrays.asList("input1", "input2"));
        DataProvider input = tpl.getInput("output");
        DataRecipient out1 = tpl.getOutput("input1");
        DataRecipient out2 = tpl.getOutput("input2");

        FormulaCall copy1 = register.createCall("copy.1", "in");
        tpl.addFormulaCall(copy1);
        tpl.addDataPorter(input, copy1.getInput("out"),
            new SimplePath("label"), SimplePath.EMPTY);
        tpl.addDataPorter(copy1.getOutput(), out1,
            new SimplePath(0), new SimplePath("desc"));
        tpl.addDataPorter(copy1.getOutput(), out2,
            new SimplePath(1), new SimplePath("desc"));
        return tpl;
    }

    /**
     * 模板有两个数据源，而且数据源包含同名属性，反转算法不能弄混。
     */
    @Test
    public void test2() {
        Template a = case2_a();
        Template b = case2_b();
        ReverseArithmetic reverse = new ReverseArithmetic(FormulaForTest.getRegister());
        Template ra = reverse.reverse(a);
        Template rb = reverse.reverse(b);
        Object aData = comparator.templateToData(a);
        Object bData = comparator.templateToData(b);
        Object raData = comparator.templateToData(ra);
        Object rbData = comparator.templateToData(rb);
        MyAssert.assertEquals(raData, bData);
        MyAssert.assertEquals(rbData, aData);
    }
}