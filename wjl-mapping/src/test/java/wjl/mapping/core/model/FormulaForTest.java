package wjl.mapping.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试使用的公式。
 */
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
}
