package wjl.mapping.reverse;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.formula.FormulaForTest;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.Template;
import wjl.mapping.utils.MyAssert;
import wjl.utils.YamlUtil;

import java.util.Map;

public class ReverseSimpleDataTest {
    private FormulaRegister register = FormulaForTest.getRegister();
    private TemplateToData comparator = new TemplateToData();

    @Test
    public void case1() {
        Map<?,?> testcase = YamlUtil.file2obj( Map.class,
            "src/test/data/templates/case1_AddMul.yaml");
        TemplateFromData parser = new TemplateFromData(register);
        Template a = parser.templateFromData(testcase.get("positive"));
        Template b = parser.templateFromData(testcase.get("opposite"));
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

    /**
     * 模板有两个数据源，而且数据源包含同名属性，反转算法不能弄混。
     */
    @Test
    public void case2() {
        Map<?,?> testcase = YamlUtil.file2obj(Map.class,
            "src/test/data/templates/case2_TwoInputs.yaml");
        TemplateFromData parser = new TemplateFromData(register);
        Template a = parser.templateFromData(testcase.get("positive"));
        Template b = parser.templateFromData(testcase.get("opposite"));
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

    @Test
    public void case3() {
        Map<?,?> testcase = YamlUtil.file2obj(Map.class,
            "src/test/data/templates/case3_ReusePartData.yaml");
        TemplateFromData parser = new TemplateFromData(register);
        Template a = parser.templateFromData(testcase.get("positive"));
        Template b = parser.templateFromData(testcase.get("opposite"));
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

    @Test
    public void case4() {
        Map<?,?> testcase = YamlUtil.file2obj(Map.class,
            "src/test/data/templates/case4_Distance.yaml");
        TemplateFromData parser = new TemplateFromData(register);
        Template a = parser.templateFromData(testcase.get("positive"));
        Template b = parser.templateFromData(testcase.get("opposite"));
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