package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//用于处理websocket帧的事务
public class WebsocketActionSpawn {
    public static String wsURL = "NWF";//websocket连接的默认路径
    private WebSocketServerHandshaker handShaker;
    public WebsocketActionSpawn(WebSocketServerHandshaker handShaker){
        this.handShaker = handShaker;
    }

    private static Class<?>  requestAction = null;//需要被绑定的WebsocketAction业务类
    private WebsocketAction websocketAction = null;

    //绑定业务类
    public static final void bind(Class<?> requestAction,String url) {
        wsURL = url;
        WebsocketActionSpawn.requestAction = requestAction;
    }

    //不指定url使用默认路径
    public static final void bind(Class<?> requestAction) {
        WebsocketActionSpawn.requestAction = requestAction;
    }

    //返回处理结果
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
