package com.NWF.nettyWebFrame.tools.Serializer;

/**
 * description:序列化接口
 */
public interface Serializer {

    /**
     * description:将java对象转换为二进制字节数组
     *
     * @Param: 需要序列化为的对象
     * @return:序列化后的字节数组
     */
    byte[] serialize(Object object);

    /**
     * description:将二进制数组转换为java对象
     *
     * @Param1:需要反序列化的java类
     * @Param2:需要反序列化的字节数组
     * @return:反序列化后的java对象
     */
    <T> T deserialize(Class<T> cl, byte[] bytes);
}
