package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * authorizeRequests：所有security全注释配置实现的开端，表示开始说明需要的权限。
         * 需要的权限分两部分
         *      1. 拦截的路径
         *      2. 访问该路径需要的权限
         *  antMatchers：表示需要拦截的路径，permitAll表示任何权限都可以访问，直接放行所有路径
         *  anyRequest：任何的请求，authenticated 在认证后才能访问
         *  .and().csrf().disable()：固定写法，表示使csrf拦截失效
         */
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
