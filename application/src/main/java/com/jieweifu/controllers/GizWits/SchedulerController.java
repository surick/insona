package com.jieweifu.controllers.GizWits;


import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TemplateUtil;
import com.jieweifu.models.Result;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时器接口
 */
@SuppressWarnings("unused")
@RestController("GizWitsScheduler")
@RequestMapping("giz/scheduler")
public class SchedulerController {

    private RedisUtil redisUtil;

    @Autowired
    public SchedulerController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取定时任务
     * 定时任务分为如下几类：
     * <p>
     * 一次性定时任务
     * 按星期重复定时任务
     * 按天重复定时任务
     * 每个定时任务都可以设置使能状态，只有开启状态的定时任务才会被执行。
     * 定时任务管理下的全部接口均使用 UTC 时间
     * 定时任务管理接口只能对单个设备设置定时任务
     * GET
     * https://api.gizwits.com/app/devices/{did}/scheduler?limit=20&skip=0
     * 请求参数
     * <p>
     * 参数	                    类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	是	path	需要设置定时任务的设备id
     * limit	                integer	否	query	返回的结果条数
     * skip	                    integer	否	query	表示跳过的条数，间接表示页数。
     * 响应参数
     * <p>
     * 参数	        类型	    描述
     * id	        string	定时任务id
     * created_at	string	任务创建时间 UTC 时间
     * product_key	string	产品PK
     * did	        string	需要设置定时任务的设备id
     * raw	        string	原始控制指令
     * attrs    	object
     * date     	string	一次性定时任务的执行时间
     * time     	string	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	    string	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days	        Array	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date	string	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date 	string	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled	    boolean	是否启用，若不启用则时间到也不触发，默认启用
     * remark	    string	任务备注
     *
     * @param did   设备id
     * @param limit 信息条数
     * @param skip  跳过条数
     * @return json
     */
    @GetMapping("getScheduler/{did}")
    public Result getScheduler(@PathVariable("did") String did,
                               @Param("limit") Integer limit,
                               @Param("skip") Integer skip) {
        JSONObject jsonObject = TemplateUtil.restHttp("https://api.gizwits.com/app/devices/" + did + "/scheduler" +
                (limit != null ? "?limit=" + limit : "?limit=20") +
                (skip != null ? "&skip=" + skip : "&skip=0"), getHead(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 创建一次性定时任务
     * repeat 设置为 none
     * date 和 time 必须填写，并且注意 date 和 time 为执行定时任务时的 UTC 时间
     * 创建按星期重复定时任务
     * repeat 为重复星期，多个用逗号分隔；比如每周一执行，设置为 “mon”，每周一、周三执行，设置为 “mon,wed”；
     * repeat 为重复星期时，可选值为： mon,tue,wed,thu,fri,sat,sun
     * time 必须填写，并且注意星期和 time 为执行定时任务时的 UTC 时间
     * 可以选填 start_date 和 end_date 表示定时任务开启的日期范围
     * 创建按天重复的定时任务
     * repeat 设置为 day
     * days 设置为要执行的日期列表；比如每月 1 号执行，设置为 [“1”]；每月 1 号、15 号执行，设置为 [“1”, “15”]
     * time 必须填写，并且注意 days 和 time 为执行定时任务时的 UTC 时间
     * 可以选填 start_date 和 end_date 表示定时任务开启的日期范围
     * 原始指令(raw):
     * 发送 0090 命令，只需要包括 payload 即可;格式为二进制转 byte 数组，如要发送 payload 为 010203，就是
     * {
     * "raw": [1, 2, 3]
     * }
     * 数据点方式(attrs):
     * 设备产品必须定义了数据点，比如要设置扩展类型的字段 binary 为1234567，需要补齐扩展型长度，设置布尔型需要是true和false：
     * {
     * "attrs": {
     * "boolean":true,
     * "binary": "1234567000"
     * }
     * }
     * POST
     * https://api.gizwits.com/app/devices/{did}/scheduler
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	是	path	需要设置定时任务的设备id
     * raw	                    string	否	body	原始控制指令
     * attrs	                object	否	body
     * date	                    string	是	body	一次性定时任务的执行时间
     * time	                    string	是	body	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	                string	是	body	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days                 	Array	否	body	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date           	string	否	body	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date	                string	否	body	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled	                boolean	否	body	是否启用，若不启用则时间到也不触发，默认启用
     * remark	                string	否	body	任务备注
     * 响应参数
     * <p>
     * 参数	类型	    描述
     * id	string	定时任务id
     *
     * @param did    设备id
     * @param object 任务信息
     * @return id
     */
    @PostMapping("postScheduler/{did}")
    public Result postScheduler(@PathVariable("did") String did,
                                @RequestBody JSONObject object) {
        if (String.valueOf(object.get("date")) == null ||
                String.valueOf(object.get("time")) == null ||
                String.valueOf(object.get("repeat")) == null) {
            return new Result().setError("参数不能为空");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/devices/" + did + "/scheduler", getHead(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改定时任务
     * PUT
     * https://api.gizwits.com/app/devices/{did}/scheduler/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	是	path	需要设置定时任务的设备id
     * id	                    string	是	path	需要修改的定时任务id
     * raw	                    string	否	body	原始控制指令
     * attrs	                object	否	body
     * date	                    string	是	body	一次性定时任务的执行时间
     * time                 	string	是	body	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	                string	是	body	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days                 	Array	否	body	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date	            string	否	body	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date	                string	否	body	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled              	boolean	否	body	是否启用，若不启用则时间到也不触发，默认启用
     * remark               	string	否	body	任务备注
     * 响应参数
     * <p>
     * 参数	类型	    描述
     * id	string	定时任务id
     *
     * @param did    设备id
     * @param id     任务id
     * @param object 任务信息
     * @return id
     */
    @PutMapping("putScheduler/{did}/{id}")
    public Result putScheduler(@PathVariable("did") String did,
                               @PathVariable("id") String id,
                               @RequestBody JSONObject object) {
        if (String.valueOf(object.get("date")) == null ||
                String.valueOf(object.get("time")) == null ||
                String.valueOf(object.get("repeat")) == null) {
            return new Result().setError("参数不能为空");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/devices/" + did + "/scheduler/" + id, getHead(), object, HttpMethod.PUT);
        return new Result().setData(jsonObject);
    }

    /**
     * 删除定时任务
     * DELETE
     * https://api.gizwits.com/app/devices/{did}/scheduler/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * did	                    string	是	path	需要设置定时任务的设备id
     * id	                    string	是	path	定时任务id
     * 响应参数
     * <p>
     * 无
     *
     * @param did 设备id
     * @param id  任务id
     * @return 无
     */
    @PutMapping("removeScheduler/{did}/{id}")
    public Result removeScheduler(@PathVariable("did") String did,
                                  @PathVariable("id") String id) {
        TemplateUtil.restHttp("https://api.gizwits.com/app/devices/" + did + "/scheduler/" + id, getHead(), null, HttpMethod.DELETE);
        return new Result().setMessage("删除成功");
    }

    /**
     * 获取消息头
     *
     * @return map
     */
    private Map<String, String> getHead() {
        Map<String, String> map = new HashMap<>();
        String appid = String.valueOf(redisUtil.get("GWappid"));
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        map.put("X-Gizwits-Application-Id", appid);
        map.put("X-Gizwits-User-token", token);
        return map;
    }

}
