package wjl.mapping.core.model;

import org.junit.Ignore;

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
        FormulaRegister temp = new FormulaRegister();
        temp.register("distance", distance());
        temp.register("circle_space", circleSpace());
        temp.register("many_param", manyParam());
        return temp;
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
}
