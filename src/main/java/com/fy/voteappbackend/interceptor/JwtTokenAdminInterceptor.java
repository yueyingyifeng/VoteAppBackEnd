package com.fy.voteappbackend.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.fy.voteappbackend.Tools.JWTUtils;
import com.fy.voteappbackend.context.AdminsContext;
import com.fy.voteappbackend.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {


    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader("token");

        if (token.isEmpty()) {
            return false;
        }

        try {
            if (request.getRequestURI().startsWith("/admin")){

                //解析token获取管理员id
                log.info("管理员jwt校验:{}", token);
                Claim claims = JWTUtils.getTokenInfo(token).getClaim("uid");
                String Id = claims.asString();
                log.info("当前管理员id：{}", Id);
                AdminsContext.setCurrentId(Integer.valueOf(Id));
                //3、通过，放行
                return true;

            }else {

                //解析token获取用户id
                log.info("用户jwt校验:{}", token);
                Claim claims = JWTUtils.getTokenInfo(token).getClaim("uid");
                String uId = claims.asString();
                log.info("当前用户uid：{}", uId);
                UserContext.setCurrentId(Long.valueOf(uId));

                //3、通过，放行
                return true;
            }
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
