package com.ruoyi.taskList.util;

import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfoUtil {
    public static Long getUserId(){
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
}
