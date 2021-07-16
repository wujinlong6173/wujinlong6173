package wjl.mapping.core.model;

import org.junit.Ignore;

@Ignore
public final class TemplateForTest {
    public static Template build1() {
        // name =>
        // PI * (speed * time) => space
        Template tpl = new Template();
        FormulaRegister register = FormulaForTest.getRegister();
        FormulaCall fc1 = register.makeNewCall("distance", "distance");
        FormulaCall fc2 = register.makeNewCall("circle_space", "space");
        tpl.addFormulaCall(fc1);
        tpl.addFormulaCall(fc2);

        tpl.addDataPorter(new SimplePath("name"), new SimplePath("title"));
        tpl.addDataPorter(tpl.getInput(), fc1.getInput("time"), new SimplePath("time"), SimplePath.EMPTY);
        tpl.addDataPorter(tpl.getInput(), fc1.getInput("speed"), new SimplePath("speed"), SimplePath.EMPTY);
        tpl.addDataPorter(fc1.getOutput(), fc2.getInput("r"), SimplePath.EMPTY, SimplePath.EMPTY);
        tpl.addDataPorter(fc2.getOutput(), tpl.getOutput(), SimplePath.EMPTY, new SimplePath("space"));
        return tpl;
    }

    public static Template buildWithConst() {
        // name =>
        // PI * (speed * 12) => space
        Template tpl = new Template();
        FormulaRegister register = FormulaForTest.getRegister();
        FormulaCall fc1 = register.makeNewCall("distance", "distance");
        FormulaCall fc2 = register.makeNewCall("circle_space", "space");
        tpl.addFormulaCall(fc1);
        tpl.addFormulaCall(fc2);

        tpl.addDataPorter(new SimplePath("name"), new SimplePath("title"));
        fc1.getInput("time").setConstant("12");
        tpl.addDataPorter(tpl.getInput(), fc1.getInput("speed"), new SimplePath("speed"), SimplePath.EMPTY);
        tpl.addDataPorter(fc1.getOutput(), fc2.getInput("r"), SimplePath.EMPTY, SimplePath.EMPTY);
        tpl.addDataPorter(fc2.getOutput(), tpl.getOutput(), SimplePath.EMPTY, new SimplePath("space"));
        return tpl;
    }
}
