package com.NWF.nettyWebFrame.Handler.HttpSession;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//Session管理类
public class HttpSessionManager {
    //使用一个线程同步的哈希集合保存session集合 (sessionId,HttpSession对象)
    private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<>();

    //生成一个session,返回sessionId
    public static String createSession() {
        HttpSession session = new DefaultHttpSession();//获取一个SessionId
        String sessionId = session.getId();//HttpSession实现接口
        SESSION_MAP.put(sessionId,session);//保存当前session
        return sessionId;
    }

    //判断一个session是否存在
    public static boolean isExists(String sessionId) {
        if(SESSION_MAP.containsKey(sessionId))//判断在集合中是否有这个id
        {
            HttpSession session = SESSION_MAP.get(sessionId);//如果有则获取其对象
            if(session.getId() == null)//判断该Session是否已经被销毁了
            {
                invalidate(sessionId);//移除这个session
                return false;
            }
            return true;//否则存在
        }
        else{
            return false;
        }
    }

    //删除一个session
    public static void invalidate(String sessionId) {
        SESSION_MAP.remove(sessionId) ;
    }

    //获取HttpSession对象
    public static HttpSession getSession(String sessionId) {
        return SESSION_MAP.get(sessionId) ;
    }

    //为客户端保存带有sessionId的cookie
    public static void setSessionId(boolean exists, HttpResponse response) {
        if(exists == false)//如果用户发送来的头信息里面不包含有SessionId内容
        {
            //为其创建一个session
            String encodeCookie = ServerCookieEncoder.STRICT.encode(HttpSession.SESSIONID, HttpSessionManager.createSession()) ;
            response.headers().set(HttpHeaderNames.SET_COOKIE,encodeCookie) ;//客户端保存Cookie数据
            System.out.println("创建session");
        }
    }

    //判断当前请求是否含有session
    public static HttpSession isHasSessionId(HttpRequest request) {
        String cookieStr = request.headers().get(HttpHeaderNames.COOKIE);//获取客户端头信息发送来的Cookie数据
        if (cookieStr == null || "".equals(cookieStr))//如果为空，返回false
        {
            System.out.println("没有cookie");
            return null;
        }

        System.out.println("发现cookie");
        Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieStr);//得到cookie集合
        Iterator<Cookie> iter = cookieSet.iterator();//获取其集合的迭代器
        while(iter.hasNext())//遍历cookie集合
        {
            Cookie cookie = iter.next() ;
            if(HttpSession.SESSIONID.equals(cookie.name()))//如果当前cookie的名称是HttpSession的标识
            {
                if (HttpSessionManager.isExists(cookie.value()))//如果session管理器中含有当前的sessionId
                {
                    System.out.println("发现session" + cookie.value());
                    return HttpSessionManager.getSession(cookie.value());//得到当前连接的session
                }
            }
        }
        return null;
    }

}
