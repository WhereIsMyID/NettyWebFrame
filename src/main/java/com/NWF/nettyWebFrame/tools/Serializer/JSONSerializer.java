package com.NWF.nettyWebFrame.tools.Serializer;

import com.alibaba.fastjson.JSON;

/**
 * description:实现json的序列化工具，实现java对象与json的转换
 */
public class JSONSerializer implements Serializer{

    /**
     * description:将java对象转换为json字节数组
     *
     * @Param: 需要序列化为json报文的对象
     * @return:json报文的字节数组
     */
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    /**
     * description:将json字节数组转换为java对象
     *
     * @Param1:需要反序列化的java类
     * @Param2:json报文的字节数组
     * @return:反序列化后的java对象
     */
    public <T> T deserialize(Class<T> cl, byte[] bytes) {
        return JSON.parseObject(bytes,cl);
    }
}
