package wjl.mapping.core.formula;

import org.junit.Ignore;
import wjl.mapping.formula.FormulaRegisterSupportId;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.formula.DefaultFormulaRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试使用的公式。
 */
@Ignore
public class FormulaForTest {
    private static FormulaRegister formulaRegister;

    public static FormulaRegister getRegister() {
        if (formulaRegister == null) {
            formulaRegister = build();
        }
        return formulaRegister;
    }

    private static FormulaRegister build() {
        DefaultFormulaRegister temp = new FormulaRegisterSupportId();
        temp.register("add", add());
        temp.register("mul", mul());
        temp.register("minus", minus());
        temp.register("distance", distance());
        temp.register("circle_space", circleSpace());
        temp.register("many_param", manyParam());
        temp.register("copy", copy());
        return temp;
    }

    // sum = x + y
    private static Map<String, Integer> add() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("sum", 1);
        cost.put("x", 1);
        cost.put("y", 1);
        return cost;
    }

    // product = x * y
    private static Map<String, Integer> mul() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("product", 1);
        cost.put("x", 1);
        cost.put("y", 1);
        return cost;
    }

    // y = -x
    private static Map<String, Integer> minus() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("x", 1);
        cost.put("y", 1);
        return cost;
    }

    // 路程公式 distance = speed * time
    private static Map<String, Integer> distance() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("distance", 1);
        cost.put("speed", 1);
        cost.put("time", 1);
        return cost;
    }

    // 圆的面积公式 space = PI * r * r
    private static Map<String, Integer> circleSpace() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("space", 1);
        cost.put("r", 1);
        return cost;
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

    private static Map<String, Integer> copy() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("in", 1);
        cost.put("out", 1);
        return cost;
    }
}
