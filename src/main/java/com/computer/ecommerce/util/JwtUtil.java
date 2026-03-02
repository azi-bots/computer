package com.computer.ecommerce.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT令牌
     *
     * @param username 用户名
     * @param userId   用户ID
     * @param roles    角色列表
     * @return JWT令牌
     */
    public String generateToken(String username, Long userId, String roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roles);
        claims.put("sub", username);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从令牌中获取角色
     *
     * @param token JWT令牌
     * @return 角色
     */
    public String getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roles", String.class);
    }

    /**
     * 获取令牌的过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 验证令牌是否有效
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 从令牌中获取Claims
     *
     * @param token JWT令牌
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查令牌是否即将过期（在指定时间内过期）
     *
     * @param token          JWT令牌
     * @param milliseconds   提前多少毫秒检查
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, long milliseconds) {
        Date expiration = getExpirationDateFromToken(token);
        Date now = new Date();
        return expiration.getTime() - now.getTime() <= milliseconds;
    }

    /**
     * 刷新令牌（生成新令牌，使用相同的用户信息）
     *
     * @param token 旧令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String username = getUsernameFromToken(token);
        Long userId = getUserIdFromToken(token);
        String roles = getRolesFromToken(token);
        return generateToken(username, userId, roles);
    }

    /**
     * 获取配置的请求头名称
     *
     * @return 请求头名称
     */
    public String getHeader() {
        return header;
    }

    /**
     * 获取配置的令牌前缀
     *
     * @return 令牌前缀
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * 从请求头中提取令牌（去掉前缀）
     *
     * @param authHeader 授权请求头
     * @return 令牌或null
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
        }
        return null;
    }
}