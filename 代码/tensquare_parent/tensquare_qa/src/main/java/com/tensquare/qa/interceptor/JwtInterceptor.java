package com.tensquare.qa.interceptor;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过拦截器");
        //只负责把头请求中包含token的令牌进行一个解析验证
        String header = request.getHeader("Authorization");//获取头信息
        System.out.println("header = " + header);
        if (header != null && !"".equals(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String roles = (String) claims.get("roles");
                if (roles != null && roles.equals("admin")) {
                    System.out.println("设置token：" + token);
                    request.setAttribute("claims_admin",token);
                }
                if (roles != null && roles.equals("user")) {
                    request.setAttribute("claims_user",token);
                }
            } catch (Exception e) {
                throw new RuntimeException("令牌不正确！");
            }
        }
        return true;
    }
}
