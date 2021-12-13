package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.FlushResponse;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//请求处理工厂
@Slf4j
public class RequestActionFactory {
    //私有化构造方法
    private RequestActionFactory() { }

    //存放所有的请求的处理方法
    private static Map<String, RequestAction> maps = new HashMap<>();

    //添加uri与其处理方法
    public static void add(String uri,RequestAction requestAction)
    {
        maps.put(uri,requestAction);
    }

    //根据uri获取其处理结果
    public static void get(String uri, FullHttpRequest msg, ChannelHandlerContext ctx) throws IOException, InterruptedException {
        log.info("收到请求："+uri);
        if(uri.indexOf("?") != -1)//如果是带有"?"的GET请求
        {
            uri = uri.substring(0,uri.indexOf("?"));//截取出前面的路径部分
        }
        else//否则，是一个post请求或者一个静态资源请求
        {
            if(!maps.containsKey(uri) || uri.equals("/"))//如果该uri不是记录在maps中绑定的话
            {
                //如果请求的是一个静态的地址，此处产生io需异步执行处理
                AsyncAction.doAsync(msg,ctx,maps.get("/"));
                return;
            }
        }

        log.info("解析到路径："+uri);
        RequestAction action;
        ResponsePackage response;
        try{
            action = maps.get(uri);
            if(!action.isSync())//判断该业务是否需要异步执行
            {
                AsyncAction.doAsync(msg,ctx,action);//异步执行
                return;
            }
            response = action.result(msg, ctx);
        }catch (Exception e)
        {
            response = null;
        }
        FlushResponse.set(response,msg,ctx);//同步执行
    }
}
