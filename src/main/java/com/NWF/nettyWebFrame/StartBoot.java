package com.NWF.nettyWebFrame;

import com.NWF.nettyWebFrame.Handler.HttpServerInitializer;
import com.NWF.nettyWebFrame.service.ActionInitialization;
import com.NWF.nettyWebFrame.tools.ResourcesTools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartBoot {
    public static int port = 8080;
    public static boolean websocket = false;//websocket开关
    public static String wsURL = "NWF";//websocket连接的默认路径

    //设置静态资源路径
    public StartBoot setStaticPath(String path)
    {
        ResourcesTools.location = path;
        return this;
    }

    //设置是否开启websocket机制
    public StartBoot webSocket(boolean b)
    {
        websocket = b;
        return this;
    }

    //设置websocket的url
    public StartBoot wsURL(String url)
    {
        wsURL = url;
        return this;
    }

    public void run(int p)
    {
        StartBoot.port = p;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//将boss线程设为1条
        EventLoopGroup workerGroup = new NioEventLoopGroup();//工作线程

        try{
            ServerBootstrap serverbootstrap = new ServerBootstrap();

            serverbootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//开启TCP心跳检测
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer());//绑定定义好的Initializer

            ChannelFuture channelFuture = serverbootstrap.bind(port).sync();
            log.info("服务器已开启...");
            //同步处理通道关闭监听线程
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            log.error("服务器出错："+e);
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
