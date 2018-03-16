package com.jieweifu.services.main.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.InsonaOperation;
import com.jieweifu.models.insona.InsonaOperationLog;
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
    public List<InsonaOperationLog> getInsonaOperationLogByDynamicAndLimit(InsonaOperationLog insonaOperationLog,int pageIndex, int pageSize) {
        StringBuffer sql=new StringBuffer("1=1 ");

        if(insonaOperationLog.getOpt()!=null){
            sql.append(" and opt ="+"\'"+insonaOperationLog.getOpt()+"\'");
        }
        if(insonaOperationLog.getCreateTime()!=null){
            sql.append(" and crt_time="+"\'"+insonaOperationLog.getCreateTime()+"\'");
        }
        if(insonaOperationLog.getCreateUser()!=null){
            sql.append(" and crt_user="+"\'"+insonaOperationLog.getCreateUser()+"\'");
        }
        if(insonaOperationLog.getCreateHost()!=null){
            sql.append(" and crt_host="+"\'"+insonaOperationLog.getCreateHost()+"\'");
        }
        String mes=sql.toString();
        return db.select()
                .from(InsonaOperationLog.class)
                .where(mes)
                .limit(pageIndex,pageSize)
                .queryForList(InsonaOperationLog.class);
    }
}
