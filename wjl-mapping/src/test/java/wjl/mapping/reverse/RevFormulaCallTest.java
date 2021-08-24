package wjl.mapping.reverse;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.formula.DefaultFormulaRegister;
import wjl.mapping.formula.FormulaRegisterSupportId;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.SiSoTemplate;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;

import java.util.HashMap;
import java.util.Map;

public class RevFormulaCallTest {
    private FormulaRegister register = buildRegister();

    @Test
    public void testChooseResult() {
        Template tpl = manyParamTpl();
        FormulaCall fc1 = tpl.getFormulas().get(0);
        DataProvider p1 = fc1.getOutput();
        DataRecipient p2 = fc1.getInput("p2");
        DataRecipient p3 = fc1.getInput("p3");
        DataRecipient p4 = fc1.getInput("p4");
        DataRecipient p5 = fc1.getInput("p5");
        Map<String, Integer> fc1Cost = register.getParamsCost(fc1.getFormulaName());

        // 第一次没有选中最低费用，第二次选中最低费用
        RevFormulaCall rc1 = new RevFormulaCall(fc1, fc1Cost);
        Assert.assertFalse(wrap(rc1, p1));
        Assert.assertFalse(wrap(rc1, p2));
        Assert.assertFalse(wrap(rc1, p3));
        Assert.assertTrue(wrap(rc1, p4));
        Assert.assertEquals("p5", rc1.getResultName());
        Assert.assertEquals(62, rc1.getCost()); // 3 * 4 + 50 = 62
        Assert.assertTrue(wrap(rc1, p5));
        Assert.assertEquals("p1", rc1.getResultName());
        Assert.assertEquals(22, rc1.getCost()); // 3 * 4 + 10 = 22

        // 第一次就选中最低费用
        RevFormulaCall rc2 = new RevFormulaCall(fc1, fc1Cost);
        Assert.assertFalse(wrap(rc2, p5));
        Assert.assertFalse(wrap(rc2, p4));
        Assert.assertFalse(wrap(rc2, p3));
        Assert.assertTrue(wrap(rc2, p2));
        Assert.assertEquals("p1", rc2.getResultName());
        Assert.assertEquals(22, rc2.getCost()); // 3 * 4 + 10 = 22
        Assert.assertFalse(wrap(rc2, p1));
        Assert.assertEquals("p1", rc2.getResultName());
        Assert.assertEquals(22, rc2.getCost()); // 3 * 4 + 10 = 22

        // 第一次选择了参数p3，不支持反向计算，第二次选中最低费用
        RevFormulaCall rc3 = new RevFormulaCall(fc1, fc1Cost);
        Assert.assertFalse(wrap(rc3, p1));
        Assert.assertFalse(wrap(rc3, p5));
        Assert.assertFalse(wrap(rc3, p4));
        Assert.assertFalse(wrap(rc3, p2));
        Assert.assertTrue(wrap(rc3, p3));
        Assert.assertEquals("p1", rc3.getResultName());
        Assert.assertEquals(22, rc3.getCost()); // 3 * 4 + 10 = 22
    }

    private boolean wrap(RevFormulaCall rev, DataRecipient param) {
        return rev.dataReady(new DataPorterCost(param.getInList().get(0), false, 1));
    }

    private boolean wrap(RevFormulaCall rev, DataProvider param) {
        return rev.dataReady(new DataPorterCost(param.getOutList().get(0), true, 1));
    }

    private Template manyParamTpl() {
        SiSoTemplate tpl = new SiSoTemplate();

        FormulaCall fc1 = register.createCall("many_param", "p1");
        tpl.addFormulaCall(fc1);

        DataProvider p1 = fc1.getOutput();
        DataRecipient p2 = fc1.getInput("p2");
        DataRecipient p3 = fc1.getInput("p3");
        DataRecipient p4 = fc1.getInput("p4");
        DataRecipient p5 = fc1.getInput("p5");
        tpl.addDataPorter(tpl.getInput(), new SimplePath("x2"), p2, new SimplePath("x2"));
        tpl.addDataPorter(tpl.getInput(), new SimplePath("x3"), p3, new SimplePath("x3"));
        tpl.addDataPorter(tpl.getInput(), new SimplePath("x4"), p4, new SimplePath("x4"));
        tpl.addDataPorter(tpl.getInput(), new SimplePath("x5"), p5, new SimplePath("x5"));
        tpl.addDataPorter(p1, SimplePath.EMPTY, tpl.getOutput(), new SimplePath("y1"));
        return tpl;
    }

    private static FormulaRegister buildRegister() {
        DefaultFormulaRegister temp = new FormulaRegisterSupportId();
        temp.register("many_param", manyParam());
        return temp;
    }

    // 拥有非常多参数的公式，p1 = f(p2, p3, p4, p5)
    private static Map<String, Integer> manyParam() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("p1", 10);
        cost.put("p2", 20);
        cost.put("p3", 0);
        cost.put("p4", 40);
        cost.put("p5", 50);
        return cost;
    }
}

