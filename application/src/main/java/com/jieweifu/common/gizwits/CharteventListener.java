package com.jieweifu.common.gizwits;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.jieweifu.models.gizwits.SocketIODeviceEntity;

/**
 * Created by 陶Lyn
 * on 2018/5/22.
 */
public class CharteventListener implements DataListener<SocketIODeviceEntity> {

    SocketIOServer server;

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient socketIOClient, SocketIODeviceEntity logFile, AckRequest ackRequest) throws Exception {
        this.server.getBroadcastOperations().sendEvent("chatevent", logFile);
    }
}
