package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.FlushResponse;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;


//处理异步线程
public class AsyncAction {
    public static int executors = 16;//异步线程池线程数
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(executors);//异步线程池

    //业务请求
    public static void doAsync(FullHttpRequest msg, ChannelHandlerContext ctx,RequestAction action) {
        group.submit(new Callable<Object>(){

            @Override
            public Object call() throws Exception {
                ResponsePackage result;
                try {
                    result = action.result(msg, ctx);//构建应答报文
                }catch (Exception e)
                {
                    result = null;
                }

                FlushResponse.set(result,msg, ctx);//写出报文
                return null;
            }
        });
    }

}
