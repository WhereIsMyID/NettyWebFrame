package com.NWF.nettyWebFrame.tools.Serializer;

import com.alibaba.fastjson.JSON;

//实现json的序列化
public class JSONSerializer implements Serializer{
    //实现java对象与json的转换

    //将java对象转换为json字节数组
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    //将json字节数组转换为java对象
    public <T> T deserialize(Class<T> cl, byte[] bytes) {
        return JSON.parseObject(bytes,cl);
    }
}
