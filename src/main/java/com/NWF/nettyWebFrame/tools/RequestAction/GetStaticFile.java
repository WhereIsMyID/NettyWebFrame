package com.NWF.nettyWebFrame.tools.RequestAction;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.tools.ResponseTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


//静态资源获取
public class GetStaticFile extends RequestAction {
    @Override
    public ResponsePackage action(FullHttpRequest msg, ChannelHandlerContext ctx) {
        String uri = "/index.html";
        if(!msg.uri().equals("/")) uri = msg.uri();//如果是"/"则返回index.html
        return ResponseTools.handleResource(msg,uri);
    }
}
