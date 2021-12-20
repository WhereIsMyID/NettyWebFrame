package com.NWF.nettyWebFrame.Handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * description:服务端Handler初始化类，用于初始化整个workerGroup中的EventLoop连接处理流水线
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch){
        //向管道加入处理器
        ChannelPipeline pipeline = ch.pipeline();

        //往管道中加入Netty提供的httpServerCodec编解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec())//加入Http编解码器
                .addLast("aggregator",new HttpObjectAggregator(1024*1024))//加入Http消息聚合器
                .addLast(new ChunkedWriteHandler())//加入块写入器
                .addLast("MyHttpServerHandler",new HttpServerHandler());//设置业务的Handler

    }
}
