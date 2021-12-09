package com.NWF.nettyWebFrame.Handler;

import com.NWF.nettyWebFrame.StartBoot;
import com.NWF.nettyWebFrame.tools.RequestAction.RequestActionFactory;
import com.NWF.nettyWebFrame.tools.RequestAction.WebsocketActionSpawn;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;


@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private FullHttpRequest request;//请求的报文
    private WebSocketServerHandshaker handShaker;//websocket基础类
    private WebsocketActionSpawn websocketActionSpawn;//websocket业务生成器
    private ResponsePackage responses;//封装应答报文的数据结构

    @Override
    //处理http请求
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest)//如果当前是Http请求
        {
            this.request = (FullHttpRequest) msg;
            if(StartBoot.logInfo)
            {
                log.info(ctx.channel() + "\n========================================================================收到http请求========================================================================\n"
                        + msg +
                        "\n============================================================================================================================================================");
            }

            String uri = request.uri();//获取请求参数
            if(uri.equals("/favicon.ico")) return;//过滤掉"/favicon.ico"的请求

            //回应的内容
            if(StartBoot.websocket && request.headers().get("Upgrade")!=null)//如果当前是开启websocket机制并且当前是websocket升级报文
                {
                    if(request.headers().get("Upgrade").equals("websocket")) {
                        webSocketUpgrade(ctx,uri);//升级为websocket协议
                        return;
                    }
                }else RequestActionFactory.get(uri,request,ctx);//通过应答报文工厂获取uri对应的执行方法的结果

        }
        else if(msg instanceof WebSocketFrame)//如果当前是websocket帧
        {
            try {
                responses = websocketActionSpawn.result(msg,ctx);//交给处理websocket的事务
                if(responses == null) return;
            }catch (Exception e) {
                return;
            }
            log.info("websocket连接发送数据。\n");
            ctx.writeAndFlush(responses.getResponses().get(0));//写入并刷出消息
        }

    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //升级websocket
    private void webSocketUpgrade(ChannelHandlerContext ctx,String url)
    {
        //创建一个websocket工厂
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WebsocketActionSpawn.wsURL, null, false);
        log.info(ctx.channel() + "发起websocket连接： " +url);
        handShaker = wsFactory.newHandshaker(request);//构建应答报文

        if (handShaker == null) WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        else handShaker.handshake(ctx.channel(),request);

        websocketActionSpawn = new WebsocketActionSpawn(handShaker);//初始化业务生成器
    }



}
