package com.NWF.nettyWebFrame.service;

import com.NWF.nettyWebFrame.service.Simple.TestAsyncAction;
import com.NWF.nettyWebFrame.service.Simple.TestWebsocketAction;
import com.NWF.nettyWebFrame.service.Simple.TestAction;
import com.NWF.nettyWebFrame.tools.RequestAction.WebsocketActionSpawn;

public class ActionInitialization {
    //初始化所有测试业务处理方法
    public final static void action(){
        new TestAction().bind("/get");//为测试业务绑定访问路径
        new TestAsyncAction().bind("/async",false);//测试异步业务的路径
        WebsocketActionSpawn.bind(TestWebsocketAction.class,"/hello");//绑定自定义的websocket处理类
    }

}
