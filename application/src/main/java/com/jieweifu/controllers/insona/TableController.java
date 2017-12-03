package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.Table;
import com.jieweifu.models.regex.Regex;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.services.insona.TableService;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@SuppressWarnings("unused")
@RestController("InsonaTable")
@RequestMapping("insona/table")
public class TableController {

    private TableService tableService;
    private UserService userService;

    @Autowired
    public TableController(TableService tableService,
                           UserService userService) {
        this.tableService = tableService;
        this.userService = userService;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("getUser")
    public Result getUser() {
        User user = BaseContextHandler.getUser();
        System.out.println(user.getId());
        return new Result().setData(user);
    }

    /**
     * 修改当前用户密码
     */
    @PutMapping("putPassword")
    public Result putPassword(@Valid @RequestBody PassInfo passInfo, Errors errors) {
        System.out.println(passInfo.password+"  "+passInfo.newPassword);
        String password = passInfo.getPassword();
        String newPassword = passInfo.getNewPassword();
        User user = BaseContextHandler.getUser();
        System.out.println(user.getId());
        if (!userService.doUserLogin(user.getId(), password))
            return new Result().setError("原密码错误");
        if (!password.matches(Regex.PASSWORD_REX))
            return new Result().setError("新密码格式错误");
        String salt = UUID.randomUUID().toString().replace("-", "");
        user.setPassword(DigestUtils.md5Hex(salt + newPassword));
        user.setIsFirst(true);
        user.setSalt(salt);
        userService.updateUser(user);
        return new Result().setMessage("修改成功");
    }

    /**
     * 获取表信息
     */
    @GetMapping("listTables")
    public Result listTables() {
        return new Result().setData(tableService.listTables());
    }

    /**
     * 获取地图信息
     */
    @GetMapping("listMap")
    public Result listMap() {
        List<Table> tables = tableService.listTables();
        int max = 0;
        List<Data> list = new ArrayList<>();
        for (Table table : tables) {
            Data data = new Data();
            data.setName(table.getCity());
            data.setValue(table.getNormalProduct());
            list.add(data);
            max += table.getNormalProduct();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("max", max);
        return new Result().setData(map);
    }

    public static class PassInfo {
        @NotBlank(message = "旧密码不能为空")
        private String password;
        @NotBlank(message = "新密码不能为空")
        private String newPassword;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    class Data {
        private String name;
        private Integer value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "name:'" + name + '\'' +
                    ", value:" + value +
                    '}';
        }
    }
}
