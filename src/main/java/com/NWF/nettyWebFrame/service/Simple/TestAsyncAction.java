package com.NWF.nettyWebFrame.service.Simple;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.basic.Item;
import com.NWF.nettyWebFrame.tools.RequestAction.RequestAction;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import com.NWF.nettyWebFrame.tools.Serializer.JSONSerializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

//测试用业务，用于进行异步处理
@Slf4j
public class TestAsyncAction extends RequestAction {
    private JSONSerializer jsonSerializer = new JSONSerializer();//json序列化工具

    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx){
        FullHttpRequest request = (FullHttpRequest)msg;//将报文转换为完整HTTP报文对象
        log.info("异步执行，睡眠5秒...");
        try {
            Thread.sleep(5000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        byte[] responseBytes;
        //回应的内容
        Item item = new Item();
        item.setMes("我是5秒后的异步信息！");
        log.info("5秒结束，发送信息"+item);
        responseBytes = jsonSerializer.serialize(item);//将java对象转换为json字节

        //构建json报文
        return ResponseTools.Response(responseBytes, request,"application/json",ResponsePackage.KEEP_ALIVE);
    }
}
