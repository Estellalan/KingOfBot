package com.kob.backend1.consumer;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend1.consumer.utils.Game;
import com.kob.backend1.consumer.utils.JwtAuthentication;
import com.kob.backend1.mapper.RecordMapper;
import com.kob.backend1.mapper.UserMapper;
import com.kob.backend1.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Game game = null;

    /**
     * 由于WebSocket其实不算spring的一部分，并不是单例模式，所以这里注入mapper时方法如下
     */
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }

    public static RecordMapper recordMapper;
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    /**
     * 1.users是所有实例都可以访问的，所以这里定义成静态变量
     * （无论有多少个类的实例，静态变量只有一份副本，被该类的所有实例共享。静态变量也称为类变量。）
     * 2.ConcurrentHashMap 是线程安全的
     * （在Java中，线程安全指的是一种程序设计和编程语言的特性或程序的特性，
     * 它在多线程环境中能够正确地处理共享数据结构，
     * 使得多个线程可以并行执行而不会导致数据损坏或不可预期的行为。）
     */
    final public  static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session=null;//每个用户的连接用session维护

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session=session;
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        users.put(userId,this);

        if(this.user != null){
            users.put(userId,this);
        }else{
            this.session.close();
        }

        //System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        if(this.user!=null){
            users.remove(this.user.getId());
        }
    }

    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA((direction));
        }else if( game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message!");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        if("start-matching".equals(event)){
            startMatching();
        } else if("stop-matching".equals(event)){
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    private void stopMatching() {
        System.out.println("stop-matching");
        matchpool.remove(this.user);
    }

    private void startMatching() {
        System.out.println("start-matching:"+this.user.getUsername());
        matchpool.add(this.user);

        while (matchpool.size()>=2){
            Iterator<User> it = matchpool.iterator();
            User a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game = new Game(13, 14, 20, a.getId(), b.getId());
            game.createMap();
            game.start();//启动线程

            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;

            JSONObject respGame = new JSONObject();
            respGame.put("a_id", game.getPlayerA().getId());
            respGame.put("a_sx", game.getPlayerA().getSx());
            respGame.put("a_sy", game.getPlayerA().getSy());
            respGame.put("b_id", game.getPlayerB().getId());
            respGame.put("b_sx", game.getPlayerB().getSx());
            respGame.put("b_sy", game.getPlayerB().getSy());
            respGame.put("map", game.getG());

            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("game",respGame);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("game",respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
    * 在Java中，synchronized是一个关键字，用于控制多线程访问同步代码块以实现线程安全，它可以用在方法或者代码块中。
    * 当一个线程正在执行一个synchronized方法或者synchronized代码块时，
    * 其他线程不能执行该对象的任何其他synchronized方法或代码块，
    * 直到第一个线程执行完毕。这有助于防止多个线程同时访问相同的数据，可能导致数据状态不一致或其他线程安全问题。
    * */

    public void sendMessage(String message){
        synchronized (this.session){//我们是异步发信息，所以要加一个锁
            try {
                //从后端向前端发信息
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();//将异常直接输出
            }
        }
    }
}

