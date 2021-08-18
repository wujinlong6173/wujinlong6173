package wjl.mapping.reverse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjl.mapping.model.DataPorter;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 在反转算法表示一个模板，管理算法需要的各种数据。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
class RevTemplate {
    private final static Logger LOGGER = LoggerFactory.getLogger(RevTemplate.class);
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
        // 反转模板对调原模板的输入和输出
        Template retTpl = new Template(originalTpl.getOutputs().keySet(), originalTpl.getInputs().keySet());

        // 从选中的搬运工开始，反向查找所有选中的搬运工
        Queue<DataPorterCost> queue = initBuildQueue();
        Map<RevFormulaCall, FormulaCall> retCalls = new HashMap<>();
        while (!queue.isEmpty()) {
            String srcDataName;
            String dstDataName;
            SimplePath srcPath;
            SimplePath dstPath;
            RevFormulaCall srcRevCall;
            RevFormulaCall dstRevCall;

            DataPorterCost seed = queue.poll();
            DataPorter porter = seed.getPorter();
            if (seed.isReverse()) {
                DataRecipient src = porter.getRecipient();
                DataProvider dst = porter.getProvider();
                srcDataName = src.getName();
                dstDataName = dst.getName();
                srcPath = porter.getDstPath();
                dstPath = porter.getSrcPath();
                srcRevCall = recipientToRevCall.get(src);
                dstRevCall = providerToRevCall.get(dst);
            } else {
                DataProvider src = porter.getProvider();
                DataRecipient dst = porter.getRecipient();
                srcPath = porter.getSrcPath();
                dstPath = porter.getDstPath();
                srcDataName = src.getName();
                dstDataName = dst.getName();
                srcRevCall = providerToRevCall.get(src);
                dstRevCall = recipientToRevCall.get(dst);
            }

            FormulaCall srcCall = makeFormulaCall(retTpl, srcRevCall, retCalls, queue);
            FormulaCall dstCall = makeFormulaCall(retTpl, dstRevCall, retCalls, queue);
            DataProvider dataProvider = getProvider(retTpl, srcCall, srcDataName);
            DataRecipient dataRecipient = getRecipient(retTpl, dstCall, dstDataName);
            if (dataProvider == null) {
                // 数据源即不是函数，也不是反向模板的输入，肯定是利用已还原数据的情况
                DataPorter ahead = searchPrePorter(retTpl, srcDataName, srcPath);
                if (ahead != null) {
                    dataProvider = ahead.getProvider();
                    srcPath = SimplePath.replacePrefix(srcPath, ahead.getDstPath(), ahead.getSrcPath());
                } else {
                    LOGGER.error("search previous porter failed, {}-{}", srcDataName, srcPath.toString());
                    break;
                }
            }

            retTpl.addDataPorter(dataProvider, dataRecipient, srcPath, dstPath);
        }

        return retTpl;
    }

    private Queue<DataPorterCost> initBuildQueue() {
        // 从选中的搬运工开始，反向查找所有选中的搬运工
        Queue<DataPorterCost> queue = new LinkedList<>();
        for (RevTemplateInput revTplInput : revTplInputs.values()) {
            for (DataPorterCost start : revTplInput.getBestPorters()) {
                queue.offer(start);
            }
        }
        return queue;
    }

    private DataPorter searchPrePorter(Template result, String dataName, SimplePath proPath) {
        DataRecipient tplOutput = result.getOutput(dataName);
        if (tplOutput == null) {
            return null; // 不应该执行到这里
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

    private DataProvider getProvider(Template result, FormulaCall call, String dataName) {
        if (call == null) {
            return result.getInput(dataName);
        }
        return call.getOutput();
    }

    private DataRecipient getRecipient(Template result, FormulaCall call, String dataName) {
        if (call == null) {
            return result.getOutput(dataName);
        }
        return call.getInput(dataName);
    }

    private FormulaCall makeFormulaCall(Template tpl, RevFormulaCall revCall,
        Map<RevFormulaCall, FormulaCall> cache, Queue<DataPorterCost> queue) {
        if (revCall == null) {
            return null;
        }

        FormulaCall call = cache.get(revCall);
        if (call == null) {
            call = register.createCall(revCall.getFormulaName(), revCall.getResultName());
            cache.put(revCall, call);
            tpl.addFormulaCall(call);
            FormulaCall original = revCall.getCall();
            for (RevFormulaParam inputOnly : revCall.getInputParams()) {
                queue.addAll(inputOnly.getProvided());
                DataRecipient originalInput = original.getInput(inputOnly.getName());
                if (originalInput != null) {
                    call.getInput(inputOnly.getName()).setConstant(originalInput.getConstant());
                }
            }
        }
        return call;
    }
}
