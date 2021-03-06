package com.littlefrog.service;

import com.littlefrog.common.Tag;
import com.littlefrog.entity.Course;
import com.littlefrog.respository.CourseRepository;
import com.littlefrog.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("CourseService")
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    private Store store = new Store();

    public List<Course> getCourseByTag(Tag tag,String keyword,int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if ( courseList== null){
            courseList = courseRepository.findCourseByTag(keyword);
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }

    public List<Course> getCourseByTagAndPopularity(Tag tag,String keyword,int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if ( courseList== null){
            courseList=courseRepository.findCourseByTagAndPopularity(tag.ordinal(),keyword);
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }

    public List<Course> getCourseByTagAndReleaseTime(Tag tag,String keyword,int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if ( courseList== null){
            courseList = courseRepository.findCourseByTagAndReleaseTime(tag.ordinal(),keyword);
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }

    public List<Course> getCourseByReleaseTime (String keyword,int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if ( courseList== null){
            courseList=courseRepository.findCourseByReleaseTime(keyword);
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }

    public List<Course> getCourseByPopularity (String keyword,int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if (courseList== null){
            courseList=courseRepository.findCourseByPopularity(keyword);
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }

    public List<Course> getAllCourse (int index,int offset,int id){
        List<Course> courseList=store.getCourseList(id,index,offset);
        if (courseList== null){
            courseList=courseRepository.findAll();
            store.addCourseList(id,courseList);
        }
        try {
            return courseList.subList(index, Math.min(index + offset, courseList.size()));
        }
        catch (IllegalArgumentException i){
            return null;
        }
    }


    public Course addCourse (String location, String name, String teacher, String introduction, int popularity, Tag tag, String coverPic, Date releaseTime, double price, int courseNum){
        Course course = courseRepository.save(new Course(location,name,teacher,introduction,popularity,tag,coverPic,releaseTime,price,courseNum));
        return course;
    }

    public boolean deleteCourse(Integer id){
        try {
            courseRepository.deleteCourse(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public Course findByID(Integer courseID){
       return courseRepository.findByCourseId(courseID);
    }

    public boolean setCourseInfo(Integer id,String location, String name, String teacher, String introduction, int popularity, Tag tag, String cover_pic, double price, int course_num){
        try {
            courseRepository.setCourseInfo(id,location,name,teacher,introduction,popularity,tag.ordinal(),cover_pic,price,course_num);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean setCoverPic(int id, String Url){
        try {
            courseRepository.setCoverPic(id,Url);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
