package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;
import wjl.mapping.core.model.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 比较两个模板是否相同，模板内所有公式调用的名称必须唯一，
 * 推荐使用FormulaRegisterTest支持的name.id格式。
 *
 * @author wujinlong
 * @since 2021-8-1
 */
public class TemplateToData {
    private Map<DataProvider, String> providerLocators;

    public Map<String, Object> templateToData(Template tpl) {
        buildMapping(tpl);

        Map<String, Object> result = new HashMap<>();
        result.put("inputs", providersToData(tpl.getInputs()));
        result.put("outputs", recipientsToData(tpl.getOutputs()));
        result.put("calls", callsToData(tpl.getFormulas()));
        return result;
    }

    private void buildMapping(Template tpl) {
        providerLocators = new HashMap<>();
        for (DataProvider tplInput : tpl.getInputs().values()) {
            providerLocators.put(tplInput, String.format(Locale.ENGLISH,
                "input-%s", tplInput.getName()));
        }
        for (FormulaCall call : tpl.getFormulas()) {
            providerLocators.put(call.getOutput(), String.format(Locale.ENGLISH,
                "%s-%s", call.getFormulaName(), call.getResultName()));
        }
    }

    private Map<String, Object> providersToData(Map<String, DataProvider> providers) {
        if (providers == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, DataProvider> each : providers.entrySet()) {
            result.put(each.getKey(), null);
        }
        return result;
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

    private Map<String, Object> recipientToData(DataRecipient recipient) {
        if (recipient == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("constant", recipient.getConstant());
        for (DataPorter porter : recipient.getInList()) {
            result.put(porter.getDstPath().toString(true),
                String.format(Locale.ENGLISH, "%s-%s",
                providerLocators.get(porter.getProvider()), porter.getSrcPath().toString(true)));
        }
        return result;
    }

    private Map<String, Object> callsToData(List<FormulaCall> formulas) {
        if (formulas == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (FormulaCall call : formulas) {
            if (call != null) {
                // 公式名称应该是name:id格式，在一个模板内唯一
                result.put(call.getFormulaName(), callToData(call));
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
