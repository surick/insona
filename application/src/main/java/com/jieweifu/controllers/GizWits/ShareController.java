package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.HttpUtil;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.Result;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
@RestController("GizWitsShare")
@RequestMapping("giz/share")
public class ShareController {

    private RedisUtil redisUtil;

    @Autowired
    public ShareController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 创建分享邀请
     * 此接口只有设备主账号可以调用
     * 如果 type 为普通分享，uid/username/email/phone 四选一，为是被分享者的用户信息；
     * 如果 type 为二维码分享，uid、username、email、phone 设置为空，若设置不会生效；
     * 普通分享24小时过期，二维码分享15分钟过期；
     * 针对二维码分享，客户端收到返回的二维码内容之后，本地生成二维码图片；
     * POST
     * https://api.gizwits.com/app/sharing
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * type                 	integer	是	body	分享类型，0：普通分享，1：二维码分享
     * did                  	string	否	body	设备ID
     * uid                  	string	否	body	普通分享类型，被分享的用户ID
     * username	                string	否	body	普通分享类型，被分享的用户名
     * email	                string	否	body	普通分享类型，被分享的用户邮箱地址
     * phone                	string	否	body	普通分享类型，被分享的用户手机号码
     * duration	                integer	否	body	持续分享时间，guest 接受分享之后可使用设备的时长，单位：分钟。最小时长 1 分钟，最大时长 1440 分钟
     * <p>
     * 响应参数	    类型	    描述
     * id	        string	分享记录id，针对普通分享才有
     * qr_content	integer	二维码图片内容，针对二维码分享才有
     *
     * @param json type必填,uid/username/phone/email
     * @return json
     */
    @PostMapping("postShare")
    public Result postShare(@RequestBody JSONObject json) {
        JSONObject jsonObject = null;
        if(String.valueOf(json.get("did")).equals("")){
            return new Result().setError("设备id不能为空");
        }
        if (String.valueOf(json.get("type")).equals("0")) {
            if (json.get("uid") == null &&
                    json.get("username") == null &&
                    json.get("email") == null &&
                    json.get("phone") == null) {
                return new Result().setError("参数不合法");
            } else {
                Map<String, String> map = getMap();
                jsonObject = HttpUtil.postSSL("https://api.gizwits.com/app/sharing", map, json);
            }
        } else if (String.valueOf(json.get("type")).equals("1")) {
            if (json.get("uid") != null &&
                    json.get("username") != null &&
                    json.get("email") != null &&
                    json.get("phone") != null) {
                return new Result().setError("参数不合法");
            } else {
                Map<String, String> map = getMap();
                jsonObject = HttpUtil.postSSL("https://api.gizwits.com/app/sharing", map, json);
            }
        } else {
            return new Result().setError("分享类型不正确");
        }
        return new Result().setData(jsonObject);
    }

