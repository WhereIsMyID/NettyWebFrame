package com.NWF.nettyWebFrame.Handler.HttpSession;

/**
 * description:定义Session对象接口
 */
public interface HttpSession
{
    /**
     * description:获取session中储存的对象
     *
     * @Param:关联对象储存的键
     * @return:键对应的值
     */
    Object getAttribute(String name);

    /**
     * description:添加新的关联的对象
     *
     * @Param:关联对象储存的键
     * @Param:关联的对象
     */
    void setAttribute(String name, Object value);

    /**
     * description:移除一个关联的对象
     *
     * @Param:关联对象储存的键
     */
    void removeAttribute(String name);

    /**
     * description:获取sessionId
     *
     * @return:该对象创建时随机生成的sessionId
     */
    String getId();

    /**
     * description:使该对象的sessionId无效化
     */
    void invalidate();

}
