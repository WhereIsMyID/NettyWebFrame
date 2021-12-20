package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.FlushResponse;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * description:请求处理工厂
 */
@Slf4j
public class RequestActionFactory {
    //私有化构造方法
    private RequestActionFactory() { }

    //存放所有的请求的处理方法
    private static Map<String, RequestAction> maps = new HashMap<>();

    /**
     * description:添加uri与其对应的处理方法至maps
     *
     * @Param1:绑定的url
     * @Param2:绑定的RequestAction业务对象
     */
    public static void add(String uri,RequestAction requestAction)
    {
        maps.put(uri,requestAction);
    }

    /**
     * description:根据http请求中的uri获取其对应的处理结果，并执行报文应答。
     * 首先进行get请求的判断，分离get请求的路径和参数，若不是get请求则进行静态资源请求的判断，若是静态资源请求则调用GetStaticFile业务异步执行。
     * 否则既是post请求，将uri从maps中找到对应业务，判断是否需要异步执行，若需要则将其通过AsyncAction的doAsync方法执行，反之直接执行其result方法。
     * 最后将结果通过FlushResponse.set方法发送给客户端。
     *
     * @Param1:http请求报文对象
     * @Param2:当前handler连接的channel上下文
     */
    public static void get(FullHttpRequest msg, ChannelHandlerContext ctx) throws IOException, InterruptedException {
        String uri = msg.uri();
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
