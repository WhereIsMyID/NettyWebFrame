package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * description:业务处理对象父类，所有的业务实现都需要继承该类，并通过bind方法绑定访问路径
 */
@Slf4j
public abstract class RequestAction {
    private boolean isSync = true;//设定是否同步执行.默认为同步
    private String url;//业务的url
    private ResponsePackage response;//封装的应答报文包

    /**
     * description:查看当前的业务是否需要同步处理
     *
     * @return:true为需要同步，false为需要异步处理
     */
    public boolean isSync()
    {
        return isSync;
    }

    /**
     * description:为当前业务绑定url到业务工厂RequestActionFactory中，启动服务器后浏览器即可通过绑定的url访问该业务
     *
     * @Param:需要绑定的url
     * @return:返回当前对象
     */
    public final RequestAction bind(String url)
    {
        this.url= url;
        RequestActionFactory.add(this.url,this);//在业务处理工厂中添加当前对象和url的绑定关系
        return this;
    }

    /**
     * description:绑定路径，并设定是否同步
     *
     * @Param1:需要绑定的url
     * @Param2:true为同步，false为异步
     * @return:返回当前对象
     */
    public final RequestAction bind(String url,boolean isSync)
    {
        this.isSync = isSync;
        this.url= url;
        RequestActionFactory.add(this.url,this);
        return this;
    }

    /**
     * description:需要使用者自定义对接收的参数进行操作的抽象方法。本业务处理类的核心。
     *
     * @Param1:http请求报文对象
     * @Param2:当前handler连接的channel上下文
     * @return:用户处理后生成的应答报文包
     */
    abstract public ResponsePackage action(Object msg, ChannelHandlerContext ctx);

    /**
     * description:提供给RequestFactory工厂用于调用action方法，返回处理结果
     *
     * @Param1:http请求报文对象
     * @Param2:当前handler连接的channel上下文
     * @return:执行action处理后生成的应答报文包
     */
    public final ResponsePackage result(Object msg, ChannelHandlerContext ctx){
        response = action(msg,ctx);
        return response;
    }

}
