package wjl.mapping.reverse;

import org.junit.Test;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.SiSoTemplate;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;
import wjl.mapping.utils.MyAssert;

public class ReverseComplexDataTest {
    private FormulaRegister register = FormulaForTest.getRegister();
    private TemplateToData comparator = new TemplateToData();

    private Template case1_a() {
        SiSoTemplate tpl = new SiSoTemplate();
        DataProvider input = tpl.getInput();
        DataRecipient output = tpl.getOutput();

        // y1 = x1
        tpl.addDataPorter(new SimplePath("x1"), new SimplePath("y1"));

        // add1 = x1.age + x2.age
        FormulaCall add1 = register.createCall("add.1", "sum");
        tpl.addFormulaCall(add1);
        tpl.addDataPorter(input, add1.getInput("x"),
            new SimplePath("x1", "age"), SimplePath.EMPTY);
        tpl.addDataPorter(input, add1.getInput("y"),
            new SimplePath("x2", "age"), SimplePath.EMPTY);

        // y2 = add1
        tpl.addDataPorter(add1.getOutput(), output,
            SimplePath.EMPTY, new SimplePath("y2"));
        return tpl;
    }

    private Template case1_b() {
        Template tpl = new Template("output", "input");
        DataProvider input = tpl.getInput("output");
        DataRecipient output = tpl.getOutput("input");

        // x1 = y1
        tpl.addDataPorter(input, output,
            new SimplePath("y1"), new SimplePath("x1"));

        // add1 y = y2 - y1.age
        FormulaCall add1 = register.createCall("add.1", "y");
        tpl.addFormulaCall(add1);
        tpl.addDataPorter(input, add1.getInput("sum"),
            new SimplePath("y2"), SimplePath.EMPTY);
        tpl.addDataPorter(input, add1.getInput("x"),
            new SimplePath("y1", "age"), SimplePath.EMPTY);

        // x2.age = add1
        tpl.addDataPorter(add1.getOutput(), output,
            SimplePath.EMPTY, new SimplePath("x2", "age"));
        return tpl;
    }

    /**
     * 利用还原的数据的一部分，还原其它数据。
     */
    @Test
    public void test1() {
        Template a = case1_a();
        Template b = case1_b();
        ReverseArithmetic reverse = new ReverseArithmetic(FormulaForTest.getRegister());
        Template ra = reverse.reverse(a);
        Object bData = comparator.templateToData(b);
        Object raData = comparator.templateToData(ra);
        MyAssert.assertEquals(raData, bData);
    }
}
