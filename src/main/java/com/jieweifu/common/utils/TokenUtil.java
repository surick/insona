package com.jieweifu.common.utils;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.models.admin.UserModel;
import com.jieweifu.services.admin.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Service
public class TokenUtil {

    @Value("${custom.jwt.expiry}")
    private int timeout;

    @Value("${custom.jwt.pri-key.path}")
    private String privateKey;

    @Value("${custom.jwt.pub-key.path}")
    private String publicKey;

    private RedisUtil redisUtil;
    private UserService userService;

    private static RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();

    @Autowired
    public TokenUtil(RedisUtil redisUtil, UserService userService) {
        this.redisUtil = redisUtil;
        this.userService = userService;
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public String generateToken(String userId) {
        Claims claims = Jwts.claims();

        claims.put(CommonConstant.USER_ID, userId);
        claims.put(CommonConstant.UUID, UUID.randomUUID());
        claims.put(CommonConstant.DATE, Instant.now());

        String token = null;
        try {
            token = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.RS256, rsaKeyUtil.getPrivateKey(privateKey))
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (token != null) {
            //将token存到数据库, 并设置过期时间, 此操作会覆盖之前的token, 只允许一个账号有且只有一个客户端登录后台管理系统
            String redisUserId = String.format("_%s_%s", CommonConstant.USER_ID, userId);
            redisUtil.setEX(redisUserId, token, 120, TimeUnit.MINUTES);
        }
        return token;
    }

    /**
     * 删除Token
     */
    public void deleteToken(String token) {
        if (token != null && !token.equals("")) {
            try {
                Claims body = Jwts.parser()
                        .setSigningKey(rsaKeyUtil.getPublicKey(publicKey))
                        .parseClaimsJws(token)
                        .getBody();

                String userId = body.get(CommonConstant.USER_ID).toString();
                String redisUserId = String.format("_%s_%s", CommonConstant.USER_ID, userId);
                redisUtil.delete(redisUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取用户id
     */
    public String getUserId(String token) {
        if (token == null || token.length() == 0) {
            return null;
        }
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(rsaKeyUtil.getPublicKey(publicKey))
                    .parseClaimsJws(token)
                    .getBody();

            String userId = body.get(CommonConstant.USER_ID).toString();
            String redisUserId = String.format("_%s_%s", CommonConstant.USER_ID, userId);
            if (redisUtil.hasKey(redisUserId) && redisUtil.get(redisUserId).equalsIgnoreCase(token)) {
                //如果存在token并且数据库存在该用户, 则刷新token过期时间
                redisUtil.expiry(redisUserId, timeout, TimeUnit.MINUTES);
                return userId;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cacheUserInThreadLocal(int userId) {
        UserModel userModel = userService.getUserById(userId);
        BaseContextHandler.setUser(userModel);
        BaseContextHandler.setUserIsAdmin(userService.getIsAdmin(userId));
    }

    public boolean checkAuthorization(String path, String method) {
        return true;
    }
}
