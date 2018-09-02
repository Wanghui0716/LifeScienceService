package com.littlefrog.controller;


import com.littlefrog.common.Response;
import com.littlefrog.entity.Lesson;
import com.littlefrog.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.littlefrog.common.Response;
import com.littlefrog.common.Tag;
import com.littlefrog.entity.Course;
import com.littlefrog.entity.User;
import com.littlefrog.respository.UserRepository;
import com.littlefrog.service.CourseService;
import com.littlefrog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.littlefrog.common.ResultGenerator.genFailResult;
import static com.littlefrog.common.ResultGenerator.genSuccessResult;

@RestController
@CrossOrigin
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Value("${appid}")
    private String appid;

    private int currentID = 0;

    @GetMapping("api/course/lesson")
    public Response getCourseLessons(@RequestHeader String appid,@RequestParam int courseID,@RequestParam int index,@RequestParam int offset){
        if(!appid.equals(this.appid)){
            return genFailResult("错误的appid");
        }
        List<Lesson> lessonList = lessonService.getLessonsByCourseID(courseID,currentID++,index,offset);
        if (lessonList  == null){
            return genFailResult("获取列表失败");
        } else {
            return genSuccessResult(lessonList);
        }
    }

    @PostMapping("api/lesson/Addlesson")
    public Response AddLesson(@RequestHeader String appid,@RequestParam int courseID,@RequestParam int order,@RequestParam String title,@RequestParam String videoUrl,@RequestParam String description,@RequestParam String coverUrl){
        Lesson lesson = lessonService.AddLesson(courseID,order,title,videoUrl,description,coverUrl);
        if(lesson==null){
            return genFailResult("添加课程失败");
        }else{
            return genSuccessResult(lesson);
        }
    }

    @PostMapping("api/lesson/setLessonInfo")
    public Response setLesson(@RequestHeader String appid,@RequestParam int id,@RequestParam String title,@RequestParam String videoUrl,@RequestParam String description,@RequestParam String coverUrl){
        boolean result=lessonService.setLessonInfo(id,title,videoUrl,description,coverUrl);
        if(!result){
            return genFailResult("设置课程信息失败！");
        }else{
            Optional<Lesson> lesson=lessonService.FindById(id);
            if(!lesson.isPresent()){
                return genFailResult("服务器查找数据失败，请重试");
            }else{
                return genSuccessResult(lesson);
            }
        }
    }

    @PostMapping("api/lesson/setVideoURL")
    public Response setVideoURL(@RequestHeader String appid,@RequestParam int ID,@RequestParam String videoUrl){
        boolean result=lessonService.setVideoUrl(ID,videoUrl);
        if(!result){
            return genFailResult("设置视频url失败！");
        }else{
            Optional<Lesson> lesson=lessonService.FindById(ID);
            if(!lesson.isPresent()){
                return genFailResult("服务器查找数据失败，请重试");
            }else{
                return genSuccessResult(lesson);
            }
        }
    }

    @PostMapping("api/lesson/SetCoverPic")
    public Response SetCoverPic(@RequestHeader String appid,@RequestParam int id,@RequestParam String url){
        boolean result=lessonService.setCoverPic(id,url);
        if(!result){
            return genFailResult("设置封面url失败！");
        }else{
            Optional<Lesson> lesson=lessonService.FindById(id);
            if(!lesson.isPresent()){
                return genFailResult("服务器查找数据失败，请重试");
            }else{
                return genSuccessResult(lesson);
            }
        }
    }
}
