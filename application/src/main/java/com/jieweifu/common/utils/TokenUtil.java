package com.jieweifu.common.utils;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.admin.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token验证工具集
 */
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

        claims.put(UserConstant.USER_ID, userId);
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
            String redisUserId = String.format("_%s_%s", UserConstant.USER_ID, userId);
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

                String userId = body.get(UserConstant.USER_ID).toString();
                String redisUserId = String.format("_%s_%s", UserConstant.USER_ID, userId);
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

            String userId = body.get(UserConstant.USER_ID).toString();
            String redisUserId = String.format("_%s_%s", UserConstant.USER_ID, userId);
            if (redisUtil.hasKey(redisUserId) && redisUtil.get(redisUserId).toString().equalsIgnoreCase(token)) {
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
        User user = userService.getUserById(userId);
        BaseContextHandler.setUser(user);
        BaseContextHandler.setUserIsAdmin(userService.getIsAdmin(userId));
    }

    @SuppressWarnings("unchecked")
    public boolean checkAuthorization(String path, String method) {
        int userId = BaseContextHandler.getUserId();
        boolean isAdmin = BaseContextHandler.getUserIsAdmin();
        List<Element> elements;
        String elementUserKey = String.format("_%s_%s", UserConstant.USER_ELEMENTS, userId);
        if (redisUtil.hasKey(elementUserKey)) {
            elements = (List<Element>) redisUtil.get(elementUserKey);
        } else {
            elements = userService.getAllAuthElements(userId, isAdmin);
            redisUtil.setEX(elementUserKey, elements, timeout, TimeUnit.MINUTES);
        }
        return elements
                .stream()
                .filter(p -> new AntPathMatcher().match(p.getPath(), path) &&
                        p.getMethod().equalsIgnoreCase(method))
                .count() > 0;
    }

    public void refreshAuthorization(int userId, boolean isAdmin) {
        new Thread(() -> {
            List<Element> elements = userService.getAllAuthElements(userId, isAdmin);
            String elementUserKey = String.format("_%s_%s", UserConstant.USER_ELEMENTS, userId);
            redisUtil.setEX(elementUserKey, elements, timeout, TimeUnit.MINUTES);
        }).start();
    }
}
