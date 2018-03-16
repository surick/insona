package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.ClientUtil;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TokenIdUtil;
import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaOperation;
import com.jieweifu.models.insona.InsonaOperationLog;
import com.jieweifu.services.main.InsonaOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陶Lyn
 * on 2018/3/16.
 */


@SuppressWarnings("unused")
@RestController("mainUserLog")
@RequestMapping("main/log")
@AdminAuthAnnotation
public class OperationLogController {

    @Autowired
    private InsonaOperationLogService insonaOperationLogService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TokenIdUtil tokenIdUtil;

    /**
     * 获得日志具体操作
     * */
    @AdminAuthAnnotation(check = false)
    @GetMapping("/getOperation")
    public Result getOperation(){

        List<InsonaOperation> list=new ArrayList<>();
        try {
            list= insonaOperationLogService.getInsonaOperation();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("查询失败");
        }
        return new Result().setData(list);
    }



    /**
     * 保存日志
     * */
    @PostMapping("/savaLog")
    public Result saveLog(@RequestBody InsonaOperationLog insonaOperationLog, HttpServletRequest request){

        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        //通过工具类获得ip
        String ip=ClientUtil.getClientIp(request);
        insonaOperationLog.setCreateHost(ip);
        insonaOperationLog.setCreateUser(id.toString());
        try {
            insonaOperationLogService.addInsonaOperationLog(insonaOperationLog);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result().setError("保存日志失败");
        }
        return  new Result().setMessage("保存日志成功");
    }

    /**
     * 查询日志
     * */
    @PostMapping("getLog/{pageIndex}/{pageSize}")
    public Result getLog(@RequestBody InsonaOperationLog insonaOperationLog,@PathVariable("pageIndex") int pageIndex,
                         @PathVariable("pageSize") int pageSize,HttpServletRequest request){
        if (pageIndex < 0 || pageSize < 0) {
            return new Result().setError("页码或条目数不合法");
        }
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        String ip=ClientUtil.getClientIp(request);
        insonaOperationLog.setCreateHost(ip);
        insonaOperationLog.setCreateUser(id.toString());
        List<InsonaOperationLog> list=null;
        try {
             list=insonaOperationLogService.getInsonaOperationLogByDynamicAndLimit(insonaOperationLog,pageIndex,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("查询失败");
        }
        return new Result().setData(list);
    }

}
