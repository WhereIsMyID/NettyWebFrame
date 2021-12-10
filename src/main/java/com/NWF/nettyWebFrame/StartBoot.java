package com.NWF.nettyWebFrame;

import com.NWF.nettyWebFrame.Handler.HttpServerInitializer;
import com.NWF.nettyWebFrame.tools.RequestAction.GetStaticFile;
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
    private static int port = 8080;//端口
    public static boolean logInfo = true;//是否开启报文日志
    public static boolean websocket = false;//websocket开关

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

    //设置是否打开日志输出
    public StartBoot openLog(boolean b)
    {
        logInfo = b;
        return this;
    }

    public void run(int p)
    {
        StartBoot.port = p;
        new GetStaticFile().bind("/");//设置静态资源根目录

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//将boss线程设为1条
        EventLoopGroup workerGroup = new NioEventLoopGroup();//工作线程池

        try{
            ServerBootstrap serverbootstrap = new ServerBootstrap();

            serverbootstrap.group(bossGroup,workerGroup)//设置boss线程和worker线程池
                    .channel(NioServerSocketChannel.class)//指定channel类型(基于TCP的客户端连接)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//开启TCP心跳检测
                    .childHandler(new HttpServerInitializer());//绑定定义好的Initializer

            //判断是否开启报文日志
            if(logInfo) serverbootstrap.handler(new LoggingHandler(LogLevel.INFO));

            ChannelFuture channelFuture = serverbootstrap.bind(port).sync();//绑定端口
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
