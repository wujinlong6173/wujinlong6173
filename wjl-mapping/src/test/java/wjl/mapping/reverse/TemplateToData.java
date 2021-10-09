package wjl.mapping.reverse;

import wjl.mapping.model.DataPorter;
import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比较两个模板是否相同，模板内所有公式调用的名称必须唯一，
 * 推荐使用FormulaRegisterTest支持的name.id格式。
 *
 * @author wujinlong
 * @since 2021-8-1
 */
public class TemplateToData {
    private Map<DataProvider, String> providerToCall;

    public Map<String, Object> templateToData(Template tpl) {
        buildMapping(tpl);

        Map<String, Object> result = new HashMap<>();
        result.put("inputs", tpl.getInputs().keySet());
        result.put("outputs", recipientsToData(tpl.getOutputs()));
        result.put("calls", callsToData(tpl.getFormulas()));
        return result;
    }

    private void buildMapping(Template tpl) {
        providerToCall = new HashMap<>();
        for (FormulaCall call : tpl.getFormulas()) {
            providerToCall.put(call.getOutput(), call.getName());
        }
    }

    private Object recipientsToData(Map<String, DataRecipient> recipients) {
        if (recipients == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, DataRecipient> each : recipients.entrySet()) {
            result.put(each.getKey(), recipientToData(each.getValue()));
        }
        return result;
    }

    private Object recipientToData(DataRecipient recipient) {
        if (recipient == null) {
            return null;
        }

        if (recipient.getInList().isEmpty()) {
            // 只有常量的特殊情况
            return recipient.getConstant();
        }

        if (recipient.getConstant() == null && recipient.getInList().size() == 1) {
            DataPorter porter = recipient.getInList().get(0);
            SimplePath dstPath = porter.getDstPath();
            if (SimplePath.EMPTY.equals(dstPath)) {
                // 只有一个赋值语句的特殊情况
                Map<String, Object> loc;
                String callName = providerToCall.get(porter.getProvider());
                if (callName == null) {
                    loc = funcTemplateInput(porter.getProvider(), porter.getSrcPath());
                } else {
                    loc = funcFormulaResult(callName, porter.getSrcPath());
                }
                return loc;
            }
        }

        Map<String, Object> result = new HashMap<>();
        if (recipient.getConstant() != null) {
            result.put("constant", recipient.getConstant());
        }
        for (DataPorter porter : recipient.getInList()) {
            Map<String, Object> loc;
            String callName = providerToCall.get(porter.getProvider());
            if (callName == null) {
                loc = funcTemplateInput(porter.getProvider(), porter.getSrcPath());
            } else {
                loc = funcFormulaResult(callName, porter.getSrcPath());
            }
            result.put(porter.getDstPath().toString(false), loc);
        }
        return result;
    }

    private Map<String, Object> funcTemplateInput(DataProvider input, SimplePath path) {
        Map<String, Object> func = new HashMap<>();
        func.put(TemplateFromData.TEMPLATE_INPUT, Arrays.asList(input.getName(), path.toString(false)));
        return func;
    }

    private Map<String, Object> funcFormulaResult(String callName, SimplePath path) {
        Map<String, Object> func = new HashMap<>();
        if (path.depth() == 0) {
            func.put(TemplateFromData.CALL_RESULT, callName);
        } else {
            func.put(TemplateFromData.CALL_RESULT, Arrays.asList(callName, path.toString(false)));
        }
        return func;
    }

    private Map<String, Object> callsToData(List<FormulaCall> formulas) {
        if (formulas == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (FormulaCall call : formulas) {
            if (call != null) {
                // 公式名称应该是name:id格式，在一个模板内唯一
                result.put(call.getName(), callToData(call));
            }
        }
        return result;
    }

    private Map<String, Object> callToData(FormulaCall call) {
        Map<String, Object> result = new HashMap<>();
        result.put("inputs", recipientsToData(call.getInputs()));
        result.put("output", call.getResultName());
        return result;
    }
}
