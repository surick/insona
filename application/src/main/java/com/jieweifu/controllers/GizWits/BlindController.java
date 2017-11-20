package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TemplateUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Url;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("GizWitsBlind")
@RequestMapping("giz/blind")
public class BlindController {
    private RedisUtil redisUtil;
    private Url url;

    @Autowired
    public BlindController(RedisUtil redisUtil, Url url) {
        this.redisUtil = redisUtil;
        this.url = url;
    }

    /**
     * 通过MAC地址绑定设备
     * POST
     * http://api.gizwits.com/app/bind_mac
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * X-Gizwits-Timestamp  	string	是	header	请求时间戳，与服务器相差不能超过 5 分钟
     * X-Gizwits-Signature  	string	是	header	签名，计算方法为 lower(md5(product_secret + X-Gizwits-Timestamp ))
     * product_key          	string	是	body	产品product_key
     * mac	                    string	是	body	设备mac地址
     * remark                  	string	否	body	备注
     * dev_alias            	string	否	body	设备别名
     * set_owner	            integer	否	body	是否设置成 owner，只对开启了设备分享功能的产品有效；0（默认值）：不设置成owner，1：设置成owner
     * <p>
     * 响应参数     	类型	            描述
     * product_key	string	        产品product_key
     * did      	string	        设备id
     * mac	        string	        设备mac地址
     * is_online	boolean	        是否在线
     * passcode 	string	        设备 passcode
     * host     	string	        连接服务器的域名
     * port     	integer	        M2M 的 mqtt 端口号
     * port_s	    integer	        M2M 的 mqtt SSL 端口号
     * ws_port	    integer	        websocket 端口号
     * wss_port	    integer	        websocket SSL 端口号
     * remark	    string	        设备备注
     * is_disabled	boolean	        是否注销
     * type	        string	        设备类型，单品设备:normal,中控设备:center_control,中控子设备:sub_dev
     * dev_alias	string	        设备别名
     * dev_label	Array[string]	设备标签列表，目前用于语音 API 批量设备控制
     * role	        string	        绑定角色， 特殊用户:special,拥有者:owner,访客:guest,普通用户:normal
     *
     * @param productSecret productSecret
     * @param jsonObject    product_key mac
     * @return json
     */
    @PostMapping("postBlind/{productSecret}")
    public Result postBlind(@PathVariable("productSecret") String productSecret, @RequestBody JSONObject jsonObject) {
        String timesTamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature = DigestUtils.md5Hex(productSecret + timesTamp).toLowerCase();
        Map<String, String> map1 = getMap();
        map1.put("X-Gizwits-Timestamp", timesTamp);
        map1.put("X-Gizwits-Signature", signature);
        if (jsonObject.get("product_key") == null || jsonObject.get("mac") == null) {
            return new Result().setError("请设置正确的参数");
        }
        JSONObject result =
                TemplateUtil.restHttp(url.getBlindMac(), map1, jsonObject, HttpMethod.POST);
        return new Result().setData(result);
    }

