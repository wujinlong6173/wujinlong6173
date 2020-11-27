package wjl.datamodel;

import wjl.datamodel.schema.ObjectSchema;
import wjl.util.ErrorCollector;

import java.util.Map;

/**
 * 创建对象时校验输入参数是否正确。
 */
public class SchemaValidator {
    /**
     * 检查参数是否匹配数据字典
     *
     * @param objSchema 数据字典
     * @param inputs 参数
     * @return 错误信息
     */
    public static Map<String,String> checkObject(ObjectSchema objSchema, Map<String,Object> inputs) {
        if (objSchema == null) {
            return null;
        }
        ErrorCollector error = new ErrorCollector();
        objSchema.validate(inputs, error);
        return error.getErrors();
    }
}
