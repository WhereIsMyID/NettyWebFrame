package com.NWF.nettyWebFrame.Handler;

import com.NWF.nettyWebFrame.tools.RequestAction.RequestActionFactory;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private FullHttpRequest request;
    private ResponsePackage responses;

    @Override
    //处理http请求
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest)//如果当前是Http请求
        {
            this.request = (FullHttpRequest) msg;
            System.out.println("\n");
            log.info(ctx.channel() + "\n========================================================================收到http请求========================================================================\n"
                    + msg +
                    "\n============================================================================================================================================================");
            String uri = request.uri();//获取请求参数
            if(uri.equals("/favicon.ico")) return;//过滤掉"/favicon.ico"的请求
            try {
                //回应的内容
                responses =  (ResponsePackage)RequestActionFactory.get(uri,request,ctx);
            }catch (Exception e)
            {
                log.info("报文处理时发生错误：\n"+e);
                ResponseTools.notFound(ctx,request);//返回一个404页面
                return;
            }

            ChannelFuture channelFuture = null;
            HttpResponse firstResponse = (HttpResponse) responses.getResponses().get(0);//获取第一个应答报文

            boolean keepAlive = HttpUtil.isKeepAlive(request);//判断是否有心跳

            if(keepAlive && responses.getTag()==ResponsePackage.KEEP_ALIVE)//如果有心跳,且开启了心跳机制
            {
                log.info(ctx.channel() + " 保持心跳");
                firstResponse.headers().set(HttpHeaderNames.CONNECTION,"keep-alive");//往报文头部中添加保持心跳选项
            }

            log.info("发送数据。");
            //遍历所有应答报文,写入ctx中
            for (Object r : responses.getResponses())
            {
                channelFuture = ctx.write(r);//获取channelFuture
            }

            if(!keepAlive || responses.getTag()!=ResponsePackage.KEEP_ALIVE)//如果没有心跳或者没有开启心跳机制
            {
                log.info(ctx.channel() + " 心跳关闭");
                channelFuture.addListener(ChannelFutureListener.CLOSE);//回复客户端，并关闭连接
            }

            ctx.flush();
        }
    }


    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
