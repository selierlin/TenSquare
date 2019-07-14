package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * pre ：可以在请求被路由之前调用
     * route ：在路由请求时候被调用
     * post ：在route和error过滤器之后被调用
     * error ：处理请求时发生错误时被调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级为0，数字越大，优先级别越低
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器
     * true表示需要过滤
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器执行的操作
     * return任何对象都表示继续执行
     * setSendzuulResponse(false) 可导致不再继续执行
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过后台过滤器了");
        //获取request对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //排除OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return null;
        }
        //排除登录请求
        String url = request.getRequestURL().toString();
        if (url.indexOf("login") > 0) {
            System.out.println("登录页面 = " + url);
            return null;
        }
        //获取header
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header) && header.startsWith("Bearer ")) {
            //头信息转发
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                if ("admin".equals(claims.get("roles"))) {
                    currentContext.addZuulRequestHeader("Authorization", header);
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //终止运行
                currentContext.setSendZuulResponse(false);
            }
        }
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("无权访问");
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");
        return null;
    }
}
