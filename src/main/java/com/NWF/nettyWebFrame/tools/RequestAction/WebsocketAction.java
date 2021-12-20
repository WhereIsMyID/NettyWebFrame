package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;

//
/**
 * description:websocket的业务处理类，使用者继承该类实现对不同websocket帧的类型进行的操作。
 */
public class WebsocketAction extends RequestAction {
    protected ResponsePackage response = null;//封装的应答报文包
    protected WebSocketServerHandshaker handShaker = null;//当前连接的handShaker

    /**
     * description:绑定websocket当前连接的handShaker，用于确定连接对象(该方法在WebsocketActionSpawn绑定中处理，不需要使用者重写)
     *
     * @Param:websocket连接和关闭的handshakes基本对象
     */
    public void setHandShaker(WebSocketServerHandshaker handShaker)
    {
        this.handShaker = handShaker;
    }

    /**
     * description:websocket帧的关闭链路请求
     *
     * @Param1:websocket关闭请求帧对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    protected ResponsePackage isCloseWebSocketFrame(CloseWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        if(handShaker != null)
        handShaker.close(ctx.channel(),frame.retain());//关闭链路
        return null;
    }

    /**
     * description:websocket帧的ping请求
     *
     * @Param1:websocket帧的ping请求对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    protected ResponsePackage isPingWebSocketFrame(PingWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));//回复一个应答报文
        return null;
    }

    /**
     * description:websocket帧的字节信息请求
     *
     * @Param1:websocket字节信息帧对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    protected ResponsePackage isBinaryWebSocketFrame(BinaryWebSocketFrame frame,ChannelHandlerContext ctx) { return null; }

    /**
     * description:websocket帧的文本信息请求
     *
     * @Param1:websocket文本信息帧对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    protected ResponsePackage isTextWebSocketFrame(TextWebSocketFrame frame,ChannelHandlerContext ctx)
    {
        return null;
    }

    /**
     * description:根据不同的websocket帧请求作出不同的处理
     *
     * @Param1:webscoket帧请求报文对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
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
