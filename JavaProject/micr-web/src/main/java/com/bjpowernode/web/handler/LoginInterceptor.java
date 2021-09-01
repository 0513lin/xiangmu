package com.bjpowernode.web.handler;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.vo.ResultObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //判断是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
        System.out.println("======LoginInterceptor perHandler="+user);
        if( user == null){
            //区分ajax和普通有视图页面的请求
            String ajaxHeader  = request.getHeader("X-Requested-With");
            if("XMLHttpRequest".equals(ajaxHeader)){
                //ajax请求，需要返还数据
                ResultObject ro  =  new ResultObject(20000,"需要登录","");
                PrintWriter out = response.getWriter();
                // ro 转为就 json
                out.println(JSONObject.toJSONString(ro));
                out.flush();
                out.close();

            } else {
                //没有登录， 重定向到登录页面
                response.sendRedirect( request.getContextPath() + "/loan/page/login");
            }
            return false;
        }
        return true;
    }
}
