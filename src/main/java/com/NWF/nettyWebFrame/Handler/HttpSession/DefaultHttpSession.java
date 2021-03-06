package com.NWF.nettyWebFrame.Handler.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * description:实现HttpSession接口,表示一个session对象
 */
public class DefaultHttpSession implements HttpSession {
    public static String SESSIONID = "NettyWebFrameSessionId";//客户端发送cookie的名称
    private String sessionId;//session的随机码
    //存放session内容的哈希表
    private Map<String,Object> attributes =  new HashMap<>();

    /**
     * description:初始化构造函数，每次创建对象，为该对象生成一个随机的sessionId
     */
    public DefaultHttpSession(){
        this.sessionId = UUID.randomUUID().toString();//随机生成一个sessionId
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name,value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    @Override
    public void invalidate() {
        this.sessionId = null;
    }
}
