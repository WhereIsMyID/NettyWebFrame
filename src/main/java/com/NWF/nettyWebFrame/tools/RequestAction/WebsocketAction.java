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
    protected void isCloseWebSocketFrame(CloseWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        if(handShaker != null)
        handShaker.close(ctx.channel(),frame.retain());//关闭链路
    }

    //Ping消息
    protected void isPingWebSocketFrame(PingWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));//回复一个应答报文
    }

    //二进制消息
    protected void isBinaryWebSocketFrame(BinaryWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        return;
    }

    //文本消息
    protected void isTextWebSocketFrame(TextWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        return;
    }


    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx) {
        WebSocketFrame frame = (WebSocketFrame)msg;

        //判断是否是关闭链路
        if (frame instanceof CloseWebSocketFrame) {
            isCloseWebSocketFrame((CloseWebSocketFrame) frame,ctx);
        }
        //判断是否是ping消息
        else if (frame instanceof PingWebSocketFrame) {
            isPingWebSocketFrame((PingWebSocketFrame) frame,ctx);
        }
        //判断是否是二进制消息
        else if (frame instanceof BinaryWebSocketFrame) {
            isBinaryWebSocketFrame((BinaryWebSocketFrame) frame,ctx);
        }
        //判断是否是文本消息
        else if(frame instanceof TextWebSocketFrame){
            isTextWebSocketFrame((TextWebSocketFrame)frame,ctx);
        }

        return response;
    }
}
