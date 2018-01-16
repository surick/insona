package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TemplateUtil;
import com.jieweifu.models.Result;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 场景接口
 */
@SuppressWarnings("unused")
@RestController("GizWitsScene")
@RequestMapping("giz/scene")
public class SceneController {

    private RedisUtil redisUtil;

    @Autowired
    public SceneController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 查询用户所有场景
     * GET
     * https://api.gizwits.com/app/scene
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * 响应参数
     * <p>
     * 参数       	类型  	描述
     * id	        string	场景 id
     * scene_name	string	场景名称
     * created_at	string	场景创建时间（UTC 时间）
     * updated_at	string	场景更新时间（UTC 时间）
     * remark	    string	场景备注
     * task_type	string	场景任务类型：”delay”: 延时任务，”device”: 控制当个设备任务，”group”: 控制设备分组任务（仅可控制单 product_key 分组)
     * did	        string	针对单品控制任务，设备 did
     * product_key	string	针对非延时任务，产品 product_key
     * group_name	string	针对设备分组控制任务，设备分组名称
     * raw	        string	针对非延时任务，原始控制指令，base64 编码
     * attrs	    object	针对非延时任务，数据点控制命令，枚举型数据点传序号，扩展型数据点传 base64 编码字符串
     * dev_remark	string	针对设备控制任务，设备备注
     * time	        integer	针对延时任务，延时时间，以秒为单位，最大值： 3600 秒
     * group_id	    string	针对设备分组控制任务，设备分组 id
     * verbose_name	string	针对非延时任务，产品名称
     * dev_alias	string	针对设备控制任务，设备别名
     *
     * @return {id}
     */
    @GetMapping("getScene")
    public Result getScene() {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene", getHead(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 创建场景
     * POST
     * https://api.gizwits.com/app/scene
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * scene_name	            string	是	body	场景名称
     * remark	                string	否	body	场景备注
     * task_type	            string	是	body	场景任务类型： “delay”: 延时任务，”device”: 控制当个设备任务，”group”: 控制设备分组任务（仅可控制单 product_key 分组)
     * time                 	integer	否	body	针对延时任务，延时时间，以秒为单位，最大值： 3600 秒
     * did	                    string	否	body	针对单品控制任务，设备 did
     * group_id	                string	否	body	针对设备分组控制任务，设备分组 id
     * attrs	                object	否	body	针对非延时任务，数据点控制命令，枚举型数据点传序号，扩展型数据点传 base64 编码字符串
     * raw	                    string	否	body	针对非延时任务，原始控制指令，base64 编码
     * 响应参数
     * <p>
     * 参数	类型	    描述
     * id	string	场景 id
     *
     * @param object 场景参数
     * @return id
     */
    @PostMapping("postScene")
    public Result postScene(@RequestBody JSONObject object) {
        if (String.valueOf(object.get("scene_name")) == null ||
                String.valueOf(object.get("task_type")) == null) {
            return new Result().setError("请设置必要的参数");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene", getHead(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 删除场景
     * DELETE
     * https://api.gizwits.com/app/scene/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * id	                    string	是	path	场景ID
     * 响应参数
     * <p>
     * 无
     *
     * @param id 场景id
     * @return message
     */
    @DeleteMapping("removeScene/{id}")
    public Result removeScene(@PathVariable("id") String id) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene/" + id, getHead(), null, HttpMethod.DELETE);
        return new Result().setMessage("删除成功");
    }

    /**
     * 修改场景
     * POST
     * https://api.gizwits.com/app/scene/{id}
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * id	                    string	是	path	场景ID
     * scene_name           	string	是	body	场景名称
     * remark	                string	否	body	场景备注
     * task_type	            string	是	body	场景任务类型： “delay”: 延时任务，”device”: 控制当个设备任务，”group”: 控制设备分组任务（仅可控制单 product_key 分组)
     * time	                    integer	否	body	针对延时任务，延时时间，以秒为单位，最大值： 3600 秒
     * did                  	string	否	body	针对单品控制任务，设备 did
     * group_id	                string	否	body	针对设备分组控制任务，设备分组 id
     * attrs	                object	否	body	针对非延时任务，数据点控制命令，枚举型数据点传序号，扩展型数据点传 base64 编码字符串
     * raw	                    string	否	body	针对非延时任务，原始控制指令，base64 编码
     * 响应参数
     * <p>
     * 无
     *
     * @return message
     */
    @PutMapping("updateScene/{id}")
    public Result updateScene(@PathVariable("id") String id, @RequestBody JSONObject object) {
        if (String.valueOf(object.get("scene_name")) == null ||
                String.valueOf(object.get("task_type")) == null) {
            return new Result().setError("请设置必要的参数");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene/" + id, getHead(), object, HttpMethod.PUT);
        return new Result().setMessage("修改成功");
    }

    /**
     * 查询场景任务执行状态
     * GET
     * https://api.gizwits.com/app/scene/{id}/task
     * 请求参数
     * <p>
     * 参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	场景ID
     * 响应参数
     * <p>
     * 参数	类型	描述
     * status	integer	场景任务执行状态,0：进行中;1：执行完成;2：执行失败;3：任务未在执行
     *
     * @param id 场景id
     * @return message
     */
    @GetMapping("getTask/{id}")
    public Result getTask(@PathVariable("id") String id) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene/" + id + "/task", getHead(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 执行场景任务
     * POST
     * https://api.gizwits.com/app/scene/{id}/task
     * 请求参数
     * <p>
     * 参数                   	类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id                   	string	是	path	场景ID
     * 响应参数
     * <p>
     * 无
     *
     * @param id 场景id
     * @return message
     */
    @PostMapping("postTask/{id}")
    public Result postTask(@PathVariable("id") String id) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/scene/" + id + "/task", getHead(), null, HttpMethod.POST);
        return new Result().setMessage("开始执行任务");
    }

    /**
     * 从redis抓取消息头
     *
     * @return map
     */
    private Map<String, String> getHead() {
        Map<String, String> map = new HashMap<>();
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        String appid = String.valueOf(redisUtil.get("GWappid"));
        map.put("X-Gizwits-Application-Id", appid);
        map.put("X-Gizwits-User-token", token);
        return map;
    }
}
