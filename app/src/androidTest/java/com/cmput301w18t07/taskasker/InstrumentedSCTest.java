package com.cmput301w18t07.taskasker;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by lucasgauk on 2018-03-14.
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedSCTest {
    String url = "http://cmput301.softwareprocess.es:8080/cmput301w18t07";
    @Test
    public void createTest(){
        SearchController controller = new SearchController(url);
        assertNotNull(controller);
    }
    @Test
    public void putTaskTest(){
        SearchController controller = new SearchController(url);
        Task task = new Task("Test");
        controller.saveTask(task);
    }
    @Test
    public void putUserTest(){
        SearchController controller = new SearchController(url);
        User user = new User("Test2");
        controller.saveUser(user);
    }
    @Test
    public void getUserTest(){
        SearchController controller = new SearchController(url);
        User user = new User("Test2");
        controller.saveUser(user);
        sleep2();
        assertEquals("Test2",controller.getUserByUsername("Test2").getUsername());
        controller.deleteUserByUsername("Test2");
        sleep2();
        assertEquals(null,controller.getUserByUsername("Test2"));
    }
    @Test
    public void deleteAllUserTest(){
        SearchController controller = new SearchController(url);
        User user1 = new User("Bill");
        User user2 = new User("Jill");
        controller.saveUser(user1);
        controller.saveUser(user2);
        sleep2();
        controller.deleteAllUsers();
        assertEquals(null,controller.getUserByUsername("Bill"));
        assertEquals(null,controller.getUserByUsername("Jill"));
    }
    @Test
    public void searchAllTasks(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        User user = new User("Bill");
        try {
            Task task1 = new Task("Hello", "Fuck you", user);
            Task task2 = new Task("Jelly", "Bing bong", user);
            Task task3 = new Task("Plink1", "132", user);
            controller.saveUser(user);
            sleep2();
            controller.saveTask(task1);
            sleep2();
            controller.saveTask(task2);
            sleep2();
            controller.saveTask(task3);
            sleep2();
            ArrayList<Task> taskList = controller.getOpenTasks("Bing CRAP");
            assertEquals(1,taskList.size());
            assertEquals("Jelly",taskList.get(0).getName());
            sleep2();
            taskList = controller.getOpenTasks("Bing link");
            assertEquals(2,taskList.size());
            assertEquals("Jelly",taskList.get(0).getName());
            assertEquals("Plink1",taskList.get(1).getName());
            sleep2();
            taskList = controller.getOpenTasks("Bing link hell");
            assertEquals(3,taskList.size());
            assertEquals("Hello",taskList.get(0).getName());
            assertEquals("Plink1",taskList.get(2).getName());
            sleep2();
            taskList = controller.getOpenTasks("");
            assertEquals(3,taskList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void updateTaskTest(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        Task task1 = new Task("Task 1");
        controller.saveTask(task1);
        sleep2();
        Task testTask = controller.getTaskById(task1.getTaskID());
        assertEquals(testTask.getName(),"Task 1");
        task1.addBid(new Bid(new User("Bill"),100));
        controller.updateTask(task1);
        sleep2();
        testTask = controller.getTaskById(task1.getTaskID());
        assertEquals(testTask.getBidList().get(0).getBid(),100.0);
    }
    @Test
    public void getTasksByRequester(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        User user1 = new User("Bill");
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");
        task1.setRequester(user1);
        task2.setRequester(user1);
        controller.saveTask(task1);
        controller.saveTask(task2);
        controller.saveTask(task3);
        sleep2();
        ArrayList<Task> taskList = controller.getTaskByRequester(user1.getUsername());
        assertEquals("Task 1",taskList.get(0).getName());
        assertEquals("Task 2",taskList.get(1).getName());
    }
    @Test
    public void getTasksByRequesterWithStatus(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        User user1 = new User("Bill");
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");
        task1.setRequester(user1);
        task2.setRequester(user1);
        task2.setStatus("FINDME");
        controller.saveTask(task1);
        controller.saveTask(task2);
        controller.saveTask(task3);
        sleep2();
        ArrayList<Task> taskList = controller.getTaskByRequester(user1.getUsername(),"FINDME");
        assertEquals("Task 2",taskList.get(0).getName());
    }
    @Test
    public void getTasksByTaker(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        User user1 = new User("Bill");
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");
        task3.setTaker(user1);
        task1.setTaker(user1);
        controller.saveTask(task1);
        controller.saveTask(task2);
        controller.saveTask(task3);
        sleep2();
        ArrayList<Task> taskList = controller.getTaskByTaker(user1.getUsername());
        assertEquals("Task 1",taskList.get(0).getName());
        assertEquals("Task 3",taskList.get(1).getName());
    }
    @Test
    public void getTaskByID(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");
        task1.setTaskID(1);
        task2.setTaskID(2);
        task3.setTaskID(3);
        controller.saveTask(task1);
        controller.saveTask(task2);
        controller.saveTask(task3);
        sleep2();
        assertEquals("Task 1",controller.getTaskById(1).getName());
        assertEquals("Task 3",controller.getTaskById(3).getName());
        assertEquals("Task 2",controller.getTaskById(2).getName());
    }
    @Test
    public void getMaxTaskID(){
        SearchController controller = new SearchController(url);
        controller.deleteAllTasks();
        sleep2();
        assertEquals(1,controller.getMaxTaskId());
        Task task1 = new Task("Task 1");
        task1.setTaskID(controller.getMaxTaskId());
        controller.saveTask(task1);
        sleep2();
        assertEquals(2,controller.getMaxTaskId());
        Task task2 = new Task("Task 2");
        task2.setTaskID(14);
        controller.saveTask(task2);
        sleep2();
        assertEquals(15,controller.getMaxTaskId());
    }
    @Test
    public void deleteTask(){
        SearchController controller = new SearchController(url);
        Task deleteTask1 = new Task("Delete Me");
        deleteTask1.setTaskID(69);
        controller.saveTask(deleteTask1);
        sleep2();
        controller.deleteTaskById(69);
        sleep2();
        assertNull(controller.getTaskById(69));
    }
    @Test
    public void getUnassignedTasks(){
        SearchController controller = new SearchController(url);
        User user1 = new User("Tom");
        controller.deleteAllTasks();
        sleep2();
        controller.saveUser(user1);
        Task task1 = new Task("Test task 1");
        Task task2 = new Task("Test task 2");
        Task task3 = new Task("Test task 3");
        task1.setTaker(user1);
        task2.setTaker(user1);
        controller.saveTask(task1);
        sleep2();
        controller.saveTask(task2);
        sleep2();
        controller.saveTask(task3);
        sleep2();
        ArrayList<Task> openTasks = controller.getOpenTasks();
        assertEquals(1, openTasks.size());
        assertEquals(openTasks.get(0).getName(),"Test task 3");
    }
    @Test
    public void getTasksByBidder(){
        SearchController controller = new SearchController(url);
        User user1 = new User("Tom");
        User user2 = new User("Jimmy");
        controller.deleteAllTasks();
        sleep2();
        controller.saveUser(user1);
        controller.saveUser(user2);
        Task task1 = new Task("Test task 1");
        sleep2();
        Task task2 = new Task("Test task 2");
        sleep2();
        Task task3 = new Task("Test task 3");
        sleep2();
        Task task4 = new Task("Test task 4");
        sleep2();
        Bid bid1 = new Bid(user1, 100);
        Bid bid2 = new Bid(user1, 200);
        Bid bid3 = new Bid(user2, 69);
        Bid bid4 = new Bid(user2, 240);
        task1.addBid(bid1);
        task2.addBid(bid3);
        task3.addBid(bid2);
        task3.addBid(bid4);
        controller.saveTask(task1);
        sleep2();
        task2.setTaskID(controller.getMaxTaskId());
        controller.saveTask(task2);
        sleep2();
        task3.setTaskID(controller.getMaxTaskId());
        controller.saveTask(task3);
        sleep2();
        task4.setTaskID(controller.getMaxTaskId());
        controller.saveTask(task4);
        sleep2();
        ArrayList<Task> biddedTasks = controller.getTaskByBidder("Tom");
        assertEquals(2, biddedTasks.size());
        assertEquals(biddedTasks.get(0).getName(),"Test task 1");
        assertEquals(biddedTasks.get(1).getName(),"Test task 3");
        task4.addBid(bid1);
        controller.updateTask(task4);
        sleep2();
        biddedTasks = controller.getTaskByBidder("Tom");
        assertEquals(3, biddedTasks.size());
        assertEquals(biddedTasks.get(0).getName(),"Test task 1");
        assertEquals(biddedTasks.get(1).getName(),"Test task 4");
        assertEquals(biddedTasks.get(2).getName(),"Test task 3");
        sleep2();
        biddedTasks = controller.getTaskByBidder("Jimmy");
        assertEquals(2, biddedTasks.size());
        assertEquals(biddedTasks.get(0).getName(),"Test task 2");
        assertEquals(biddedTasks.get(1).getName(),"Test task 3");
    }
    /*Helper function*/
    private void sleep2(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
