package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.FlushResponse;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.tools.ResourcesTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.SneakyThrows;


//处理异步线程
public class AsyncAction {

    //静态资源请求
    public static void doAsync(FullHttpRequest msg, ChannelHandlerContext ctx,String uri,RequestAction action) {
        ctx.executor().execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                ResponsePackage result = null;
                if(ResourcesTools.isStaticFile(uri))//如果是存在的资源
                {
                    result = action.result(msg, ctx);//构建应答报文
                }
                FlushResponse.set(result,msg, ctx);//写出报文
            }
        });
    }

    //业务请求
    public static void doAsync(FullHttpRequest msg, ChannelHandlerContext ctx,RequestAction action) {
        ctx.executor().execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                ResponsePackage result;
                try {
                    result = action.result(msg, ctx);//构建应答报文
                }catch (Exception e)
                {
                    result = null;
                }

                FlushResponse.set(result,msg, ctx);//写出报文
            }
        });
    }

}
