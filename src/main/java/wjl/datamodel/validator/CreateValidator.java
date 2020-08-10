package wjl.datamodel.validator;

import wjl.datamodel.schema.ObjectSchema;
import wjl.util.ErrorCollector;

import java.util.Map;

/**
 * 创建对象时校验输入参数是否正确。
 */
public class CreateValidator {
    /**
     * 检查参数是否匹配数据字典
     *
     * @param objSchema 数据字典
     * @param inputs 参数
     * @param error 收集错误信息
     */
    public static void checkObject(ObjectSchema objSchema, Map<String,Object> inputs, ErrorCollector error) {
    }
}
