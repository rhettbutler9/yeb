package com.xxxx.server.config.security.Compoment;


import com.xxxx.server.service.IAdminService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {


    private static final  String CLAIN_KEY_USERNAME="sub";
    private static final  String CLAIN_KEY_CREATED="created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     *根据用户信息生成Token
     *
     */
    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims=new HashMap<>();
        claims.put(CLAIN_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIN_KEY_CREATED,new Date());
        return  generateToken(claims);
    }

    /**
     * 从Token获取登录用户名
     * @param token
     * @return
     */
    public  String getUserNameFromToken(String token){
        String username;
        try {
            //从token中获取荷载
            Claims claims= getClaimsFromToken(token);
            //获取用户名
            username = claims.getSubject();
        } catch (Exception e) {
            username=null;
        }
        return username;
    }

    /**
     * 验证token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public  boolean validateToken(String token,UserDetails userDetails)
    {
        String username =getUserNameFromToken(token);
        //从token中获取的名字是否与传过来的相同,并且token未过期
        return  username.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public  boolean canRefresh(String token)
    {
        return  !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public  String refreshToekn(String token)
    {
        Claims claims=getClaimsFromToken(token);
        claims.put(CLAIN_KEY_CREATED,new Date());
        return  generateToken(claims);
    }



    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expireDate=getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取当前失效时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims=getClaimsFromToken(token);
        return  claims.getExpiration();
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims =null;
        try {
            //jwt 解析
            claims= Jwts.parser()
                    //设置密钥
                    .setSigningKey(secret)
                    //根据token解析
                    .parseClaimsJws(token)
                    //获取主体
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  claims;
    }


    /**
     * 根据荷载生成JWT　Token
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims) {
        return Jwts.builder()
                //设置荷载
                .setClaims(claims)
                //设置过期时间
                .setExpiration(generateExpirationDate())
                //算法和密钥
                .signWith(SignatureAlgorithm.HS512,secret)
                //签发
                .compact();
    }

    /**
     * 生成Token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        return  new Date(System.currentTimeMillis()+expiration*1000);
    }


}
