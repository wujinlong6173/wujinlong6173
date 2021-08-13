package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.SimplePath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 在反转算法表示模板的一个数据源，处理算法中和此数据源有关的数据。
 *
 * @author wujinlong
 * @since 2021-8-13
 */
class RevTemplateInput {
    private final DataProvider originalInput;
    private final List<DataPorterCost> bestPorters;
    private final Set<DataPorter> doneInTplInput;

    RevTemplateInput(DataProvider originalInput) {
        this.originalInput = originalInput;
        doneInTplInput = new HashSet<>();
        bestPorters = new ArrayList<>();
    }

    List<DataPorterCost> dataReady(DataPorter porter, int dataCost) {
        if (!saveBestPorter(porter, dataCost)) {
            return null;
        }

        List<DataPorterCost> nextList = new ArrayList<>();
        for (DataPorter each : originalInput.getOutList()) {
            if (doneInTplInput.contains(each)) {
                // 该搬运工反方向的费用更低，没必要尝试正方向。
                continue;
            }
            if (porter.getSrcPath().contain(each.getSrcPath())) {
                // 已经还原出数据 a.b 的情况下，可以利用数据 a.b 或 a.b.c 还原其它数据。
                nextList.add(new DataPorterCost(each, false, dataCost));
            }
        }
        return nextList;
    }

    private boolean saveBestPorter(DataPorter porter, int dataCost) {
        doneInTplInput.add(porter);

        // 假如已存在existPath=a.b ，那么
        // newPath=a 时返回真，newPath=a.b 或 a.b.c 时返回假
        // newPath = x.y.z 时返回真，即和a.b没有任何关系
        SimplePath newPath = porter.getSrcPath();
        for (DataPorterCost exist : bestPorters) {
            SimplePath existPath = exist.getPorter().getSrcPath();
            if (existPath.contain(newPath)) {
                return false;
            }
        }

        bestPorters.add(new DataPorterCost(porter, true, dataCost));
        return true;
    }

    List<DataPorterCost> getBestPorters() {
        return bestPorters;
    }
}
