package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

/**
 * 产品的概念：设备、链路、虚拟设备、虚拟链路都属于产品。
 * 供应商的概念：又称为运营商，出售产品的公司，例如中国移动、中国电信。
 * 本接口只提供创建、删除一种产品的能力。
 */
public interface ProductProvider {
    /**
     * 获取提供商的名称，又称为运营商
     *
     * @return 提供商的名称
     */
    String getProviderName();

    /**
     * 获取产品的名称
     *
     * @return 产品的名称
     */
    String getProductName();

    /**
     * 创建产品的输入参数的模型
     *
     * @return 参数模型
     */
    ObjectSchema getCreateSchema();
}
