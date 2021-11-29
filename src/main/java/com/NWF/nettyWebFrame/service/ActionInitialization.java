package com.NWF.nettyWebFrame.service;

import com.NWF.nettyWebFrame.service.Simple.TestWebsocketAction;
import com.NWF.nettyWebFrame.tools.RequestAction.GetStaticFile;
import com.NWF.nettyWebFrame.service.Simple.TestAction;
import com.NWF.nettyWebFrame.tools.RequestAction.WebsocketActionSpawn;

public class ActionInitialization {
    //初始化所有业务处理方法
    public final static void action(){
        new TestAction().bind("/get");
        new GetStaticFile().bind("/");//设置静态资源根目录

        WebsocketActionSpawn.bind(TestWebsocketAction.class);//绑定自定义的websocket处理类
    }

}
