package com.NWF.nettyWebFrame.Handler;

import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//将ResponsePackage报文包拆分输出
@Slf4j
public class FlushResponse {
    public static void set(ResponsePackage responses, FullHttpRequest request,ChannelHandlerContext ctx) throws IOException {
        if(responses == null)
        {
            log.info("报文处理时发生错误!\n");
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

        //遍历所有应答报文,写入ctx中
        for (Object r : responses.getResponses())
        {
            channelFuture = ctx.write(r);//获取channelFuture
        }

        if(!keepAlive|| responses.getTag()!=ResponsePackage.KEEP_ALIVE)//如果没有心跳或者没有开启心跳机制
        {
            log.info(ctx.channel() + " 心跳关闭");
            channelFuture.addListener(ChannelFutureListener.CLOSE);//关闭连接
        }

        log.info("发送数据。\n");
        ctx.flush();//刷出消息
    }
}
