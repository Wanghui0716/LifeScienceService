package com.littlefrog.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlefrog.common.Category;
import com.littlefrog.common.Response;
import com.littlefrog.common.ResultGenerator;
import com.littlefrog.entity.Activity;
import com.littlefrog.entity.ActivityRecord;
import com.littlefrog.entity.Coupon;
import com.littlefrog.entity.User;
import com.littlefrog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.littlefrog.common.ResultGenerator.genFailResult;
import static com.littlefrog.common.ResultGenerator.genSuccessResult;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private InformService informService;
    @Autowired
    private ActivityRecordService activityRecordService;

    @Value("${appID}")
    private String appID;

    @GetMapping("api/user/index")
    public Response index(@RequestHeader String appID){
        if(!appID.equals(this.appID)){
            return genFailResult("错误的appID");
        }
        List<User> u = userService.getAllUser();
        if(u!=null){
            return genSuccessResult(u);
        }else{
            return genFailResult("没有用户");
        }
    }
    @PostMapping("api/user/login")
    public Response addUser(@RequestHeader String appID, @RequestParam String code){
        if(!appID.equals(this.appID)){
            return genFailResult("错误的appID");
        }
       Object user = userService.login(code);
       if(user.getClass() != User.class){
           JSONObject res = JSONObject.parseObject(user.toString());
           String massage = res.getString("errmsg");
           return genFailResult(massage);
       }
       if(user != null){
           return genSuccessResult(user);
       }else{
           return genFailResult("登陆失败，请稍后再试");
       }
    }
    @GetMapping("api/user/info")
    public Response indexforID(@RequestHeader String appID, @RequestParam Integer userID){
        if(!appID.equals(this.appID)){
            return genFailResult("错误的appID");
        }
        User user = userService.getUserInfo(userID);
        if(user != null){
            return genSuccessResult(user);
        }else{
            return genFailResult("该用户不存在");
        }
    }

    @PostMapping("api/user/updateInfo")
    public Response updateInfo(@RequestHeader String appID, @RequestParam int userID,@RequestParam int gender,@RequestParam String name, @RequestParam String phoneNum){
        if(!appID.equals(this.appID)){
            return genFailResult("错误的appID");
        }
        User user=userService.setUserInfo(userID,gender,name,phoneNum);
        if(user ==null){
            return genFailResult("更新失败，没有该用户！");
        }else{
            return genSuccessResult(user);
        }
    }
    @PostMapping("api/user/rechargeResult")
    public Response rechargeResult(@RequestHeader String appID,@RequestParam int userID, @RequestParam int amount,@RequestParam boolean isSuccess){
        if(!appID.equals(this.appID)){
            return genFailResult("错误的appID");
        }
        if(!isSuccess){
            return genSuccessResult();
        }else{
            Activity activity = activityService.findByrequirementAndAmmount(amount,2);
            if(activity==null){
                return genSuccessResult();
            }else{
                try {
                    ActivityRecord activityRecord = activityRecordService.getActivityRecord(userID, activity.getActivityID());
                    if(activityRecord!=null){
                        return ResultGenerator.genSuccessResult("成功");
                    }
                    activityRecordService.addActivityRecord(new ActivityRecord(userID,activity.getActivityID()));
                    Coupon.setLastTime(activity.getCouponExpiry());
                    Coupon mycoupon = couponService.addCoupon(userID, activity.getCoupon(), activity.getActivityID(), activity.getRequirement());
                    informService.addInform(userID,"恭喜您成功获得"+mycoupon.getAmount()+"元优惠券，赶快去使用吧！",Category.SYSTEM,null);
                    return genSuccessResult("成功");
                }catch (Exception e){
                    return genFailResult(e.getMessage());
                }
            }
        }
    }
}
