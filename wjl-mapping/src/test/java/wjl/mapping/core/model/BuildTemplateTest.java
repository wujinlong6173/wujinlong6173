package wjl.mapping.core.model;

import org.junit.Test;

/**
 * 测试构造一个模板。
 */
public class BuildTemplateTest {

    @Test
    public void build() {
        Template tpl = new Template();
        FormulaRegister register = FormulaForTest.getRegister();
        FormulaCall fc1 = register.makeNewCall("distance", "distance");
        FormulaCall fc2 = register.makeNewCall("circleSpace", "space");
        tpl.addFormulaCall(fc1);
        tpl.addFormulaCall(fc2);

        tpl.addDataPorter(new SimplePath("name"), new SimplePath("title"));
        tpl.addDataPorter(tpl.getInput(), fc1.getInput("time"), new SimplePath("time"), SimplePath.EMPTY);
        tpl.addDataPorter(tpl.getInput(), fc1.getInput("speed"), new SimplePath("speed"), SimplePath.EMPTY);
        tpl.addDataPorter(fc1.getOutput(), fc2.getInput("r"), SimplePath.EMPTY, SimplePath.EMPTY);
        tpl.addDataPorter(fc2.getOutput(), tpl.getOutput(), SimplePath.EMPTY, new SimplePath("space"));
    }
}