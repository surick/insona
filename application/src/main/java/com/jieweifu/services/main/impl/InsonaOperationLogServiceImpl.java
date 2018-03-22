package com.jieweifu.services.main.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.InsonaLogInfo;
import com.jieweifu.models.insona.InsonaOperation;
import com.jieweifu.models.insona.InsonaOperationLog;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.services.main.InsonaOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 陶Lyn
 * on 2018/3/16.
 */

@Service
public class InsonaOperationLogServiceImpl implements InsonaOperationLogService {
    @Autowired
    private DB db;

    @Override
    public List<InsonaOperation> getInsonaOperation() {
        return db.select()
                .from(InsonaOperation.class)
                .queryForList(InsonaOperation.class);
    }


    @Override
    public void addInsonaOperationLog(InsonaOperationLog insonaOperationLog) {
        db.insert()
                .save(insonaOperationLog)
                .execute();
    }

    @Override
    public List<InsonaLogInfo> getInsonaOperationLogByDynamicAndLimit(InsonaOperationLog insonaOperationLog, int pageIndex, int pageSize) {
        System.out.println("====>"+insonaOperationLog.getStartTime()+"+++________》》》》"+insonaOperationLog.getEndTime());
        StringBuffer sql = new StringBuffer("1=1 ");

        if (insonaOperationLog.getOpt() != null && insonaOperationLog.getOpt() != "") {
            sql.append(" and A.opt =" + "\'" + insonaOperationLog.getOpt() + "\'");
        }
        if (insonaOperationLog.getStartTime()!= null && insonaOperationLog.getStartTime()!="" && insonaOperationLog.getEndTime()!= null && insonaOperationLog.getEndTime()!=""){
            sql.append(" and A.crt_time between" + " \'" + insonaOperationLog.getStartTime() + "\'"+"and"+ " \'" + insonaOperationLog.getEndTime() + "\'");
        }
        if (insonaOperationLog.getCreateUser() != null) {
            sql.append(" and A.crt_user=" + insonaOperationLog.getCreateUser());
        }

        String mes = sql.toString();
        return db.select()
                .columns("A.*,B.username ")
                .from(InsonaOperationLog.class, "A")
                .leftOuterJoin(InsonaUser.class, "B", "A.crt_user=B.id")
                .where(mes)
                .limit(pageIndex, pageSize)
                .queryForList(InsonaLogInfo.class);
    }

    @Override
    public int getOperationLogTotal(InsonaOperationLog insonaOperationLog) {
        StringBuffer sql = new StringBuffer("1=1 ");

        if (insonaOperationLog.getOpt() != null && insonaOperationLog.getOpt() != "") {
            sql.append(" and opt =" + "\'" + insonaOperationLog.getOpt() + "\'");
        }
        if (insonaOperationLog.getStartTime()!= null && insonaOperationLog.getStartTime()!="" && insonaOperationLog.getEndTime()!= null && insonaOperationLog.getEndTime()!="") {
            sql.append(" and crt_time between" + " \'" + insonaOperationLog.getStartTime() + "\'"+"and"+ " \'" + insonaOperationLog.getEndTime() + "\'");
        }
        if (insonaOperationLog.getCreateUser() != null) {
            sql.append(" and crt_user=" + insonaOperationLog.getCreateUser());
        }

        String mes = sql.toString();
        return db.select()
                .from(InsonaOperationLog.class)
                .where(mes)
                .total();
    }

    @Override
    public List<InsonaLogInfo> getUserByGroupBy() {
        return db.select()
                .columns("a.*,b.username")
                .from(InsonaOperationLog.class,"a")
                .leftOuterJoin(InsonaUser.class,"b","a.crt_user=b.id")
                .groupBy("a.crt_user")
                .queryForList(InsonaLogInfo.class);
    }
}
