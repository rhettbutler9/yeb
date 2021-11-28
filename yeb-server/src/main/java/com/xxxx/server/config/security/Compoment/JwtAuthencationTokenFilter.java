package com.xxxx.server.config.security.Compoment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt 登录授权过滤器
 */
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private  String tokenHeader;
    @Value("${jwt.tokenHead}")
    private  String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * authHeader:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2MjAyMjUyNTY4MTksImV4cCI6MTYyMDgzMD
         * A1Nn0.t3Nq7gVOwYAmUDXQu9RtZdyTvyV_MJmyQdMC1Y_PmjQFs_FuxWDLDX6wXhbetF9HitD1XesEekZwVREgO4ATrA
         */

        /**
         * authToken: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2MjAyMjUyNTY4MTksImV4cCI6MTYyMDgzMDA1Nn0.t
         * 3Nq7gVOwYAmUDXQu9RtZdyTvyV_MJmyQdMC1Y_PmjQFs_FuxWDLDX6wXhbetF9HitD1XesEekZwVREgO4ATrA
         */
        String authHeader = request.getHeader(tokenHeader);
        if(null!=authHeader&&authHeader.startsWith(tokenHead)){
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            //token存在,用户名未登录
            if(null!=username&&null== SecurityContextHolder.getContext().getAuthentication()){
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //如果token有效,重新设置用户对象
                if(jwtTokenUtil.validateToken(authToken ,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
