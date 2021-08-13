package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.SimplePath;
import wjl.mapping.core.model.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * 在反转算法表示一个模板，管理算法需要的各种数据。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
class RevTemplate {
    private final FormulaRegister register;
    private final Template originalTpl;
    private final Map<DataProvider, RevTemplateInput> revTplInputs;
    private final Map<DataProvider, RevFormulaCall> providerToRevCall;
    private final Map<DataRecipient, RevFormulaCall> recipientToRevCall;

    RevTemplate(Template tpl, FormulaRegister register) {
        this.register = register;
        this.originalTpl = tpl;

        providerToRevCall = new HashMap<>();
        recipientToRevCall = new HashMap<>();
        for (FormulaCall call : tpl.getFormulas()) {
            RevFormulaCall revCall = new RevFormulaCall(call, register.getParamsCost(call.getFormulaName()));
            providerToRevCall.put(call.getOutput(), revCall);
            for (DataRecipient callInput : call.getInputs().values()) {
                recipientToRevCall.put(callInput, revCall);
            }
        }

        revTplInputs = new HashMap<>();
        for (DataProvider tplInput : tpl.getInputs().values()) {
            revTplInputs.put(tplInput, new RevTemplateInput(tplInput));
        }
    }

    /**
     * 为模板的输入或输出准备好了一份数据。注意规律，先到达的数据，费用肯定比后到达的要低。
     *
     * @param porter 原模板中的数据搬运工
     * @param reverse 记正向搬运，或反向搬运
     * @param dataCost 数据的费用
     * @return 如果数据能够继续往下传递，则生成新的候选节点
     */
    List<DataPorterCost> dataReady(DataPorter porter, boolean reverse, int dataCost) {
        if (!reverse) {
            // 数据搬运的目的地是模板的输出，没有任何意义，忽略它。
            return null;
        }

        // 数据搬运的目的地是模板的输入，只保留费用最低的
        RevTemplateInput revTplInput = revTplInputs.get(porter.getProvider());
        return revTplInput.dataReady(porter, dataCost);
    }

    /**
     * 根据数据搬运的目的地查找公式调用，如果目的地是模板的输入或输出，则返回空。
     *
     * @param dataPorter 数据搬运工
     * @return 公式调用
     */
    RevFormulaCall findRevCall(DataPorterCost dataPorter) {
        if (dataPorter.isReverse()) {
            return providerToRevCall.get(dataPorter.getPorter().getProvider());
        } else {
            return recipientToRevCall.get(dataPorter.getPorter().getRecipient());
        }
    }

