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

@SuppressWarnings("unused")
@RestController("GizWitsGroup")
@RequestMapping("giz/group")
public class GroupController {

    private RedisUtil redisUtil;

    @Autowired
    public GroupController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 查询用户所有的分组
     * GET
     * https://api.gizwits.com/app/group
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * <p>
     * 响应参数	    类型	    描述
     * created_at	string	设备分组创建时间（UTC 时间）
     * updated_at	string	设备分组更新时间（UTC 时间）
     * product_key	string	产品 product_key，单PK的分组才会有值
     * group_name	string	设备分组名称
     * verbose_name	string	产品名称，单PK的分组才会有值
     * id	        string	设备分组 id
     *
     * @return json
     */
    @GetMapping("getGroup")
    public Result getGroup() {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group", getMap(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 创建分组
     * POST
     * https://api.gizwits.com/app/group
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * product_key	            string	否	body	产品PK，对单个pk分组需要输入此参数
     * group_name	            string	是	body	设备分组名称
     * <p>
     * 响应参数	类型  	描述
     * id	    string	设备分组id
     *
     * @param object product_key group_name
     * @return json
     */
    @PostMapping("postGroup")
    public Result postGroup(@RequestBody JSONObject object) {
        if (String.valueOf(object.get("product_key")) == null ||
                String.valueOf(object.get("group_name")) == null) {
            return new Result().setError("请晚上必要的分组信息");
        }
        Map<String, String> map = getMap();
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group", getMap(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 删除设备分组
     * DELETE
     * https://api.gizwits.com/app/group/{id}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	 path	设备分组 id
     * <p>
     * 响应参数
     * 无
     *
     * @param id 设备分组 id
     * @return json
     */
    @DeleteMapping("removeGroup/{id}")
    public Result removeGroup(@PathVariable("id") String id) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id, getMap(), null, HttpMethod.DELETE);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改分组信息
     * PUT
     * https://api.gizwits.com/app/group/{id}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	设备分组 id
     * group_name	            string	是	body	设备分组名称
     * <p>
     * 响应参数
     * 无
     *
     * @param id     设备分组 id
     * @param object group_name
     * @return json
     */
    @PutMapping("putGroup/{id}")
    public Result putGroup(@PathVariable("id") String id, @RequestBody JSONObject object) {
        if (String.valueOf(object.get("group_name")) == null) {
            return new Result().setError("请填写分组名称");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id, getMap(), object, HttpMethod.PUT);
        return new Result().setData(jsonObject);
    }

    /**
     * 查询分组的设备信息
     * GET
     * https://api.gizwits.com/app/group/{id}/devices
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	设备分组 ID
     * <p>
     * 响应参数	    类型	    描述
     * did	        string	该分组下的设备ID
     * type	        string	设备类型：普通设备：noramal；中控设备：center_control；中控子设备：sub_dev
     * verbose_name	string	产品名称，单PK的分组才会有值
     * dev_alias	string	设备别名
     * product_key	string	产品 product_key，单PK的分组才会有值
     *
     * @param id 设备分组 ID
     * @return json
     */
    @GetMapping("getDevices/{id}")
    public Result getDevices(@PathVariable("id") String id) {
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id + "/devices", getMap(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 将设备列表添加到分组
     * POST
     * https://api.gizwits.com/app/group/{id}/devices?show_detail={show_detail}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	设备分组 ID
     * show_detail	            integer	否	query	返回错误信息，1：添加失败的did返回详细错误信息，0(默认值)：添加失败的did，不返回详细错误信息
     * dids	                    Array	是	body	需要添加到分组的设备ID数组
     * <p>
     * 响应参数	        类型  	描述
     * failed	        Array	添加成功的设备ID 数组
     * detail	        Array	添加失败原因，show_detail为1时候才会返回此信息
     * success	        Array	添加失败的设备ID 数组
     * did	            string	添加的失败的设备ID
     * error_message	string	失败错误信息
     * error_code	    integer	失败错误码
     * detail_msg	    string	详细错误信息
     *
     * @param id         设备分组 ID
     * @param showDetail 返回错误信息
     * @param object     需要添加到分组的设备ID数组
     * @return json
     */
    @PostMapping("postDevices/{id}")
    public Result postDevices(@PathVariable("id") String id,
                              @Param("showDetail") String showDetail,
                              @RequestBody JSONObject object) {
        if (showDetail == null || String.valueOf(object.get("dids")).equals("[]")) {
            return new Result().setError("请输入正确的参数");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id + "/devices?show_detail=" + showDetail, getMap(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 将设备列表从分组移除
     * DELETE
     * https://api.gizwits.com/app/group/{id}/devices
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * id	                    string	是	path	设备分组 ID
     * dids	                    Array	是	body	需要移出分组的设备ID数组
     * <p>
     * 响应参数	类型	    描述
     * failed	Array	添加成功的设备ID 数组
     * success	Array	添加失败的设备ID 数组
     * did	    string	添加的失败的设备ID
     *
     * @param id     设备分组 ID
     * @param object 需要移出分组的设备ID数组
     * @return json
     */
    @DeleteMapping("removeDevices/{id}")
    public Result removeDevices(@PathVariable("id") String id, @RequestBody JSONObject object) {
        if (String.valueOf(object.get("dids")).equals("[]")) {
            return new Result().setError("请输入正确的参数");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id + "/devices", getMap(), object, HttpMethod.DELETE);
        return new Result().setData(jsonObject);
    }

    /**
     * 对设备分组内的设备统一控制
     * 通过云端对分组内的所有设备进行控制，该接口只能统一控制单PK分组的设备。
     * 原始指令(raw):
     * 发送 0090 命令，只需要包括 payload 即可;格式为二进制转 byte 数组，如要发送 payload 为 010203，就是
     * {"raw": [1, 2, 3]}
     * 数据点方式(attrs):
     * 设备产品必须定义了数据点，比如要设置扩展类型的字段 binary 为1234567，需要补齐扩展型长度，设置布尔型需要是true和false：
     * {
     * "attrs": {
     * "boolean":true,
     * "binary": "1234567000"
     * }
     * }
     * 请求方式
     * POST
     * https://api.gizwits.com/app/group/{id}/control
     * <p>
     * 请求参数	                类型	           必填	参数类型	描述
     * X-Gizwits-Application-Id	string	        是	header	appid
     * X-Gizwits-User-token	    string	        是	header	用户token
     * id	                    string	        是	path	设备分组 ID
     * raw	                    Array[integer]	否	body	原始控制指令
     * attrs	                object	        否	body    数据方式节点
     * <p>
     * 响应参数	类型	    描述
     * did	    string	分组下的设备ID
     * result	boolean	是否控制成功，true：成功；false：失败
     *
     * @param id     设备分组 ID
     * @param object 数据方式节点
     * @return json
     */
    @PostMapping("postControl/{id}")
    public Result postControl(@PathVariable("id") String id, @RequestBody JSONObject object) {
        if (String.valueOf(object.get("raw")).equals("[]") || String.valueOf(object.get("attrs")).equals("{}")) {
            return new Result().setError("请输入指令");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.gizwits.com/app/group/" + id + "/control", getMap(), object, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 从Ridis获取请求消息头
     *
     * @return 请求消息头
     */
    public Map<String, String> getMap() {
        String appid = String.valueOf(redisUtil.get("GWappid"));
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        Map<String, String> map = new HashMap<>();
        map.put("X-Gizwits-User-token", token);
        map.put("X-Gizwits-Application-Id", appid);
        return map;
    }

}
