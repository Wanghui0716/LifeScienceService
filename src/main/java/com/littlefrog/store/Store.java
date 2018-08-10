package com.littlefrog.store;


import com.littlefrog.entity.Course;
import com.littlefrog.entity.Lesson;
import com.littlefrog.entity.Post;
import com.littlefrog.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    private static final int MAX_SIZE = 100;
    private static Map<Integer, User> userStore = new HashMap<Integer, User>(MAX_SIZE);
    private static Map<Integer, ArrayList<Course>> courseStore = new HashMap<Integer, ArrayList<Course>>();
    private static Map<Integer,ArrayList<Lesson>> lessonStore = new HashMap<Integer, ArrayList<Lesson>>();

    public User getById(int id) {
        return userStore.getOrDefault(id, null);
    }

    public void addUser(User user) {
        if (!userStore.containsValue(user)) {
            if (userStore.size() > MAX_SIZE) {
                userStore.clear();
            }
            userStore.put(user.getId(), user);
        }
    }

    public void updateUserInfo(int id, User user) {
        if (userStore.containsKey(id)) {
            userStore.replace(id, user);
        } else {
            this.addUser(user);
        }
    }

    /**
     * 由于post数量问题，重新设置最大值
     */
    private static final int MAX_SIZE_FOR_POST = 20;
    private static Map<Integer, ArrayList<Post>> postStore = new HashMap<>(MAX_SIZE_FOR_POST);

    public ArrayList<Post> searchPostInCache(int courseID) {
        return postStore.getOrDefault(courseID, null);
    }

    public void addToPostCache(ArrayList<Post> arrayList) {
        if (!postStore.containsValue(arrayList) && arrayList.size() != 0) {
            if (postStore.size() > MAX_SIZE_FOR_POST) {
                postStore.clear();
            }
            postStore.put(arrayList.get(0).getCourseID(), arrayList);
        }
    }



    public void addNewToPostCache(Post p) {
        if (postStore.containsKey(p.getCourseID())) {
            ArrayList<Post> arrayList = postStore.get(p.getCourseID());
            arrayList.add(p);
            //postStore.put(arrayList.get(0).getCourseID(), arrayList);
        }
    }

    public void updatePostCache(Post prePost, Post newPost) {
        if (postStore.containsKey(prePost.getCourseID())) {
            ArrayList<Post> l = postStore.get(prePost.getCourseID());
            if (l.contains(prePost)) {
                l.set(l.indexOf(prePost), newPost);
            }
            //postStore.put(l.get(0).getCourseID(), l);
        }
    }

    public void removePostInCache(Post post) {
        if (postStore.containsKey(post.getCourseID())) {
            ArrayList<Post> l = postStore.get(post.getCourseID());
            l.remove(post);
            //postStore.put(l.get(0).getCourseID(), l);
        }
    }

    public static Map<Integer, ArrayList<Post>> getPostStore() {
        return new HashMap<>(postStore);
    }

    public List<Course> getCourseList(int id,int index,int offset){
        if (courseStore.containsKey(id)){
            return courseStore.get(id).subList(index,Math.min(index+offset-1,courseStore.get(id).size()));
        }else {
            return null;
        }
    }

    public void addCourseList(int id, List<Course> courseList) {
        if (courseStore.size() > MAX_SIZE) {
            courseStore.clear();
        }
        courseStore.put(id, (ArrayList) courseList);
    }

    public List<Lesson> getLessonList(int id,int index,int offset){
        if (lessonStore.containsKey(id)){
            return lessonStore.get(id).subList(index,Math.min(index+offset-1,lessonStore.get(id).size()));
        }
        else {
            return null;
        }
    }
    public void addLessonList(int id,List<Lesson> lessonList){
        if (lessonStore.size() > MAX_SIZE){
            lessonStore.clear();
        }
        lessonStore.put(id,(ArrayList)lessonList);
    }

}

