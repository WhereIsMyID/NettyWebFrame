package com.NWF.nettyWebFrame;

import com.NWF.nettyWebFrame.service.ActionInitialization;

public class TestRun {
    //测试类
    public static void main(String[] args) throws Exception {
        ActionInitialization.action();//初始化所有的业务
        StartBoot startBoot = new StartBoot()
                .webSocket(true)//开启websocket
                .openLog(true);//开启报文日志
        startBoot.run(8080);//开启服务
    }
}
