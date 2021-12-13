package com.NWF.nettyWebFrame.tools;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
//应答报文的工具类
public class ResponseTools {
    //私有化构造方法
    private ResponseTools(){};

    //构造http基本应答报文
    public static ResponsePackage Response(byte[] bytes, HttpRequest msg, String contextType,int tag)
    {
        List<Object> responses = new ArrayList<>();//应答报文列表

        log.info("构建" +contextType +"报文");
        FullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(),HttpResponseStatus.OK,Unpooled.copiedBuffer(bytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,contextType);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,bytes.length);

        responses.add(response);

        ResponsePackage responsePackage = new ResponsePackage(tag,responses);//创建一个报文应答包
        return responsePackage;
    }

    //静态资源报文
    public static ResponsePackage handleResource(FullHttpRequest msg, String resource)
    {
        return ResourcesTools.handleResource(msg,resource);
    }

}
