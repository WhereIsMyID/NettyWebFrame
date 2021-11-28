package com.NWF.nettyWebFrame.Handler.HttpSession;

//定义Session对象接口
public interface HttpSession
{
    String SESSIONID = "NettyWebFrameSessionId" ;
    Object getAttribute(String name);//获取session中储存的对象
    void setAttribute(String name, Object value);//添加新的关联的对象
    void removeAttribute(String name);//移除一个关联的对象
    String getId();//获取sessionId
    void invalidate();//使sessionId无效化
}
