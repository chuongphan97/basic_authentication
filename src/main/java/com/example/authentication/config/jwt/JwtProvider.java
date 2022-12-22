package com.example.authentication.config.jwt;


import com.example.authentication.config.security.UserPrincipal;
import com.example.authentication.enums.TokenEnum;
import com.example.authentication.utils.constants.APIConstants;
import com.example.authentication.utils.constants.Constants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public boolean validateJwtToken(String authToken) throws ServletException {
        try {
            if (isTokenExpired(authToken)) {
                throw new ServletException(APIConstants.ERROR_JWT_TOKEN_EXPIRED);
            }
            Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new ServletException(APIConstants.ERROR_JWT_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ServletException(APIConstants.ERROR_JWT_TOKEN_EXPIRED);
        }
    }

    public AccessToken createAccessToken(Authentication authentication, boolean rememberMe, boolean isAdminApi) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String name = String.valueOf(principal.getUserId());
        long now = (new Date()).getTime();
        long dateToMilliseconds = 24 * 60 * 60 * 1000L;
        Date validity;
        Date refreshTokenExpiration = new Date(now + TokenEnum.REFRESH_TOKEN_EXPIRED.getValue() * dateToMilliseconds);
        if (rememberMe) {
            validity = new Date(now + TokenEnum.TOKEN_REMEMBER_ME_EXPIRED.getValue() * dateToMilliseconds);
        } else {
            validity = new Date(now + TokenEnum.TOKEN_JWT_EXPIRED.getValue() * dateToMilliseconds);
        }
        //Build access token
        String jwt = Jwts.builder().setSubject(name)
                .setClaims(buildClaims(principal, isAdminApi))
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret).compact();

        //Build refresh token
        String refreshToken = Jwts.builder().setSubject(name)
                .setExpiration(refreshTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(jwt);
        accessToken.setExpired(validity);
        accessToken.setRefreshToken(refreshToken);
        accessToken.setUserId(principal.getUserId());
        accessToken.setTokenType(Constants.JWT_TOKEN_TYPE);
        return accessToken;
    }

    /**
     * Get subject from input token
     *
     * @param token access token
     * @return subject
     */
    public Integer getSubjectFromToken(String token) {
        return Integer.valueOf(Jwts.parser()
                .setSigningKey(this.jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    /**
     * build Claims
     *
     * @param principal User Principal
     * @return
     */
    private Map<String, Object> buildClaims(UserPrincipal principal, boolean isAdminApi) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", principal.getUserId());
        claims.put(Constants.JWT_CLAIM_KEY_IS_ADMIN_API, isAdminApi);
        return claims;
    }

    /**
     * Get Claim info from access token value.
     *
     * @param token    the access token
     * @param claimKey Claim key
     * @return Claims info for claimKey
     */
    public Object getClaimInfo(String token, String claimKey) throws BadRequestException {
        Claims claims = Jwts.parser().
                setSigningKey(this.jwtSecret).
                parseClaimsJws(token).
                getBody();
        if (claims.get(claimKey) == null) {
            if (!Constants.JWT_CLAIM_KEY_IS_ADMIN_API.equals(claimKey)) {
                throw new BadRequestException(APIConstants.ERROR_INVALID_TOKEN);
            } else {
                return null;
            }
        }
        return claims.get(claimKey);
    }

    /**
     * Is Token Expired
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String createAuthTokenForgotPassword(String email) {
        long now = (new Date()).getTime();
        long dateToMilliseconds = 24 * 60 * 60 * 1000L;
        Date validity = new Date(now + TokenEnum.TOKEN_REMEMBER_ME_EXPIRED.getValue() * dateToMilliseconds);
        return Jwts.builder().setSubject(email)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
    }
}