    /**
     * 查询分享邀请
     * GET
     * https://api.gizwits.com/app/sharing?sharing_type={sharing_type}
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * sharing_type         	integer	是	query	分享类型，0：我分享的设备，1：分享给我的设备
     * status	                string	否	query	分享状态，0：未接受分享，1：已接受分享，2：拒绝分享，3：取消分享
     * did                  	string	否	query	指定的设备did，只有owner用户可查询指定did的分享信息
     * limit                	integer	否	query	返回的条数，默认20
     * skip	                    integer	否	query	跳过的条数，默认0
     * <p>
     * 响应参数	    类型	    描述
     * total	    integer	总条数
     * limit	    integer	返回的条数
     * skip     	integer	跳过的条数
     * previous	    string	上一页的请求地址
     * next	        string	下一页的请求地址
     * id	        integer	分享记录id
     * type     	integer	分享类型，0：普通分享，1：二维码分享
     * uid	        string	用户ID
     * username 	string	用户名,中间4个字母*替代
     * user_alias	string	用户别名
     * email	    string	用户邮箱地址,@前面4个字符*替代
     * phone	    string	用户手机号码,中间4个数字*替代
     * did	        string	设备id
     * product_name	string	产品名称
     * dev_alias	string	设备别名
     * status	    integer	当前分享状态 0：未接受分享，1：已接受分享，2：拒绝分享，3：取消分享
     * created_at	string	创建分享时间 （UTC时间）
     * updated_at	string	分享状态更新时间 （UTC时间）
     * expired_at	string	分享超时时间 （UTC时间）
     * 注：
     * <p>
     * 若 sharing_type 为 0，表示以 Owner 身份查询自己发出的分享邀请，返回结果中用户信息为 Guest 的信息
     * 若 sharing_type 为 1，表示以 Guest 身份查询收到的分享邀请，返回结果中的用户信息为 Owner 的信息
     *
     * @param sharingType 分享类型，0：我分享的设备，1：分享给我的设备
     * @param status      分享状态，0：未接受分享，1：已接受分享，2：拒绝分享，3：取消分享
     * @param did         指定的设备did，只有owner用户可查询指定did的分享信息
     * @param limit       回的条数，默认20
     * @param skip        跳过的条数，默认0
     * @return json
     */
    @GetMapping("getShare/{sharing_type}")
    public Result getShare(@PathVariable("sharing_type") Integer sharingType,
                           @Param("status") String status,
                           @Param("did") String did,
                           @Param("limit") String limit,
                           @Param("skip") String skip) {
        if (sharingType != 0 && sharingType != 1) {
            return new Result().setError("分享类型不能为空");
        }
        String[] strings = status.split(",");
        StringBuilder staBuilder = new StringBuilder();
        for (String s : strings) {
            staBuilder.append("%2C").append(s);
        }
        staBuilder.replace(0, 3, "");
        Map<String, String> map = getMap();
        JSONObject jsonObject = HttpUtil.getSSL("https://api.gizwits.com/app/sharing?sharing_type=" + sharingType +
                (!staBuilder.equals("") ? ("&status=" + staBuilder) : "") +
                (did != null ? "&skip=" + did : "") +
                (limit != null ? "&limit=" + limit : "&limit=20") +
                (skip != null ? "&skip=" + skip : "&skip=0"), map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * 取消/收回分享
     * 二维码分享的设备无法收回
     * DELETE
     * https://api.gizwits.com/app/sharing/{id}
     * <p>
     * 请求参数                 	类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * id	                    integer	是	path	分享记录id
     * <p>
     * 响应参数	类型	    描述
     * id	    integer	分享记录id
     *
     * @param id 分享记录id
     * @return json
     */
    @DeleteMapping("removeShare/{id}")
    public Result removeShare(@PathVariable("id") Integer id) {
        Map<String, String> map = getMap();
        JSONObject jsonObject = HttpUtil.DeleteSSL("https://api.gizwits.com/app/sharing/" + id, map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * 接受/拒绝分享
     * 二维码分享的设备无法收回
     * PUT
     * https://api.gizwits.com/app/sharing/{id}
     * <p>
     * 请求参数	                类型   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * id	                    integer	是	path	分享记录id
     * status	                integer	是	query	接受/拒绝分享，1：接受分享，2：拒绝分享
     * <p>
     * 响应参数	类型	    描述
     * id	    integer	分享记录id
     *
     * @param id     分享记录id
     * @param status 是否接受  1:接受分享,2:拒绝分享
     * @return json
     */
    @PutMapping("putShare/{id}")
    public Result putShare(@PathVariable("id") Integer id,
                           @Param("status") Integer status) {
        JSONObject jsonObject = null;
        Map<String, String> map = getMap();
        if (status == 1 || status == 2) {
            jsonObject = HttpUtil.putSSL("https://api.gizwits.com/app/sharing/" + id + "?status=" + status, map, null);
        } else {
            return new Result().setError("状态码错误");
        }
        return new Result().setData(jsonObject);
    }

    /**
     * 获取二维码分享信息
     * 设备拥有者 owner 无法使用此接口
     * GET
     * https://api.gizwits.com/app/sharing/code/{code}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * code                 	integer	是	path	二维码分享的qr_content
     * <p>
     * 响应参数	    类型	    描述
     * owner	    string	设备拥有者信息，手机号/邮箱/用户名/uid
     * product_name	string	产品名称
     * dev_alias	string	设备别名
     * expired_at	string	分享超时时间（UTC时间）
     *
     * @param code 二维码字符串
     * @return json
     */
    @GetMapping("getCode/{code}")
    public Result getCode(@PathVariable("code") String code) {
        Map<String, String> map = getMap();
        JSONObject jsonObject = HttpUtil.getSSL("https://api.gizwits.com/app/sharing/code/" + code, map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * 接受二维码分享邀请
     * 设备拥有者 owner 无法使用此接口
     * POST
     * https://api.gizwits.com/app/sharing/code/{code}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token
     * code                     integer	是	path	二维码分享的qr_content
     * <p>
     * 响应参数
     * 无
     *
     * @param code 二维码字符串
     * @return json
     */
    @PostMapping("postCode/{code}")
    public Result postCode(@PathVariable("code") String code) {
        Map<String, String> map = getMap();
        JSONObject jsonObject = HttpUtil.postSSL("https://api.gizwits.com/app/sharing/code/" + code, map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改用户备注信息
     * PUT
     * https://api.gizwits.com/app/sharing/{id}/alias?user_alias={user_alias}
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * id	                    integer	是	path	分享记录id
     * user_alias	            string	是	query   新的备注信息
     * <p>
     * 响应参数
     * 无
     *
     * @param id        用户id
     * @param userAlias 备注
     * @return json
     */
    @PutMapping("putAlias/{id}")
    public Result putAlias(@PathVariable("id") Integer id, @Param("userAlias") String userAlias) {
        if (userAlias == null || userAlias.equals("")) {
            return new Result().setError("备注信息不能为空");
        }
        Map<String, String> map = getMap();
        System.out.println("https://api.gizwits.com/app/sharing/" + id + "/alias?user_alias=" + userAlias);
        JSONObject jsonObject = HttpUtil.putSSL("https://api.gizwits.com/app/sharing/" + id + "/alias?user_alias=" + userAlias, map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * owner 权限转移
     * POST
     * https://api.gizwits.com/app/sharing/1/transfer?uid=1
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * did	                    string	是	path    设备ID
     * uid                  	string	是	query	需要成为owner用户的ID
     * <p>
     * 响应参数
     * 无
     *
     * @param did owner的did
     * @param uid 转移目标guest的uid
     * @return json
     */
    @PostMapping("postTransfer/{did}")
    public Result postTransfer(@PathVariable("did") String did, @Param("uid") String uid) {
        if (uid == null || uid.equals("")) {
            return new Result().setError("请设置需要成为owner用户的ID");
        }
        Map<String, String> map = getMap();
        JSONObject jsonObject = HttpUtil.postSSL("https://api.gizwits.com/app/sharing/" + did + "/transfer?uid=" + uid, map, null);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取消息头
     * @return 消息头封装map
     */
    public Map<String, String> getMap() {
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        String appid = String.valueOf(redisUtil.get("GWappid"));
        Map<String, String> map1 = new HashMap<>();
        map1.put("X-Gizwits-Application-Id", appid);
        map1.put("X-Gizwits-User-token", token);
        return map1;
    }

}
