package wjl.mapping.reverse;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.Template;
import wjl.mapping.formula.TemplateForTest;

import java.util.Map;

public class RevFormulaCallTest {
    @Test
    public void testChooseResult() {
        Template tpl = TemplateForTest.manyParam();
        FormulaCall fc1 = tpl.getFormulas().get(0);
        DataProvider p1 = fc1.getOutput();
        DataRecipient p2 = fc1.getInput("p2");
        DataRecipient p3 = fc1.getInput("p3");
        DataRecipient p4 = fc1.getInput("p4");
        DataRecipient p5 = fc1.getInput("p5");
        Map<String, Integer> fc1Cost = FormulaForTest.getRegister().getParamsCost(fc1.getFormulaName());

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
}

