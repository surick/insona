package com.jieweifu.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 陶Lyn
 * on 2018/3/14.
 */
@Service
public class TokenIdUtil {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisUtil redisUtil;
    //通过编码后的tokenid 获得token
    public  int getUserId(String headToken){
        String a=(String)redisUtil.get(headToken);
        if(a==null||a==""){
            return -1;
        }
        String b=tokenUtil.getUserId(a);
        return Integer.parseInt(b);
    }
}
