package com.jieweifu.schedules;


import com.gizwits.noti2.client.Events;
import com.gizwits.noti2.client.LoginData;
import com.gizwits.noti2.client.NotiClient;
import com.gizwits.noti2.client.NotiEvent;
import com.jieweifu.models.insona.Type;
import com.jieweifu.services.insona.TypeService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 设备Snoti通讯
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class NotiComponent {


    private TypeService typeService;
    @Autowired
    public NotiComponent(TypeService typeService){
        this.typeService = typeService;
    }

    @Async
    public void start(){

        List<LoginData> loginDataList = new ArrayList<>();
        List<Events> events = new ArrayList<>();
        events.add(Events.getEvent("device.online"));
        events.add(Events.getEvent("device.offline"));
        events.add(Events.getEvent("device.status.kv"));
        List<Type> list = typeService.types();
        for (Type type : list) {
            LoginData loginData = new LoginData();
            loginData.setAuthId(type.getAppid());
            loginData.setAuthSecret(type.getAppsecret());
            loginData.setProductKey(type.getProduct_key());
            loginData.setSubkey("client");
            loginData.setEvents(events);
            loginDataList.add(loginData);
        }
        /*LoginData loginData = new LoginData();
        loginData.setAuthId(list.get(0).getAppid());
        loginData.setAuthSecret(list.get(0).getAppsecret());
        loginData.setProductKey(list.get(0).getProduct_key());
        loginData.setSubkey("client");
        loginData.setEvents(events);
        loginDataList.add(loginData);*/
        //List<LoginData> dataList = Arrays.asList(new LoginData("34882cc6fb934695857bc88a53554039","SUBIlBYnRKemOq11xirDow","Lf68PPHBTPakV+MG/f0KHg","client",events));
        NotiClient notiClient = NotiClient
                .build()
                .setHost("snoti.gizwits.com")
                .setPort(2017)
                .login(loginDataList);
        //启动
        notiClient.doStart();
        // 等待启动（启动链接耗时),否则无法发送控制指令
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){
            return;
        }
        //test send message
        //Map<String,Object> attrs = new HashMap<>();
        //attrs.put("temp_indoor", 50);
        //boolean sendBool1 =notiClient.sendControlMessage("34882cc6fb934695857bc88a53554039", "virtual:site", "hKrYMzDPF7wVFpfK4LVBZF", attrs);
        //System.out.println(sendBool1);
        //订阅(接收)推送事件消息,接收推送事件类型查看PushEvents枚举类
        Thread thread = new Thread(() -> {
            String messgae = null;
            while ((messgae = notiClient.receiveMessage()) != null) {
                System.out.println("实时接收snoti消息:" + messgae);
            }
        });
        thread.start();

        // 测试销毁事件
        //TimeUnit.SECONDS.sleep(5);
        //notiClient.destory();

            // 监听客户端销毁回调事件
        notiClient.addListener(event -> {

            if (event == NotiEvent.DESTORY) {
                try {
                    while (!notiClient.messageNone()) {
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    thread.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
