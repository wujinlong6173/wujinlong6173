package wjl.mapping.reverse;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.Template;
import wjl.mapping.utils.MyAssert;
import wjl.utils.YamlUtil;

import java.util.Map;

public class L3vpnToNetTest {
    private FormulaRegister register = FormulaForTest.getRegister();
    private TemplateToData comparator = new TemplateToData();

    @Test
    public void testInputOutputSameName() {
        Map<?,?> testcase = YamlUtil.file2obj(Map.class,
            "src/test/data/templates/case5_InputOutputSameName.yaml");
        TemplateFromData parser = new TemplateFromData(register);
        Template a = parser.templateFromData(testcase.get("l3vpn_to_net"));
        Template b = parser.templateFromData(testcase.get("net_to_l3vpn"));
        Assert.assertNull(parser.getErrors());

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
