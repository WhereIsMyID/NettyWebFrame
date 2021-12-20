package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.FlushResponse;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;


/**
 * description:处理异步的业务
 */
public class AsyncAction {
    public static int executors = Runtime.getRuntime().availableProcessors();//异步线程池线程数(默认与cpu核数相同)
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(executors);//异步线程池

    /**
     * description:通过异步线程池来执行FlushResponse中的set方法，实现一个请求业务的独立执行，不会阻塞NioEventGroup的后续队列。
     *
     * @Param1:http请求对象
     * @Param2:当前handler连接的channel上下文
     * @Param3:需要异步处理的业务对象
     */
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
