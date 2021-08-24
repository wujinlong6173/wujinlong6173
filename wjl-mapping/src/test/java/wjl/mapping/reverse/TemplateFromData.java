package wjl.mapping.reverse;

import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.SimplePath;
import wjl.mapping.model.Template;
import wjl.mapping.parser.SimplePathParser;
import wjl.mapping.parser.StringParser;
import wjl.utils.ErrorCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TemplateFromData extends ErrorCollector {
    private static final String CALLS = "calls";
    private static final String CONSTANT = "constant";
    private static final String INPUTS = "inputs";
    private static final String OUTPUT = "output";
    private static final String OUTPUTS = "outputs";
    private static final String TEMPLATE_INPUT = "template_input";
    private static final String FORMULA_RESULT = "formula_result";

    private final FormulaRegister register;
    private final Map<String, FormulaCall> allCalls = new HashMap<>();
    private final SimplePathParser pathParser = new SimplePathParser();

    public TemplateFromData(FormulaRegister register) {
        this.register = register;
    }

    public Template templateFromData(Object data) {
        if (data == null) {
            return null;
        } else if (data instanceof Map) {
            Map<?,?> mapData = (Map<?,?>)data;
            List<String> inputNames = parseInputName(mapData);
            List<String> outputNames = parseOutputName(mapData);
            if (inputNames == null || outputNames == null) {
                return null;
            }

            Template tpl = new Template(inputNames, outputNames);
            parseCalls(tpl, mapData);
            parsePorters(tpl, mapData);
            allCalls.clear();
            return tpl;
        } else {
            reportError("template must be map.");
            return null;
        }
    }

    private List<String> parseInputName(Map<?, ?> rawTpl) {
        Object inputs = rawTpl.get(INPUTS);
        if (inputs instanceof List) {
            pushLocator(INPUTS);
            List<?> lstInputs = (List<?>)inputs;
            List<String> ret = new ArrayList<>(lstInputs.size());
            int idx = 0;
            for (Object each : lstInputs) {
                if (each instanceof String) {
                    ret.add((String)each);
                } else {
                    reportError(idx, "require string");
                }
                idx++;
            }
            popLocator();
            return ret;
        } else {
            reportError(INPUTS, "require list of string.");
            return null;
        }
    }

    private List<String> parseOutputName(Map<?,?> rawTpl) {
        Object outputs = rawTpl.get(OUTPUTS);
        if (outputs instanceof Map) {
            Map<?,?> mapOutputs = (Map<?,?>)outputs;
            List<String> ret = new ArrayList<>(mapOutputs.size());
            for (Object each : mapOutputs.keySet()) {
                ret.add((String)each);
            }
            return ret;
        } else {
            reportError(OUTPUTS, "require map.");
            return null;
        }
    }

    private void parseCalls(Template tpl, Map<?,?> rawTpl) {
        Object calls = rawTpl.get(CALLS);
        if (calls instanceof Map) {
            pushLocator(CALLS);
            Map<?,?> mapCalls = (Map<?,?>)calls;
            for (Map.Entry<?,?> each : mapCalls.entrySet()) {
                String callName = (String)each.getKey();
                pushLocator(callName);
                parseCall(tpl, callName, each.getValue());
                popLocator();
            }
            popLocator();
        } else if (calls != null) {
            reportError(CALLS, "require map of calls.");
        }
    }

    private void parseCall(Template tpl, String callName, Object rawCall) {
        if (rawCall instanceof Map) {
            Map<?,?> mapCall = (Map<?,?>)rawCall;
            String resultName = StringParser.parseRequired(this, OUTPUT, mapCall);
            if (resultName != null) {
                FormulaCall call = register.createCall(callName, resultName);
                tpl.addFormulaCall(call);
                allCalls.put(callName, call);
            }
        } else {
            reportError("require map for one call.");
        }
    }

    private void parsePorters(Template tpl, Map<?,?> rawTpl) {
        Object outputs = rawTpl.get(OUTPUTS);
        pushLocator(OUTPUTS);
        parserPorterOfParams(tpl, tpl.getOutputs(), outputs);
        popLocator();

        pushLocator(CALLS);
        parseCallPorters(tpl, rawTpl.get(CALLS));
        popLocator();
    }

    private void parseCallPorters(Template tpl, Object calls) {
        if (!(calls instanceof Map)) {
            return;
        }

        Map<?,?> mapCalls = (Map<?,?>)calls;
        for (Map.Entry<?,?> eachCall : mapCalls.entrySet()) {
            String callName = (String)eachCall.getKey();
            FormulaCall call = allCalls.get(callName);
            pushLocator(callName);
            parseCallPorters(tpl, call, eachCall.getValue());
            popLocator();
        }
    }

    private void parseCallPorters(Template tpl, FormulaCall call, Object rawCall) {
        if (!(rawCall instanceof Map)) {
            return;
        }

        Map<?,?> mapCall = (Map<?,?>)rawCall;
        Object inputs = mapCall.get(INPUTS);
        if (inputs instanceof Map) {
            pushLocator(INPUTS);
            Map<?,?> mapInputs = (Map<?,?>)inputs;
            for (Map.Entry<?,?> eachInput : mapInputs.entrySet()) {
                String inputName = (String)eachInput.getKey();
                DataRecipient data = call.getInput(inputName);
                if (data == null) {
                    reportError(inputName, "invalid input name.");
                    continue;
                }
                parsePorterOfParam(tpl, data, eachInput.getValue());
            }
            popLocator(); // INPUTS
        } else if (inputs != null) {
            reportError(INPUTS, String.format(Locale.ENGLISH,
                "require map, but is %s.", inputs.getClass().getName()));
        }
    }

    private void parserPorterOfParams(Template tpl, Map<String, DataRecipient> dataMap, Object params) {
        if (params instanceof Map) {
            Map<?,?> mapParams = (Map<?,?>)params;
            for (Map.Entry<?,?> each : mapParams.entrySet()) {
                String paramName = (String)each.getKey();
                DataRecipient data = dataMap.get(paramName);
                if (data == null) {
                    reportError(paramName, "invalid parameter.");
                    continue;
                }
                pushLocator(paramName);
                parsePorterOfParam(tpl, data, each.getValue());
                popLocator();
            }
        } else if (params != null) {
            reportError("require map of parameters.");
        }
    }

    private void parsePorterOfParam(Template tpl, DataRecipient data, Object param) {
        FakeFunction fake = parseFakeFunction(param);
        if (fake != null) {
            DataProvider provider;
            if (fake.getCallName() != null) {
                FormulaCall call = allCalls.get(fake.getCallName());
                if (call == null) {
                    reportError(String.format(Locale.ENGLISH,
                        "invalidate call %s.", fake.getCallName()));
                    return;
                }
                provider = call.getOutput();
            } else {
                provider = tpl.getInput(fake.getInputName());
                if (provider == null) {
                    reportError(String.format(Locale.ENGLISH,
                        "invalidate input name %s.", fake.getInputName()));
                    return;
                }
            }

            SimplePath srcPath = pathParser.parseNoBrace(fake.getPath());
            if (pathParser.getError() != null) {
                reportError(String.format(Locale.ENGLISH,
                    "path %s error : %s.", fake.getPath(), pathParser.getError()));
                return;
            }

            tpl.addDataPorter(provider, srcPath, data, SimplePath.EMPTY);
            return;
        }

        if (param instanceof Map) {
            Map<?,?> mapParam = (Map<?,?>)param;
            for (Map.Entry<?,?> each : mapParam.entrySet()) {
                if (CONSTANT.equals(each.getKey())) {
                    data.setConstant(each.getValue());
                    continue;
                }
                String key = (String)each.getKey();
                SimplePath dstPath = key.startsWith("${")
                    ? pathParser.parseWithBrace(key)
                    : pathParser.parseNoBrace(key);
                if (pathParser.getError() != null) {
                    reportError(key, pathParser.getError());
                    continue;
                }

                pushLocator(key);
                fake = parseFakeFunction(each.getValue());
                popLocator();
                if (fake == null) {
                    continue;
                }

                DataProvider provider;
                if (fake.getCallName() != null) {
                    FormulaCall call = allCalls.get(fake.getCallName());
                    if (call == null) {
                        reportError(key, String.format(Locale.ENGLISH,
                            "invalidate call %s.", fake.getCallName()));
                        continue;
                    }
                    provider = call.getOutput();
                } else {
                    provider = tpl.getInput(fake.getInputName());
                    if (provider == null) {
                        reportError(key, String.format(Locale.ENGLISH,
                            "invalidate input name %s.", fake.getInputName()));
                        continue;
                    }
                }

                SimplePath srcPath = pathParser.parseNoBrace(fake.getPath());
                if (pathParser.getError() != null) {
                    reportError(key, String.format(Locale.ENGLISH,
                        "path %s error : %s.", fake.getPath(), pathParser.getError()));
                    continue;
                }

                tpl.addDataPorter(provider, srcPath, data, dstPath);
            }
        } else if (param != null) {
            data.setConstant(param);
        }
    }

    private FakeFunction parseFakeFunction(Object func) {
        if (!(func instanceof Map)) {
            return null;
        }

        Map<?,?> mapFunc = (Map<?,?>)func;
        if (mapFunc.size() != 1) {
            return null;
        }

        Object value = mapFunc.get(FORMULA_RESULT);
        if (value != null) {
            pushLocator(FORMULA_RESULT);
            FakeFunction fake = parseFormulaResult(value);
            popLocator();
            return fake;
        }

        value = mapFunc.get(TEMPLATE_INPUT);
        if (value != null) {
            pushLocator(TEMPLATE_INPUT);
            FakeFunction fake = parseTemplateInput(value);
            popLocator();
            return fake;
        }

        return null;
    }

    private FakeFunction parseFormulaResult(Object args) {
        List<String> lstArgs = StringParser.parseList(this, args);
        if (lstArgs == null) {
            return null;
        }

        if (lstArgs.size() == 1) {
            FakeFunction fake = new FakeFunction();
            fake.setCallName(lstArgs.get(0));
            return fake;
        }

        if (lstArgs.size() == 2) {
            FakeFunction fake = new FakeFunction();
            fake.setCallName(lstArgs.get(0));
            fake.setPath(lstArgs.get(1));
            return fake;
        }

        reportError("require one or two arguments.");
        return null;
    }

    private FakeFunction parseTemplateInput(Object args) {
        List<String> lstArgs = StringParser.parseList(this, args);
        if (lstArgs == null) {
            return null;
        }

        if (lstArgs.size() == 2) {
            FakeFunction fake = new FakeFunction();
            fake.setInputName(lstArgs.get(0));
            fake.setPath(lstArgs.get(1));
            return fake;
        }

        reportError("require two arguments.");
        return null;
    }

}
