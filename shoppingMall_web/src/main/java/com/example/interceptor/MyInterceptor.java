package com.example.interceptor;

import com.example.domain.User;
import com.example.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @类名 MyInterceptor
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/16 13:39
 * @版本 1.0
 */
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
        if (ObjectUtils.allNull(user)) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
