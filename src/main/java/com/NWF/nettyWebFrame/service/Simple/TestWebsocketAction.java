package com.NWF.nettyWebFrame.service.Simple;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import com.NWF.nettyWebFrame.tools.RequestAction.WebsocketAction;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//websocket测试
public class TestWebsocketAction extends WebsocketAction {

    //重写文本处理器
    @Override
    protected ResponsePackage isTextWebSocketFrame(TextWebSocketFrame frame, ChannelHandlerContext ctx) {
        log.info("服务器收到消息："+frame.text());
        //回复消息
        List<Object> result = new ArrayList<>();//创建一个Object列表存放应答报文内容
        result.add(new TextWebSocketFrame("服务器" + LocalDateTime.now() + "： " + frame.text()));//为其添加一个websocket帧信息
        return new ResponsePackage(result);//将列表创建为应答报文包，构建应答报文
    }
}
