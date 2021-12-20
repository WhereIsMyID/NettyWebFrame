package com.NWF.nettyWebFrame.Handler.HttpRequest;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:处理Get请求
 */
public class GetRequest implements RequestHandler {

    /**
     * description:获取GET请求报文中的参数内容
     *
     * @Param:http请求报文
     * @return:报文内容
     */
    public Object handle(FullHttpRequest fullHttpRequest) {
        String uri = fullHttpRequest.uri();
        try {
            String contentType = getContentType(fullHttpRequest.headers());//获取传输的报文类型
            if(contentType.equals("application/json"))//判断是否是json类型的报文
            {
                return getJsonParams(uri);//直接获取json报文
            }
        }catch (NullPointerException e){
            Map<String,String> params = getParams(uri);//获取GET参数键值对
            return params.toString();//返回字符串类型
        }
        return null;
    }

    /**
     * description:根据GET报文中的GET参数解析成键值对
     *
     * @Param:get请求的路径
     * @return:get路径中所有参数的键值对
     */
    private Map<String,String> getParams(String uri)
    {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);//根据key-value解码uri
        Map<String, List<String>> getMaps = queryStringDecoder.parameters();//转换为Map形式
        Map<String,String> params = new HashMap<String, String>();
        for(Map.Entry<String,List<String>> i : getMaps.entrySet())//遍历getMaps的每个键值对
        {
            for(String s : i.getValue())//遍历该键值对中值的List
            {
                params.put(i.getKey(),s);//将该键与其List的最后一个值存入params
            }
        }
        return params;
    }

    /**
     * description: 获取json形式的报文
     *
     * @Param:get请求的路径
     * @return:解析出的json报文字符串
     */
    private String getJsonParams(String uri)
    {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
        Map<String, List<String>> getMaps = queryStringDecoder.parameters();
        String result = "{}";
        for(Map.Entry<String,List<String>> i : getMaps.entrySet())
        {
            result = i.getKey();
        }
        return result;
    }

    /**
     * description: 通过header来获取get传输的报文类型
     *
     * @Param:http报文头字段
     * @return:该http报文Content字段的类型
     */
    private String getContentType(HttpHeaders headers)
    {
        String type = headers.get("Content-Type");//获取http头部的type信息
        String[] list = type.split(";");//截取第一个内容
        return list[0];
    }
}
