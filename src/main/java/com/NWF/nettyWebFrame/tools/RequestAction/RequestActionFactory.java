package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.tools.ResourcesTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
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
    public static Object get(String uri, FullHttpRequest msg, ChannelHandlerContext ctx)
    {
        log.info("收到请求："+uri);
        if(uri.indexOf("?") != -1)//如果是带有"?"的GET请求
        {
            uri = uri.substring(0,uri.indexOf("?"));//截取出前面的路径部分
        }
        else//否则，是一个post请求或者一个静态资源请求
        {
            if(!maps.containsKey(uri))//如果该uri不是记录在maps中绑定的话
            {
                if(ResourcesTools.isStaticFile(uri))//如果请求的是一个静态的地址
                {
                    uri = "/";//访问根目录
                }
            }
        }
        log.info("解析到路径："+uri);
        try {
            return maps.get(uri).result(msg,ctx);
        }catch (Exception e)
        {
            return e;
        }
    }
}