    /**
     * 解除绑定信息
     * DELETE
     * https://api.gizwits.com/app/bindings
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * devices	                Array	是	body	需要解绑的设备数组
     * did	                    string	是	body	设备ID
     * 响应参数	类型	            描述
     * success	Array[string]	解绑成功的设备
     * failed	Array[string]	解绑失败的设备
     *
     * @param json json数据
     * @return json数据
     */
    @DeleteMapping("removeBlind")
    public Result removeBlind(@RequestBody JSONObject json) {
        if (String.valueOf(json.get("devices")).equals("[]")) {
            return new Result().setError("请输入正确的数组");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getDeleteBlind(), getMap(), json, HttpMethod.DELETE);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取绑定列表
     * <p>
     * <p>
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * limit	                integer	否  query	返回的结果条数
     * skip                 	integer	否	query	表示跳过的条数，间接表示页数
     * show_disabled        	integer	否	query	是否显示已注销的设备，1:显示，0:不显示
     * show_proto_ver       	integer	否	query	是否显示设备通信协议版本，1:显示，0:不显示
     * 响应参数
     * <p>
     * 参数	                类型	            描述
     * devices	            Array	        绑定的设备组
     * product_key	        string	        产品product_key
     * did	                string	        设备id
     * mac	                string	        设备mac地址
     * is_online	        boolean	        是否在线
     * passcode	            string	        设备 passcode
     * host     	        string	        连接服务器的域名
     * port	                integer	        M2M 的 mqtt 端口号
     * port_s	            integer	        M2M 的 mqtt SSL 端口号
     * ws_port	            integer	        websocket 端口号
     * wss_port	            integer	        websocket SSL 端口号
     * remark	            string	        设备备注
     * is_disabled	        boolean	        是否注销
     * type	                string	        设备类型，单品设备:normal,中控设备:center_control,中控子设备:sub_dev
     * dev_alias	        string	        设备别名
     * dev_label	        Array[string]	设备标签列表，目前用于语音 API 批量设备控制
     * proto_ver	        string	        协议版本号，’01’, ‘01_01’, ‘03’, ‘04’
     * wifi_soft_version	string	        wifi版本号
     * is_sandbox	        boolean	        是否连接sandbox环境
     * role	                string	        绑定角色， 首绑用户:special,拥有者:owner,访客:guest,普通用户:normal
     *
     * @param limit          结果条数
     * @param skip           跳过的条数
     * @param show_disabled  是否显示已销毁的设备
     * @param show_proto_ver 是否显示通信版本协议
     * @return json数据
     */
    @GetMapping("getBlind")
    public Result getBlind(@Param("limit") Integer limit,
                           @Param("skip") Integer skip,
                           @Param("show_disabled") Integer show_disabled,
                           @Param("show_proto_ver") Integer show_proto_ver) {
        StringBuilder urlBuild = new StringBuilder(url.getGetBlind());
        String urlPath = String.valueOf(urlBuild.append("?").append
                (!(limit == null) ? "limit=" + limit : "0").append
                (!(skip == null) ? "&skip=" + skip : "0").append
                (!(show_disabled == null) ? "&show_disabled=" + show_disabled : "0").append
                (!(show_proto_ver == null) ? "&show_proto_ver=" + show_proto_ver : "0"));
        JSONObject jsonObject = TemplateUtil.restHttp(urlPath, getMap(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 通过二维码绑定设备
     * POST
     * https://api.gizwits.com/app/bind_latest
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * qr_content	            string	是	body	通过扫描二维码得出的字符串
     * set_owner	            integer	否	body	是否设置成 owner，只对开启了设备分享功能的产品有效；0（默认值）：不设置成owner，1：设置成owner
     * <p>
     * 响应参数
     * 无
     *
     * @param json json数据
     * @return json数据
     */
    @PostMapping("postQRCode")
    public Result postQRCode(@RequestBody JSONObject json) {
        if (String.valueOf(json.get("qr_content")).equals("")) {
            return new Result().setError("请先扫描二维码");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getBlindLatest(), getMap(), json, HttpMethod.POST);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改绑定信息
     * PUT
     * https://api.gizwits.com/app/bindings/{did}
     * <p>
     * 请求参数	                 类型	        必填	参数类型	描述
     * X-Gizwits-Application-Id	string	        是	header	appid
     * X-Gizwits-User-token	    string	        是	header	用户token
     * did	                    integer	        是	path	设备ID
     * remark	                string	        否	body	设备备注
     * dev_alias	            string	        否	body	设备别名
     * dev_label	            Array[string]	否	body	设备标签列表，目前用于语音 API 批量设备控制
     * 注：body的参数最少填写输入一个
     * <p>
     * 响应参数	     类型	          描述
     * remark	    string	        设备备注
     * dev_label	Array[string]	设备标签列表，目前用于语音 API 批量设备控制
     * dev_alias	string	        设备别名
     *
     * @param did  绑定的did值
     * @param json map数据
     * @return json数据
     */
    @PutMapping("updateBlind/{did}")
    public Result updateBlind(@PathVariable("did") String did, @RequestBody JSONObject json) {
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPutBlind().replace("{did}", did), getMap(), json, HttpMethod.PUT);
        return new Result().setData(jsonObject);
    }

    /**
     * 查询设备绑定的Guest用户
     * 只有owner用户才可以调用此接口
     * GET
     * https://api.gizwits.com/app/{did}/bindings
     * <p>
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	是	path	设备ID
     * <p>
     * 响应参数	     类型	描述
     * username	    string	账号username,中间4个字母*替代
     * phone	    string	账号phone,中间4个数字*替代
     * created_at	string	Guest 用户接受分享的时间（UTC时间）
     * uid	        string	Guest用户的UID
     * email	    string	账号email,@前面4个字符*替代
     *
     * @param did 设备ID
     * @return json数据
     */
    @GetMapping("getBlinding/{did}")
    public Result getBlinding(@PathVariable("did") String did) {
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getGetDidBlind().replace("{did}", did), getMap(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 解绑 Guest 用户
     * 此接口只有 Owner 用户可调用，只返回 Guest 的用户信息，不返回自己的用户信息
     * DELETE
     * https://api.gizwits.com/app/{did}/bindings
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * did	                    string	是	path	设备ID
     * uid	                    string	否	query	guest 的 uid，不能是自己
     * <p>
     * 响应参数
     * 无
     *
     * @param did 设备ID
     * @param uid Guest用户的uid
     * @return json数据
     */
    @DeleteMapping("removeBlinds/{did}")
    public Result removeBlinds(@PathVariable("did") String did,
                               @Param("uid") String uid) {
        Map<String, String> map1 = getMap();
        StringBuilder urlBuild = new StringBuilder(url.getDeleteDidBlind());
        String urlPath = String.valueOf(urlBuild.append("?uid=").append(uid));
        System.out.println(urlPath.replace("{did}", did));
        JSONObject jsonObject =
                TemplateUtil.restHttp(urlPath.replace("{did}", did), getMap(), null, HttpMethod.DELETE);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取消息头
     *
     * @return 消息头封装成map
     */
    public Map<String, String> getMap() {
        String appid = String.valueOf(redisUtil.get("GWappid"));
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        Map<String, String> map1 = new HashMap<>();
        map1.put("X-Gizwits-Application-Id", appid);
        map1.put("X-Gizwits-User-token", token);
        return map1;
    }
}
