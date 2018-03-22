package com.jieweifu.services.main;

/**
 * Created by 陶Lyn
 * on 2018/3/16.
 */

import com.jieweifu.models.insona.InsonaLogInfo;
import com.jieweifu.models.insona.InsonaOperation;
import com.jieweifu.models.insona.InsonaOperationLog;

import java.util.List;

/**
 * 日志操作
 * */
public interface InsonaOperationLogService {
    //查询所有具体操作
     List<InsonaOperation> getInsonaOperation();

     //保存操作日志
    void addInsonaOperationLog(InsonaOperationLog insonaOperationLog);

    //动态查询
    List<InsonaLogInfo> getInsonaOperationLogByDynamicAndLimit(InsonaOperationLog insonaOperationLog, int pageIndex, int pageSize);

    //查询日志总数
    int getOperationLogTotal(InsonaOperationLog insonaOperationLog);

    //查询有日志操作的用户 （通过分组查询）

    List<InsonaLogInfo> getUserByGroupBy();
}
