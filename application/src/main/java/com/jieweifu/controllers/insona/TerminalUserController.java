package com.jieweifu.controllers.insona;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.UserProduct;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.TerminalUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaTerminal")
@RequestMapping("insona/terminal")
public class TerminalUserController {

    private TerminalUserService terminalUserService;
    private ProductService productService;

    public TerminalUserController(TerminalUserService terminalUserService,ProductService productService){
        this.terminalUserService = terminalUserService;
        this.productService = productService;
    }

    /**
     * 分页查询用户
     */
    @GetMapping("listTerminalUser/{pageIndex}/{pageSize}")
    public Result listTerminalUser(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<InsonaUser> userList = terminalUserService.listUser(pageIndex,pageSize);
        int total = terminalUserService.getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", userList);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 根据id查找用户
     */
    @GetMapping("getUserById/{id}")
    public Result getUserById(@PathVariable("id") String id) {
        if (id == null)
            return new Result().setError("查询信息不能为空");
        System.out.println(id);
        InsonaUser user = terminalUserService.getUserById(id);
        if (user == null)
            return new Result().setError("用户不存在");
        return new Result().setData(user);
    }

    /**
     * 根据id查找设备
     */
    @GetMapping("getProductById/{uid}")
    public Result getProductById(@PathVariable("uid") String uid) {
        if (uid == null)
            return new Result().setError("查询信息不能为空");
        System.out.println(uid);
        List<UserProduct> productList = terminalUserService.listProduct(uid);
        List<Product> list = new ArrayList<>();
        for(UserProduct product : productList){
            Product product1 = productService.getByDid(product.getDid());
            list.add(product1);
        }
        if (productList.isEmpty())
            return new Result().setError("设备不存在");
        return new Result().setData(list);
    }
}
