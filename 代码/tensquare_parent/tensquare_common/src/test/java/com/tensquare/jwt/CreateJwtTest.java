package com.tensquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwtTest {
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        long exp = now + 1000 * 60;
        JwtBuilder builder = Jwts.builder().setId("111")
                .setSubject("小明")
                .setExpiration(new Date(exp))//设置过期时间
                .setIssuedAt(new Date())//设置签发时间
                .signWith(SignatureAlgorithm.HS256, "itcast");//设置签名秘钥
        System.out.println(builder.compact());
    }
}
