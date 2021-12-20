package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;

/**
 * description:websocket业务生成器，用于处理websocket帧的事务。websocket只能绑定一个业务作为核心业务。
 */
@Slf4j
public class WebsocketActionSpawn {
    public static String wsURL = "/NWF";//websocket连接的默认路径
    private WebSocketServerHandshaker handShaker;
    public WebsocketActionSpawn(WebSocketServerHandshaker handShaker){
        this.handShaker = handShaker;
    }

    private static Class<?>  requestAction = null;//需要被绑定的WebsocketAction业务类
    private WebsocketAction websocketAction = null;

    /**
     * description:绑定继承WebsocketAction的业务类和其对应的路径
     *
     * @Param1:继承WebsocketAction类的类
     * @Param1:需要绑定的websocket连接路径
     */
    public static final void bind(Class<?> requestAction,String url) {
        wsURL = url;
        WebsocketActionSpawn.requestAction = requestAction;
    }

    /**
     * description:绑定继承WebsocketAction的业务类，不指定url使用默认路径
     *
     * @Param:继承WebsocketAction类的类
     */
    public static final void bind(Class<?> requestAction) {
        WebsocketActionSpawn.requestAction = requestAction;
    }

    /**
     * description:返回调用绑定好的业务的处理结果
     *
     * @Param1:webscoket帧请求报文对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    public final ResponsePackage result(Object msg, ChannelHandlerContext ctx) throws IllegalAccessException, InstantiationException {
        Object obj = requestAction.newInstance();//反射出实例
        if(!(obj instanceof WebsocketAction))//如果不是WebsocketAction对象
        {
            log.info("Websocket业务生成器必须要绑定一个WebsocketAction类型!");
            return null;
        }
        websocketAction = (WebsocketAction)obj;
        websocketAction.setHandShaker(handShaker);
        return websocketAction.result(msg,ctx);
    }

}
