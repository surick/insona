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
 * 定时任务接口
 */
@SuppressWarnings("unused")
@RestController("GizWitsCommon")
@RequestMapping("giz/common")
public class CommonSchedulerController {

    private RedisUtil redisUtil;

    @Autowired
    public CommonSchedulerController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取定时任务
     * 通用定时任务 为 定时任务管理 的升级接口，能对场景、分组和单个设备创建定时任务。
     * 请求地址及方式
     * <p>
     * GET
     * https://api.gizwits.com/app/common_scheduler?did={did}&group_id={group_id}&scene_id={scene_id}&limit={limit}&skip={skip}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	否	query	需要设置定时任务的设备id
     * group_id	                string	否	query	分组id
     * scene_id	                string	否	query	场景id
     * limit	                integer	否	query	返回的结果条数
     * skip                 	integer	否	query	表示跳过的条数，间接表示页数。
     * 响应参数
     * <p>
     * 参数	        类型  	描述
     * id	        string  定时任务id
     * created_at	string	任务创建时间 UTC 时间
     * product_key	string	产品PK
     * scene_id 	string  场景id
     * group_id	    string  分组id
     * did	        string	需要设置定时任务的设备id
     * raw	        string	原始控制指令
     * attrs	    object  数据点方式
     * date	        string	一次性定时任务的执行时间
     * time	        string	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	    string	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days	        Array	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date	string	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date 	string	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled	    boolean	是否启用，若不启用则时间到也不触发，默认启用
     * remark	    string	任务备注
     *
     * @param did     设备id
     * @param groupId 分组id
     * @param sceneId 场景id
     * @param limit   条数
     * @param skip    跳过的条数
     * @return json
     */
    @GetMapping("getCommon")
    public Result getCommon(@Param("did") String did,
                            @Param("groupId") String groupId,
                            @Param("sceneId") String sceneId,
                            @Param("limit") Integer limit,
                            @Param("skip") Integer skip) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/common_scheduler?" +
                        (did != null ? "did=" + did : "") +
                        (groupId != null ? "&group_id=" + groupId : "") +
                        (sceneId != null ? "&scene_id=" + sceneId : "") +
                        (limit != null ? "&limit=" + limit : "") +
                        (skip != null ? "&skip=" + skip : ""), getHead(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 创建定时任务
     * did, group_id, scene_id 参数只需传其中一个，分别用于创建设备，设备分组，用户场景定时任务，
     * 若同时传了多个，则按此优先级取第一个有效字符串；
     * 针对场景任务，不需要传 raw 和 attrs 控制命令，传了会被忽略；
     * 创建循环类型与定时任务管理接口一致。
     * 原始指令(raw):
     * 发送 0090 命令，只需要包括 payload 即可;格式为二进制转 byte 数组，如要发送 payload 为 010203，就是
     * <p>
     * {"raw": [1, 2, 3]}
     * 数据点方式(attrs):
     * 设备产品必须定义了数据点，比如要设置扩展类型的字段 binary 为1234567，需要补齐扩展型长度，设置布尔型需要是true和false：
     * <p>
     * {
     * "attrs": {
     * "boolean":true,
     * "binary": "1234567000"
     * }
     * }
     * 请求地址及方式
     * <p>
     * POST
     * https://api.gizwits.com/app/common_scheduler
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	否	body	需要设置定时任务的设备id
     * group_id	                string	否	body	需要设置定时任务的设备分组id
     * scene_id	                string	否	body	需要设置定时任务的场景id
     * raw	                    string	否	body	原始控制指令
     * attrs	                object	否	body
     * date	                    string	是	body	一次性定时任务的执行时间
     * time	                    string	是	body	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	                string	是	body	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days	                    Array	否	body	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date	            string	否	body	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date	                string	否	body	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled	                boolean	否	body	是否启用，若不启用则时间到也不触发，默认启用
     * remark	                string	否	body	任务备注
     * 响应参数
     * <p>
     * 参数	类型	    描述
     * id	string	通用定时任务id
     *
     * @param object 任务数据
     * @return id
     */
    @PostMapping("postCommon")
    public Result postCommon(@RequestBody JSONObject object) {
        if (String.valueOf(object.get("date")) == null ||
                String.valueOf(object.get("time")) == null ||
                String.valueOf(object.get("repeat")) == null) {
            return new Result().setError("参数不能为空");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/common_scheduler", getHead(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改定时任务
     * 请求地址及方式
     * PUT
     * https://api.gizwits.com/app/common_scheduler/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	通用定时任务id
     * did	                    string	否	body	需要设置定时任务的设备id
     * group_id	                string	否	body	需要设置定时任务的设备分组id
     * scene_id	                string	否	body	需要设置定时任务的场景id
     * raw	                    string	否	body	原始控制指令
     * attrs	                object	否	body
     * date	                    string	是	body	一次性定时任务的执行时间
     * time	                    string	是	body	定时任务执行的时间，精确到分钟，格式xx:xx，如：02:30
     * repeat	                string	是	body	是否重复，”none”,”mon”, “tue”, “wed”, “thu”, “fri”, “sat”, “sun”
     * days                 	Array	否	body	重复的日期列表，如[1, 15]表示每月1日和15日重复
     * start_date	            string	否	body	定时任务执行的开始日期，该天0点开始，格式xxxx-xx-xx，如2016-09-01
     * end_date         	    string	否	body	定时任务执行的结束日期，该天24点结束，格式xxxx-xx-xx，如2016-10-01
     * enabled	                boolean	否	body	是否启用，若不启用则时间到也不触发，默认启用
     * remark	                string	否	body	任务备注
     * 响应参数
     * <p>
     * 参数	类型	    描述
     * id	string	通用定时任务id
     *
     * @param id     任务id
     * @param object 任务信息
     * @return id
     */
    @PutMapping("updateCommon/{id}")
    public Result updateCommon(@PathVariable("id") String id,
                               @RequestBody JSONObject object) {
        if (String.valueOf(object.get("date")) == null ||
                String.valueOf(object.get("time")) == null ||
                String.valueOf(object.get("repeat")) == null) {
            return new Result().setError("参数不能为空");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/common_scheduler/".concat(id), getHead(), object, HttpMethod.PUT);
        return new Result().setData(jsonObject);
    }

    /**
     * 删除定时任务
     * 请求地址及方式
     * DELETE
     * https://api.gizwits.com/app/common_scheduler/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	通用定时任务id
     * 响应参数
     * <p>
     * 无
     *
     * @param id 任务id
     * @return 无
     */
    @DeleteMapping("removeCommon/{id}")
    public Result removeCommon(@PathVariable("id") String id) {
        TemplateUtil.restHttp("https://api.gizwits.com/app/common_scheduler/" + id, getHead(), null, HttpMethod.DELETE);
        return new Result().setData("删除成功");
    }

    /**
     * 获取消息头
     *
     * @return 消息头封装成map
     */
    private Map<String, String> getHead() {
        Map<String, String> map = new HashMap<>();
        String appid = String.valueOf(redisUtil.get("GWappid"));
        map.put("X-Gizwits-Application-Id", appid);
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        map.put("X-Gizwits-User-token", token);
        return map;
    }
}
