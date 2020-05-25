package com.lang520.vip.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/27 10:27
 */

@Component
@ServerEndpoint(value = "/productWebSocket/{userId}", configurator = WebSocketConfig.class)
public class ProductWebSocket {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    // concurrent包的线程安全Set，用来存放每个客户端对应的ProductWebSocket对象。
    private static CopyOnWriteArraySet<ProductWebSocket> webSocketSet = new CopyOnWriteArraySet<ProductWebSocket>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //指定的sid，具有唯一性，暫定為用戶id
    private String sid = "";

    private Logger log = LoggerFactory.getLogger(ProductWebSocket.class);

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    /*获取在线用户*/
    public static CopyOnWriteArraySet getOnlineUser(){
        return webSocketSet;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId")String userId, Session session) {

        //如果新用户已存在则不连进来
        boolean flag = true;
        if(!webSocketSet.isEmpty()){
            for (ProductWebSocket productWebSocket : webSocketSet) {
                if(productWebSocket.sid.equals(userId)){
                    webSocketSet.remove(productWebSocket); // 从set中删除
                    subOnlineCount(); // 在线数减1
                    break;
                }
            }
        }



        log.info("新客户端连入，用户id：" + userId);
        this.session = session;
        this.sid = userId;
        webSocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线数加1
        if(userId!=null) {
//            List<String> totalPushMsgs = new ArrayList<String>();
//            totalPushMsgs.add(userId+"连接成功-"+"-当前在线人数为："+getOnlineCount());
            sendMessage(userId+"连接成功-"+"-当前在线人数为："+getOnlineCount());
            /*if(totalPushMsgs != null && !totalPushMsgs.isEmpty()) {
                totalPushMsgs.forEach(e -> sendMessage(e));
            }*/
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("一个客户端关闭连接");
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(sid+"用户发送过来的消息为："+message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
            log.info("推送消息成功，消息为：" + message);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("推送消息失败，消息为：" + message);
        }
    }

    /**
     *指定用户唯一标识推送
     */
    public static boolean sendtoUser(String message,String sendUserId) throws IOException {
        if(sendUserId!=null){
            for (ProductWebSocket key : webSocketSet) {
                if(key.sid.equals(sendUserId)){
                    key.sendMessage(message);
                }
            }

        } else {
                //如果用户不在线则返回不在线信息给自己
                return false;
        }
        return true;
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (ProductWebSocket productWebSocket : webSocketSet) {
            productWebSocket.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return OnlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        OnlineCount.incrementAndGet(); // 在线数加1
    }

    public static synchronized void subOnlineCount() {
        OnlineCount.decrementAndGet(); // 在线数加1
    }

}


