package com.ccl.wrok.accumulated.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 拿到当前用户
 */
public class CurrentUserUtil {
    private static final String DEFAULT_MEDIA_TYPE = "application/json; charset=UTF-8";
    private static final String X_AUTHENTICATED_USERNAME = "X-Authenticated-Username";
    private static final String ROOT_USER = "root";
    private CurrentUserUtil(){}

    /**
     * 获取当前会话用户ID
     * 网关会将用户信息放到header中，此时如果拿不到用户信息，说明服务没有走网关。
     * @return
     */
    public static String getCurrentUserId(){
        Object userId = getHeader("X-Authenticated-Userid");
        return userId==null?null:String.valueOf(userId);
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        return String.valueOf(getHeader("token"));
    }

    public static Object getHeader(String name){
        RequestAttributes holder = RequestContextHolder.getRequestAttributes();
        if(holder!=null){
            HttpServletRequest request = ((ServletRequestAttributes)holder).getRequest();
            //先找cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equalsIgnoreCase(name)) {
                        return cookie.getValue();
                    }
                }
            }
            //再找header
            return request.getHeader(name);
        }
        return null;
    }

    /**
     * 是否是超级管理员
     * @return
     */
    public static boolean isSuperAdmin(){
        RequestAttributes holder = RequestContextHolder.getRequestAttributes();
        if(holder!=null){
            String userName =  ((ServletRequestAttributes)holder).getRequest().getHeader(X_AUTHENTICATED_USERNAME);
            return StringUtils.isNotBlank(userName)&&ROOT_USER.equals(userName.trim());
        }
        return false;
    }

    /**
     * 不验证权限
     * 当前当超级管理员或者没有用户信息的时候，不验证权限
     * @return
     */
    public static boolean isNotValidRight(){
        return isSuperAdmin()|| StringUtils.isBlank(getCurrentUserId());
    }

    /**
     * 增加用户验证相关的
     * @param headers
     * @return
     */
    public static HttpHeaders putPermissionHeaders(HttpHeaders headers){
        headers.setContentType(MediaType.parseMediaType(DEFAULT_MEDIA_TYPE));
        headers.add(X_AUTHENTICATED_USERNAME,String.valueOf(CurrentUserUtil.getHeader(X_AUTHENTICATED_USERNAME)));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("X-Authenticated-Userid", CurrentUserUtil.getCurrentUserId());
        headers.add("token", CurrentUserUtil.getToken());
        return headers;
    }
}
