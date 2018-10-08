package com.metacube.sageclarity.predictable.util;

import com.metacube.sageclarity.predictable.entity.User;
import com.metacube.sageclarity.predictable.helper.constant.CommonConstant;
import com.metacube.sageclarity.predictable.vo.UserVO;

import javax.servlet.http.HttpSession;

public class ApplicationUtil {
    public static UserVO getUser(HttpSession session){
        return (UserVO) session.getAttribute(CommonConstant.APP_USER);
    }
}
