package com.jieweifu.common.gizwits;

/**
 * Created by 陶Lyn
 * on 2018/5/18.
 */

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.jieweifu.models.gizwits.SocketIODeviceEntity;



public class SocketQ {

    public static SocketIOClient client = null;


    public SocketQ() {
    }

    public void webSocketServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8888);
        final SocketIOServer server = new SocketIOServer(config);
      /*  LogFile log = new LogFile();
        log.setLine("11123123123bc");
*/
        server.addJsonObjectListener(SocketIODeviceEntity.class, new DataListener<SocketIODeviceEntity>() {
            @Override
            public void onData(SocketIOClient client, SocketIODeviceEntity data, AckRequest ackSender) {
                server.getBroadcastOperations().sendJsonObject(data);
            }
        });
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                // String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
                //server.getBroadcastOperations().sendJsonObject(data);
                SocketIODeviceEntity logFile = new SocketIODeviceEntity();
                logFile.setDeviceStatus("asdfzxc");
                SocketQ.client = client;
                client.sendEvent("messageevent", logFile);
                // server.getBroadcastOperations().sendJsonObject(log);

            }
        });

       /* server.addEventListener("messageevent", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                LogFile log = new LogFile();
                log.setLine("指定返回");
                server.getBroadcastOperations().sendJsonObject(log);
            }
        });*/
        server.start();
    }

    public static void main(String[] args) {
        SocketQ s = new SocketQ();
        s.webSocketServer();
    }

}
