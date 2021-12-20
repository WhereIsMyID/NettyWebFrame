package com.NWF.nettyWebFrame.Handler;


import lombok.Data;

import java.util.List;

/**
 * description:应答报文包，封装了一次应答报文要写出的所有信息，以及对该报文的处理类型tag
 */
@Data
public class ResponsePackage {
    public static final int KEEP_ALIVE = 0;//心跳机制
    public static final int STATIC_FILE = 1;//静态文件
    public static final int NOT_KEEP_ALIVE = 2;//不需要心跳

    private int tag = KEEP_ALIVE;//表示报文包的处理类型，默认为保持心跳
    private List<Object> responses;//储存所有的报文信息

    public ResponsePackage(List<Object> responses){this.responses = responses;};

    public ResponsePackage(int tag, List<Object> responses) {
        this.tag = tag;
        this.responses = responses;
    }
}
