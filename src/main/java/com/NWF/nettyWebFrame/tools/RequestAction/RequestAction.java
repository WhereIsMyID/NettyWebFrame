package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
//业务处理对象父类
public abstract class RequestAction {
    private boolean isSync = true;//设定是否同步执行
    private String url;//业务的url
    private ResponsePackage response;//封装的应答报文包

    //查看是否同步
    public boolean isSync()
    {
        return isSync;
    }

    //绑定路径
    public final RequestAction bind(String url)
    {
        this.url= url;
        RequestActionFactory.add(this.url,this);//在业务处理工厂中添加当前对象和url的绑定关系
        return this;
    }

    //绑定路径，并设定是否同步
    public final RequestAction bind(String url,boolean isSync)
    {
        this.isSync = isSync;
        this.url= url;
        RequestActionFactory.add(this.url,this);
        return this;
    }

    //自定义对接收的参数的操作
    abstract public ResponsePackage action(Object msg, ChannelHandlerContext ctx);

    //返回处理结果
    public final ResponsePackage result(Object msg, ChannelHandlerContext ctx)
    {
        response = action(msg,ctx);
        return response;
    }

}
