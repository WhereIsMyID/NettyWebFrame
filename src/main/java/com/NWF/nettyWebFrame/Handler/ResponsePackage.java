package com.NWF.nettyWebFrame.Handler;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//应答报文包
public class ResponsePackage {
    public static final int KEEP_ALIVE = 0;//存活机制
    public static final int STATIC_FILE = 1;//静态文件

    private int tag = KEEP_ALIVE;//表示报文包的种类
    private List<Object> responses;//储存所有的报文信息

    public ResponsePackage(List<Object> responses){this.responses = responses;};

    public ResponsePackage(int tag, List<Object> responses) {
        this.tag = tag;
        this.responses = responses;
    }
}
