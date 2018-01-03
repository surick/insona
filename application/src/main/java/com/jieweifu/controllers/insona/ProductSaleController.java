package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductSale;
import com.jieweifu.services.insona.ProductSaleService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaProductSale")
@RequestMapping("insona/productSale")
public class ProductSaleController {

    private ProductSaleService productSaleService;

    @Autowired
    public ProductSaleController(ProductSaleService productSaleService) {
        this.productSaleService = productSaleService;
    }

    /**
     * 审核通过
     */
    @PostMapping("pass")
    public Result passProduct(@RequestBody List<String> ids) {
        if (ids.isEmpty()) {
            return new Result().setError("id不合法");
        }
        try {
            String id = ids.get(0);
            productSaleService.passProduct(Integer.parseInt(id));
            Product product = productSaleService.getProduct(Integer.parseInt(id));
            ProductSale productSale = new ProductSale();
            productSale.setDid(product.getDid());
            productSale.setName(product.getName());
            productSale.setGizwit_info(product.getGizwit_info());
            productSale.setSerial_code(product.getSerial_code());
            productSale.setVersion(product.getVersion());
            productSale.setStatus(product.getStatus());
            productSale.setExtract(product.getExtract());
            productSale.setSub_maker(product.getSub_maker());
            productSale.setSub_inter(product.getSub_inter());
            productSale.setType(product.getType());

            productSaleService.saveProduct(productSale);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setMessage("审核通过");
    }

    /**
     * 退回
     */
    @PutMapping("back")
    public Result backProduct(@RequestBody BackReason reason) {
        System.out.println("============: " + reason.getId());
        try {
            productSaleService.backProduct(Integer.parseInt(reason.getId()), reason.getText());
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setMessage("退回成功");
    }

    /**
     * 修改设备信息
     */
    @PutMapping("update")
    public Result updateProduct(@RequestBody ProductSale productSale) {
        try {
            productSaleService.updateProduct(productSale);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setMessage("修改成功");
    }

    /**
     * 获取设备
     */
    @GetMapping("getList/{pageIndex}/{pageSize}")
    public Result getList(@PathVariable("pageIndex") int pageIndex,
                          @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 1) {
            return new Result().setError("目录错误，请重新选页");
        }
        List<ProductSale> list;
        int total = 0;
        Map<String, Object> map = new HashMap<>();
        try {
            list = productSaleService.getList(pageIndex, pageSize);
            total = productSaleService.total();
            map.put("list", list);
            map.put("total", total);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setData(map);
    }

    /**
     * 获取待审核设备
     */
    @GetMapping("getPass/{pageIndex}/{pageSize}")
    public Result getPass(@PathVariable("pageIndex") int pageIndex,
                          @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 1) {
            return new Result().setError("目录错误，请重新选页");
        }
        int userId = BaseContextHandler.getUserId();
        List<Product> list;
        int total = 0;
        Map<String, Object> map = new HashMap<>();
        try {
            list = productSaleService.getPass(pageIndex, pageSize, userId);
            total = productSaleService.passTotal();
            map.put("list", list);
            map.put("total", total);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setData(map);
    }

    /**
     * 获取退回设备
     */
    @GetMapping("getBack/{pageIndex}/{pageSize}")
    public Result getBack(@PathVariable("pageIndex") int pageIndex,
                          @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 1) {
            return new Result().setError("目录错误，请重新选页");
        }
        List<Product> list;
        int total = 0;
        Map<String, Object> map = new HashMap<>();
        try {
            list = productSaleService.getBack(pageIndex, pageSize);
            total = productSaleService.backTotal();
            map.put("list", list);
            map.put("total", total);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请稍后重试");
        }
        return new Result().setData(map);
    }

    /**
     * 退回类
     */
    public static class BackReason {

        private String id;

        private String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
