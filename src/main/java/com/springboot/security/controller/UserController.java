package com.springboot.security.controller;

import com.springboot.security.bean.Msg;
import com.springboot.security.bean.User;
import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/13 -- 16:55
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    //检查用户名
    @ResponseBody
    @GetMapping("/checkUsername")
    public Msg checkUsername(String username){
        System.out.println(username);
        String regex="(^[a-zA-Z0-9]{4,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
        if(!username.matches(regex)){
            return Msg.fail().add("vn_msg", "用户名格式错误！");
        }
        boolean b = userService.checkUsername(username);
        if(b){
            return Msg.success().add("vn_msg", "用户名可用！");

        }else{
            return Msg.fail().add("vn_msg", "用户名不可用！");
        }
    }

    //检查邮箱
    @ResponseBody
    @GetMapping("/checkEmail")
    public Msg checkEmail(String email){
        System.out.println(email);
        String regex="^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        if(!email.matches(regex)){
            return Msg.fail().add("ve_msg", "邮箱格式错误！");
        }
        boolean b = userService.checkEmail(email);
        if(b){
            return Msg.success().add("ve_msg", "邮箱可用！");

        }else{
            return Msg.fail().add("ve_msg", "邮箱不可用！");
        }
    }


    @GetMapping("/register_p")
    //@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String register_p() {
        System.out.println("register_p");
        return "register";
    }


    //注册用户
    @PostMapping("/register")
    public ModelAndView register(@Valid User user, BindingResult bindingResult, HttpServletRequest request, Model model){//@Valid与BindingResult联合使用 验证提交User类的是否符合字段注解
        Map<String, Object> map =new HashMap<String, Object>();
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError:fieldErrors){
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            model.addAttribute("error",Msg.fail().add("error",map));
            model.addAttribute("user",user);

            return new ModelAndView("register");//如果出现错误字段，添加进Msg中返回
        }else {
            userService.insertUser(user,request);
            return new ModelAndView("");
        }

    }





    @RequestMapping(value = "/login_p", method = RequestMethod.GET)
    public ModelAndView login_p(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "不正确的用户名和密码");
        }
        if (logout != null) {
            model.addObject("msg", "你已经成功退出");
        }
        model.setViewName("login");
        return model;
    }


    @RequestMapping("/active")
    public String active(){
        return "active";
    }

    @ResponseBody
    @RequestMapping("/index")
    public String index(){
        return "active";
    }
//    @Autowired
//    private AuthenticationManager authenticationManager;

//    @RequestMapping(value="/login")
//    public ModelAndView login(@RequestParam(defaultValue="") String username,
//                                          @RequestParam(defaultValue="") String password,
//                                          HttpServletRequest request){
//        //
//        username = username.trim();
//
//        //返回登录页面
//        ModelAndView model = new ModelAndView();
//        model.setViewName("login");
//
//        if(username==null||username.isEmpty()||
//                password==null||password.isEmpty())
//        {
//            return model;
//        }
//
//        //向AJAX请求返回消息提醒(json字符串)
//        model.setViewName("index");
//        UsernamePasswordAuthenticationToken authRequest =
//                new UsernamePasswordAuthenticationToken(username, password);
//        //
//        try {
//            Authentication authentication = authenticationManager.authenticate(authRequest);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            HttpSession session = request.getSession();
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
//
//            model.addObject("message","登录用户："+authentication.getName());
//            model.addObject("ok",1);//这样view/customLogin.jsp得到成功标记后可以做url跳转。
//        } catch (AuthenticationException ex) {
//            model.addObject("message","用户名或密码错误");
//            model.addObject("ok",0);//为了view/customLogin.jsp得到失败标记后可以提醒用户重新输入用户名、密码。
//        }//end catch
//        return model;
//    }//end handler
}



