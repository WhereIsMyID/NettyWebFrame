package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


//静态资源获取
public class GetStaticFile extends RequestAction {
    @Override
    public ResponsePackage action(Object msg, ChannelHandlerContext ctx) {
        FullHttpRequest request = (FullHttpRequest)msg;
        String uri = "/index.html";
        if(!request.uri().equals("/")) uri = request.uri();//如果是"/"则返回index.html
        return ResponseTools.handleResource(request,uri);
    }
}
