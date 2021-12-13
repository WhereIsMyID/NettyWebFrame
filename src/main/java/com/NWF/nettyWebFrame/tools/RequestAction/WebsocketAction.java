package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;

//websocket的业务处理类
public class WebsocketAction extends RequestAction {
    protected ResponsePackage response = null;//封装的应答报文包
    protected WebSocketServerHandshaker handShaker = null;//当前连接的handShaker

    //初始化
    public void setHandShaker(WebSocketServerHandshaker handShaker)
    {
        this.handShaker = handShaker;
    }

    //关闭链路
    protected ResponsePackage isCloseWebSocketFrame(CloseWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        if(handShaker != null)
        handShaker.close(ctx.channel(),frame.retain());//关闭链路
        return null;
    }

    //Ping消息
    protected ResponsePackage isPingWebSocketFrame(PingWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));//回复一个应答报文
        return null;
    }

    //二进制消息
    protected ResponsePackage isBinaryWebSocketFrame(BinaryWebSocketFrame frame,ChannelHandlerContext ctx) { return null; }

    //文本消息
    protected ResponsePackage isTextWebSocketFrame(TextWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        return null;
    }


    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx) {
        WebSocketFrame frame = (WebSocketFrame)msg;

        //判断是否是关闭链路
        if (frame instanceof CloseWebSocketFrame) {
            response = isCloseWebSocketFrame((CloseWebSocketFrame) frame,ctx);
        }
        //判断是否是ping消息
        else if (frame instanceof PingWebSocketFrame) {
            response = isPingWebSocketFrame((PingWebSocketFrame) frame,ctx);
        }
        //判断是否是二进制消息
        else if (frame instanceof BinaryWebSocketFrame) {
            response = isBinaryWebSocketFrame((BinaryWebSocketFrame) frame,ctx);
        }
        //判断是否是文本消息
        else if(frame instanceof TextWebSocketFrame){
            response = isTextWebSocketFrame((TextWebSocketFrame)frame,ctx);
        }

        return response;
    }
}
