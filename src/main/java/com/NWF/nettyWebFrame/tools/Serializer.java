package com.NWF.nettyWebFrame.tools;

//序列化接口
public interface Serializer {
    byte[] serialize(Object object);//将java对象转换为二进制数组

    <T> T deserialize(Class<T> cl, byte[] bytes);//将二进制数组转换为java对象
}
