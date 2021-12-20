package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.tools.ResourcesTools;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * description:静态资源获取
 */
public class GetStaticFile extends RequestAction {

    /**
     * description:接收一个http请求，根据其访问的路径返回静态资源文件
     *
     * @Param1:http请求报文对象
     * @Param2:当前handler连接的channel上下文
     * @return:封装了资源文件的应答报文包
     */
    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx) {
        FullHttpRequest request = (FullHttpRequest)msg;
        String uri = ResourcesTools.INDEX;
        if(!request.uri().equals("/")) uri = request.uri();//如果是"/"则返回index.html
        return ResponseTools.handleResource(request,uri);
    }
}
