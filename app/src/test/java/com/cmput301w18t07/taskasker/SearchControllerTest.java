package com.cmput301w18t07.taskasker;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by lucasgauk on 2018-03-12.
 */
public class SearchControllerTest {
    String url = "http://cmput301.softwareprocess.es:8080/cmput301w18t07";
    @Test
    public void createTest(){
        SearchController controller = new SearchController(url);
        assertNotNull(controller);
    }
    @Test
    public void putTaskTest(){
        SearchController controller = new SearchController(url);
        Task task = new Task();
        controller.saveTask(task);
    }
    @Test
    public void putUserTest(){
        SearchController controller = new SearchController(url);
        User user = new User();
        controller.saveUser(user);
    }
}
