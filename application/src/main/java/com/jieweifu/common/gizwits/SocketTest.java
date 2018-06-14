package com.jieweifu.common.gizwits;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.jieweifu.models.gizwits.SocketIODeviceEntity;

/**
 * Created by 陶Lyn
 * on 2018/5/22.
 */
public class SocketTest {
    public void init() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        SocketIOServer server = new SocketIOServer(config);
        CharteventListener listner = new CharteventListener();
        listner.setServer(server);
        // chatevent为事件名称
        server.addEventListener("chatevent", SocketIODeviceEntity.class, listner);
        //启动服务
        server.start();

    }
}
