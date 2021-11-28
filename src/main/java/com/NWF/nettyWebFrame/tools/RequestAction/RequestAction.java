package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
//业务处理对象父类
public abstract class RequestAction {

    private String url;//业务的url
    public ResponsePackage response;//封装的应答报文包

    //绑定路径
    public final RequestAction bind(String url)
    {
        this.url= url;
        RequestActionFactory.add(url,this);//在业务处理工厂中添加当前对象和url的绑定关系
        return this;
    }

    //自定义对接收的参数的操作
    abstract public ResponsePackage action(FullHttpRequest msg, ChannelHandlerContext ctx);

    //返回处理结果
    public final ResponsePackage result(FullHttpRequest msg, ChannelHandlerContext ctx)
    {
        response = action(msg,ctx);
        return response;
    }


}
