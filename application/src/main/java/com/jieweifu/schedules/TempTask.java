package com.jieweifu.schedules;

import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.insona.Temp;
import com.jieweifu.services.insona.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class TempTask {
    private RedisUtil redisUtil;
    private TempService tempService;

    @Autowired
    public TempTask(RedisUtil redisUtil, TempService tempService) {
        this.redisUtil = redisUtil;
        this.tempService = tempService;
    }

    /**
     * 定时写入数据，目前注释注解
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void temp() {
        List<Temp> temps = (List<Temp>) redisUtil.get("temp");
        if (temps == null) {
            System.out.println("temps = null");
            return;
        }
        temps.forEach(
                temp -> {
                    if (redisUtil.get("temp") == null) {
                        System.out.println("temp = null");
                        return;
                    }
                    if (temp.getDid() == null || temp.getDid().equals("")) {
                        System.out.println("temp.getDid()==null");
                        return;
                    }
                    if (tempService.getTemp(temp.getDid()) != null) {
                        tempService.updateTemp(temp);
                        return;
                    }
                    tempService.saveTemp(temp);
                }
        );

    }
}
