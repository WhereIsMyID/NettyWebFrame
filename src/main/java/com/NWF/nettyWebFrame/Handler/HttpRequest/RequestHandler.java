package com.NWF.nettyWebFrame.Handler.HttpRequest;

import io.netty.handler.codec.http.FullHttpRequest;

//用于处理HttpRequest的接口
public interface RequestHandler {
    //用于处理GET和POST请求
    Object handle(FullHttpRequest fullHttpRequest);//接收一个完整的http报文
}
