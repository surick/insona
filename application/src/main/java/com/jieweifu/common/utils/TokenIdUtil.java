package com.jieweifu.common.utils;

import com.jieweifu.constants.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

    public int  getUserId(HttpServletRequest request){
        String authToken = request.getHeader(CommonConstant.TOKEN_AUTHORIZATION);
        String tokenId= (String) redisUtil.get(authToken);
        if(tokenId==null){
            return -1;
        }
        String id =tokenUtil.getUserId(tokenId);
        return Integer.parseInt(id);
    }


}
