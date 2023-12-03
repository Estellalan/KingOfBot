package com.kob.backend1.consumer.utils;

import com.kob.backend1.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {
    public  static Integer getUserId(String token){
        //核心代码:将token解析，如果能解析出用户id,即为合法
        Integer userId=-1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
