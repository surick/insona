package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.ProductDealer;
import com.jieweifu.services.insona.ProductDealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController("InsonaProductDealer")
@RequestMapping("insona/productDealer")
public class ProductDealerController {

    private ProductDealerService productDealerService;

    @Autowired
    public ProductDealerController(ProductDealerService productDealerService){
        this.productDealerService = productDealerService;
    }

    /**
     * 新增
     */
    @PostMapping("save")
    public Result save(@RequestBody ProductDealer productDealer, Errors errors){
        if(errors.hasErrors()){
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try{
            productDealerService.saveProductDealer(productDealer);
        }catch (Exception e){
            return new Result().setError("系统繁忙，请刷新后重新");
        }
        return new Result().setMessage("新增成功");
    }
    /**
     * 删除
     */
    @DeleteMapping("remove")
    public Result remove(@RequestBody List<String> ids){
        if(ids.isEmpty()){
            return new Result().setError("id无效");
        }
        for (String id:
             ids) {
            productDealerService.removeProductDealer(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }
}
