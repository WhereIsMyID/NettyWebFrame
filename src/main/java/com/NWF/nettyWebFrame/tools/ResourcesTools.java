package com.NWF.nettyWebFrame.tools;

import com.NWF.nettyWebFrame.Handler.ResponsePackage;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * description:静态资源操作工具
 */
@Slf4j
public class ResourcesTools {
    //静态文件根目录路径
    public static String location = ResourcesTools.class.getClass().getResource("/static").getPath();
    //展示的首页路径
    public static String INDEX = "/index.html";
    //错误页面路径
    public static String NOT_FOUND = "/404.html";

    //私有化构造方法
    private ResourcesTools(){}

    /**
     * description:返回一个404页面
     *
     * @Param:http请求报文对象
     * @return:封装404页面文件的应答报文包
     */
    public static ResponsePackage notFound(FullHttpRequest msg) throws IOException {
        String path = location + NOT_FOUND;
        File file = new File(path);
        return handleFile(msg,file,HttpResponseStatus.NOT_FOUND);
    }

    /**
     * description:请求静态资源
     *
     * @Param1:http请求报文对象
     * @Param2:资源路径
     * @return:封装页面文件的应答报文包
     */
    public static ResponsePackage handleResource(FullHttpRequest msg, String resource)
    {
        String path = location + resource;
        log.info("请求资源："+path);
        try {
            File file = new File(path);
            if(!file.exists() || file.isDirectory())//如果文件不存在或者是个目录
            {
                return notFound(msg);//返回一个404页面
            }
            return handleFile(msg,file,HttpResponseStatus.OK);
        }catch (IOException e)
        {
            return null;
        }
    }

    /**
     * description:根据文件的字节流，构建应答报文包
     *
     * @Param1:http请求报文对象
     * @Param2:打开的文件对象
     * @Param3:应答报文的结果状态码
     * @return:封装页面文件的应答报文包
     */
    private static ResponsePackage handleFile(FullHttpRequest msg,File file,HttpResponseStatus status) throws IOException{
        List<Object> responses = new ArrayList<>();//应答报文列表

        RandomAccessFile raf = new RandomAccessFile(file,"r");
        HttpHeaders headers = getContentTypeHeader(file);//获取文件的类型
        HttpResponse response = new DefaultHttpResponse(msg.protocolVersion(),status,headers);//根据报文头部
        responses.add(response);
        DefaultFileRegion defaultFileRegion = new DefaultFileRegion(raf.getChannel(), 0, raf.length());
        defaultFileRegion.retain();
        responses.add(defaultFileRegion);//写入文件
        responses.add(LastHttpContent.EMPTY_LAST_CONTENT);

        ResponsePackage responsePackage = new ResponsePackage(ResponsePackage.STATIC_FILE,responses);//创建一个报文应答包
        return responsePackage;
    }


    /**
     * description:获取文件资源的类型
     *
     * @Param:打开的文件对象
     * @return:该文件的类型(http头字段)
     */
    private static HttpHeaders getContentTypeHeader(File file)
    {
        MimetypesFileTypeMap map = new MimetypesFileTypeMap();
        HttpHeaders headers = new DefaultHttpHeaders();
        String contentType = map.getContentType(file);
        if(contentType.equals("text/plain"))
        {
            contentType = "text/plain;charset=ytf-8";
        }
        if(file.getName().endsWith(".css")) contentType = "text/css";
        else if(file.getName().endsWith(".js")) contentType = "application/javascript";
        headers.set(HttpHeaderNames.CONTENT_TYPE,contentType);
        return headers;
    }
}
