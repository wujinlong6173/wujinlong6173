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
        Assert.assertFalse(rc1.dataReady(p1.getOutList().get(0), true, 1));
        Assert.assertFalse(rc1.dataReady(p2.getInList().get(0), false, 1));
        Assert.assertFalse(rc1.dataReady(p3.getInList().get(0), false, 1));
        Assert.assertTrue(rc1.dataReady(p4.getInList().get(0), false, 1));
        Assert.assertEquals("p5", rc1.getResultName());
        Assert.assertEquals(54, rc1.getCost());
        Assert.assertTrue(rc1.dataReady(p5.getInList().get(0), false, 1));
        Assert.assertEquals("p1", rc1.getResultName());
        Assert.assertEquals(14, rc1.getCost());

        // 第一次就选中最低费用
        RevFormulaCall rc2 = new RevFormulaCall(fc1, fc1Cost);
        Assert.assertFalse(rc2.dataReady(p5.getInList().get(0), false, 1));
        Assert.assertFalse(rc2.dataReady(p4.getInList().get(0), false, 1));
        Assert.assertFalse(rc2.dataReady(p3.getInList().get(0), false, 1));
        Assert.assertTrue(rc2.dataReady(p2.getInList().get(0), false, 1));
        Assert.assertEquals("p1", rc2.getResultName());
        Assert.assertEquals(14, rc2.getCost());
        Assert.assertFalse(rc2.dataReady(p1.getOutList().get(0), true, 1));
        Assert.assertEquals("p1", rc2.getResultName());
        Assert.assertEquals(14, rc2.getCost());

        // 第一次选择了参数p3，不支持反向计算，第二次选中最低费用
        RevFormulaCall rc3 = new RevFormulaCall(fc1, fc1Cost);
        Assert.assertFalse(rc3.dataReady(p1.getOutList().get(0), true, 1));
        Assert.assertFalse(rc3.dataReady(p5.getInList().get(0), false, 1));
        Assert.assertFalse(rc3.dataReady(p4.getInList().get(0), false, 1));
        Assert.assertFalse(rc3.dataReady(p2.getInList().get(0), false, 1));
        Assert.assertTrue(rc3.dataReady(p3.getInList().get(0), false, 1));
        Assert.assertEquals("p1", rc3.getResultName());
        Assert.assertEquals(14, rc3.getCost());
    }
}