    Template build() {
        Template result = new Template(
            originalTpl.getOutputs().keySet(), originalTpl.getInputs().keySet());
        Map<RevFormulaCall, FormulaCall> resultCall = new HashMap<>();
        Queue<DataPorterCost> queue = new LinkedList<>();
        for (RevTemplateInput revTplInput : revTplInputs.values()) {
            for (DataPorterCost start : revTplInput.getBestPorters()) {
                queue.offer(start);
            }
        }

        while (!queue.isEmpty()) {
            DataPorterCost seed = queue.poll();
            DataPorter porter = seed.getPorter();
            if (seed.isReverse()) {
                DataRecipient src = porter.getRecipient();
                DataProvider dst = porter.getProvider();
                SimplePath srcPath = porter.getDstPath();
                RevFormulaCall srcRevCall = recipientToRevCall.get(src);
                RevFormulaCall dstRevCall = providerToRevCall.get(dst);
                if (srcRevCall == null) {
                    DataRecipient dataRecipient = getRecipient(result, resultCall, dstRevCall, dst.getName(), queue);
                    DataPorter ahead = searchAhead(result, src.getName(), srcPath);
                    if (ahead != null) {
                        result.addDataPorter(ahead.getProvider(), dataRecipient, SimplePath.replacePrefix(porter.getSrcPath(), ahead.getDstPath(), ahead.getSrcPath()), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                    } else {
                        DataProvider dataProvider = result.getInput(src.getName());
                        result.addDataPorter(dataProvider, dataRecipient, seed.isReverse() ? porter.getDstPath() : porter.getSrcPath(), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                    }
                } else {
                    DataProvider dataProvider = getProvider(result, resultCall, srcRevCall, queue);
                    DataRecipient dataRecipient = getRecipient(result, resultCall, dstRevCall, dst.getName(), queue);
                    result.addDataPorter(dataProvider, dataRecipient, seed.isReverse() ? porter.getDstPath() : porter.getSrcPath(), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                }
            } else {
                DataProvider src = porter.getProvider();
                DataRecipient dst = porter.getRecipient();
                SimplePath srcPath = porter.getSrcPath();
                RevFormulaCall srcRevCall = providerToRevCall.get(src);
                RevFormulaCall dstRevCall = recipientToRevCall.get(dst);
                if (srcRevCall == null) {
                    DataRecipient dataRecipient = getRecipient(result, resultCall, dstRevCall, dst.getName(), queue);
                    DataPorter ahead = searchAhead(result, src.getName(), srcPath);
                    if (ahead != null) {
                        result.addDataPorter(ahead.getProvider(), dataRecipient, SimplePath.replacePrefix(porter.getSrcPath(), ahead.getDstPath(), ahead.getSrcPath()), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                    } else {
                        DataProvider dataProvider = result.getInput(src.getName());
                        result.addDataPorter(dataProvider, dataRecipient, seed.isReverse() ? porter.getDstPath() : porter.getSrcPath(), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                    }
                } else {
                    DataProvider dataProvider = getProvider(result, resultCall, srcRevCall, queue);
                    DataRecipient dataRecipient = getRecipient(result, resultCall, dstRevCall, dst.getName(), queue);
                    result.addDataPorter(dataProvider, dataRecipient, seed.isReverse() ? porter.getDstPath() : porter.getSrcPath(), seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
                }
            }
        }

        return result;
    }

    private DataPorter searchAhead(Template result, String name, SimplePath proPath) {
        DataRecipient tplOutput = result.getOutput(name);
        if (tplOutput == null) {
            return null;
        }
        // 如果存在多个匹配的，选择最精确匹配的
        DataPorter best = null;
        for (DataPorter ahead : tplOutput.getInList()) {
            if (ahead.getDstPath().contain(proPath)) {
                if (best == null || best.getDstPath().contain(ahead.getDstPath())) {
                    best = ahead;
                }
            }
        }
        return best; // 不应该返回空
    }

    private DataProvider getProvider(Template result, Map<RevFormulaCall, FormulaCall> cache,
        RevFormulaCall revCall, Queue<DataPorterCost> queue) {
        FormulaCall call = makeFormulaCall(result, revCall, cache, queue);
        return call.getOutput();
    }

    private DataRecipient getRecipient(Template result, Map<RevFormulaCall, FormulaCall> cache,
        RevFormulaCall revCall, String dataName, Queue<DataPorterCost> queue) {
        if (revCall == null) {
            return result.getOutput(dataName);
        }
        FormulaCall call = makeFormulaCall(result, revCall, cache, queue);
        return call.getInput(dataName);
    }

    private FormulaCall makeFormulaCall(Template tpl, RevFormulaCall revCall, Map<RevFormulaCall, FormulaCall> cache,
        Queue<DataPorterCost> queue) {
        FormulaCall call = cache.get(revCall);
        if (call == null) {
            call = register.createCall(revCall.getFormulaName(), revCall.getResultName());
            cache.put(revCall, call);
            tpl.addFormulaCall(call);

            FormulaCall original = revCall.getCall();
            if (Objects.equals(original.getResultName(), revCall.getResultName())) {
                // 没有反转这个公式
                for (DataRecipient input : original.getInputs().values()) {
                    call.getInput(input.getName()).setConstant(input.getConstant());
                    for (DataPorter inputPorter : input.getInList()) {
                        queue.offer(new DataPorterCost(inputPorter, false, 0));
                    }
                }
            } else {
                // 反转了这个公式
                for (DataPorter outputPorter : original.getOutput().getOutList()) {
                    queue.offer(new DataPorterCost(outputPorter, true, 0));
                }

                for (DataRecipient input : original.getInputs().values()) {
                    if (Objects.equals(input.getName(), revCall.getResultName())) {
                        continue;
                    }
                    call.getInput(input.getName()).setConstant(input.getConstant());
                    for (DataPorter inputPorter : input.getInList()) {
                        queue.offer(new DataPorterCost(inputPorter, false, 0));
                    }
                }
            }
        }
        return call;
    }

}
