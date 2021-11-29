package com.NWF.nettyWebFrame.service.Simple;

import com.NWF.nettyWebFrame.Handler.HttpRequest.RequestHandler;
import com.NWF.nettyWebFrame.Handler.HttpRequest.RequestHandlerFactory;
import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.basic.Item;
import com.NWF.nettyWebFrame.tools.Serializer.JSONSerializer;
import com.NWF.nettyWebFrame.tools.RequestAction.RequestAction;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import com.alibaba.fastjson.JSONException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

//测试用业务，检测参数的收发
@Slf4j
public class TestAction extends RequestAction {
    private JSONSerializer jsonSerializer = new JSONSerializer();//json序列化工具

    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx) {
        FullHttpRequest request = (FullHttpRequest)msg;
        RequestHandler requestHandler = RequestHandlerFactory.create(request.method());//根据当前http请求获取其对应的处理方法(GET/POST)
        Object result;//返回的对象
        result = requestHandler.handle(request);//获取请求的参数内容
        log.info("收到的参数为："+ result);
        byte[] responseBytes;
        //回应的内容
        try {
            Item item = jsonSerializer.deserialize(Item.class,result.toString().getBytes());//将json字节转换为java对象
            log.info("转化的对象： "+item);
            responseBytes = jsonSerializer.serialize(item);//将java对象转换为json字节
        }catch (JSONException e)
        {
            //如果不是json报文，就返回原报文
            responseBytes = result.toString().getBytes(CharsetUtil.UTF_8);
        }
        //构建json报文
        return ResponseTools.Response(responseBytes, request,"application/json",ResponsePackage.KEEP_ALIVE);
    }

}
