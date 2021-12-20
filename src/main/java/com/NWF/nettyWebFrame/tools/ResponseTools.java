package com.NWF.nettyWebFrame.tools;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * description:应答报文的工具类
 */
@Slf4j
public class ResponseTools {
    //私有化构造方法
    private ResponseTools(){};

    /**
     * description:构造http基本应答报文
     *
     * @Param1:需要回复的字节数组
     * @Param2:http请求报文对象
     * @Param3:http应答报文的类型
     * @Param4:应答报文包的处理类型
     * @return:
     */
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

    /**
     * description:静态资源报文
     *
     * @Param1:http请求报文对象
     * @Param2:资源路径
     * @return:封装页面文件的应答报文包
     */
    public static ResponsePackage handleResource(FullHttpRequest msg, String resource)
    {
        return ResourcesTools.handleResource(msg,resource);
    }

}
