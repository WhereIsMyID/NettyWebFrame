package com.NWF.nettyWebFrame.Handler.HttpRequest;

import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * description:RequestHandler工厂，用于创建不同类型的请求对象
 */
public class RequestHandlerFactory {

    public static final Map<HttpMethod,RequestHandler> REQUEST_HANDLERS = new HashMap<HttpMethod, RequestHandler>();//将http方法与其请求类型进行映射储存到HashMap中

    static {
        REQUEST_HANDLERS.put(HttpMethod.GET,new GetRequest());//映射GET请求
        REQUEST_HANDLERS.put(HttpMethod.POST,new PostRequest());//映射POST请求
    }

    /**
     * description:用于获取对应请求类型的Handler
     *
     * @Param: http请求的方法(目前只能处理GET、POST)
     * @return: 返回针对不同请求类型所创建的RequestHandler对象
     */
    public static RequestHandler create(HttpMethod httpMethod)
    {
        return REQUEST_HANDLERS.get(httpMethod);
    }
}
