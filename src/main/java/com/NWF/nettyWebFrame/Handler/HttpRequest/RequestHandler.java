package com.NWF.nettyWebFrame.Handler.HttpRequest;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * description: 用于处理HttpRequest的接口
 */

public interface RequestHandler {
    /**
     * description:用于处理GET和POST请求
     *
     * @Param:http请求报文
     * @return:报文内容
     */
    Object handle(FullHttpRequest fullHttpRequest);//接收一个完整的http报文
}
