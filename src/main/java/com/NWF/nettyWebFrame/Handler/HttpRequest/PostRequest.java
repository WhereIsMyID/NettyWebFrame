package com.NWF.nettyWebFrame.Handler.HttpRequest;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

/**
 * description:处理POST请求
 */
public class PostRequest implements RequestHandler {

    /**
     * description:获取POST请求报文中的参数内容(目前只能处理json类型的参数)
     *
     * @Param:http请求报文
     * @return:报文内容
     */
    public Object handle(FullHttpRequest fullHttpRequest) {
        String contentType = getContentType(fullHttpRequest.headers());//获取传输的报文类型
        if(contentType.equals("application/json"))//判断是否是json类型的报文
        {
            return fullHttpRequest.content().toString(CharsetUtil.UTF_8);//返回json报文字符串
        }else
        {
            throw new IllegalArgumentException("目前只能接收 application/json 类型的报文！");
        }
    }

    /**
     * description: 通过header来获取post传输的报文类型
     *
     * @Param:http报文头字段
     * @return:该http报文Content字段的类型
     */
    private String getContentType(HttpHeaders headers)
    {
        String type = headers.get("Content-Type");//获取http头部的type信息
        String[] list = type.split(";");//截取第一个内容
        return list[0];
    }
}
