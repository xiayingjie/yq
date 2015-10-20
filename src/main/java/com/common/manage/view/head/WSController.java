package com.common.manage.view.head;

import com.common.manage.view.BaseController;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/13
 */
@ServerEndpoint("/ws/{uid}")
public class WSController extends BaseController{


    static Map<String,Session> map=new<String,Session>ConcurrentHashMap();
    @OnMessage
    public void onMessage(String message,Session session) throws IOException {

        // Print the client message for testing purposes
        System.out.println("Received: " + message);
        Map textMap=gson.fromJson(message,HashMap.class);
        System.out.println(textMap);
        System.out.println(map);
        Session ws=map.get(textMap.get("uid"));

        if(ws!=null) {
            System.out.println(ws.isOpen());
            if (ws.isOpen()) {
                ws.getBasicRemote().sendText(textMap.get("message").toString());
            } else {
                map.remove(textMap.get("uid"));
            }
        }

    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid) {
        map.put(uid, session);
        System.out.println(uid+"Client connected");

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(session.getId()+"Connection closed");
    }
}
