package com.NWF.nettyWebFrame.Handler.HttpSession;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:Session管理类
 */
@Slf4j
public class HttpSessionManager {

    //使用一个线程同步的哈希集合保存session集合 (sessionId,HttpSession对象)
    public static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * description:生成一个session,并保存至SESSION_MAP，返回session
     *
     * @return:生成的新的HttpSession对象
     */
    public static HttpSession createSession() {
        HttpSession session = new DefaultHttpSession();//获取一个SessionId
        String sessionId = session.getId();//HttpSession实现接口
        SESSION_MAP.put(sessionId,session);//保存当前session
        return session;
    }

    /**
     * description:判断一个session是否存在
     *
     * @Param:接收一个sessionId
     * @return:true为存在，false为不存在
     */
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

    /**
     * description:设置session的cookie标签(将以带有此标签的cookie的参数作为浏览器储存的sessionId)
     *
     * @Param:标签名称
     */
    public static void setTag(String tag)
    {
        DefaultHttpSession.SESSIONID = tag;
    }

    /**
     * description:删除一个session
     *
     * @Param:sessionId
     */
    public static void invalidate(String sessionId) {
        SESSION_MAP.remove(sessionId) ;
    }

    /**
     * description:获取HttpSession对象
     *
     * @Param:sessionId
     */
    public static HttpSession getSession(String sessionId) {
        return SESSION_MAP.get(sessionId) ;
    }

    /**
     * description:为客户端保存带有sessionId的cookie
     *
     * @Param:http应答报文
     * @return:为客户端创建的HttpSession对象
     */
    public static HttpSession setSessionId(HttpResponse response) {
        HttpSession session = HttpSessionManager.createSession();//创建新的session
        String encodeCookie = ServerCookieEncoder.STRICT.encode(DefaultHttpSession.SESSIONID,session.getId());//为前端cookie设置sessionId
        response.headers().set(HttpHeaderNames.SET_COOKIE,encodeCookie);//客户端保存Cookie数据
        log.info("创建session:"+session.getId());
        return session;
    }

    /**
     * description:判断当前请求是否含有session
     *
     * @Param: http请求报文
     * @return:没有cookie或者未含有定义的tag标签的cookie返回null,SESSION_MAP未含有该sessionId返回null，否则返回找到的HttpSession对象
     */
    public static HttpSession isHasSessionId(HttpRequest request) {
        String cookieStr = request.headers().get(HttpHeaderNames.COOKIE);//获取客户端头信息发送来的Cookie数据
        if (cookieStr == null || "".equals(cookieStr))//如果为空，返回false
        {
            log.info("客户端没有cookie");
            return null;
        }

        Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieStr);//得到cookie集合
        Iterator<Cookie> iter = cookieSet.iterator();//获取其集合的迭代器
        while(iter.hasNext())//遍历cookie集合
        {
            Cookie cookie = iter.next();
            if(DefaultHttpSession.SESSIONID.equals(cookie.name()))//如果当前cookie的名称是HttpSession的标识
            {
                if (HttpSessionManager.isExists(cookie.value()))//如果session管理器中含有当前的sessionId
                {
                    log.info("客户端cookie发现session: " + cookie.value());
                    return HttpSessionManager.getSession(cookie.value());//得到当前连接的session
                }
            }
        }
        return null;
    }

}
