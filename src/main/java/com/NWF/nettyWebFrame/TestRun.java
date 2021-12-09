package com.NWF.nettyWebFrame;

import com.NWF.nettyWebFrame.service.ActionInitialization;

public class TestRun {
    //测试类
    public static void main(String[] args) throws Exception {
        ActionInitialization.action();//初始化所有的业务
        StartBoot startBoot = new StartBoot().webSocket(true);//开启websocket
        startBoot.run(8080);//设置端口号
    }
}
