package com.cmput301w18t07.taskasker;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by lucasgauk on 2018-03-12.
 */

public class SearchController {
    private String url;
    private Gson gson;
    private RequestController rc;

    public SearchController(String url){
        this.gson = new Gson();
        this.rc = new RequestController();
        this.url = url;
    }

    /*
    Put a task into the elasticsearch cloud.
    Takes: Task
    Returns: void
     */
    public void saveTask(Task task){
        String stringTask = gson.toJson(task);
        this.rc.putRequest(this.url+"/task/",stringTask);
    }
    public void getTaskByName(String name){
        this.rc.getRequest(this.url+"/task/"+"_search?size=10&q="+name);
    }
    /*
    Put a new user into the elasticsearch cloud
    Takes: User
    Returns: void
     */
    public void saveUser(User user){
        String stringUser = gson.toJson(user);
    }

    /*
    Class to help facilitate HTTP requests.
     */
    private class RequestController extends AsyncTask<String, Integer, String> {
        @Override
        // surl convention 0: url 1: method 2: json object
        protected String doInBackground(String... surl){
            // Set URL
            URL url = null;
            try {
                url = new URL(surl[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // Create new connection
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Set request method (POST, PUT, GET, DELETE) surl[1]
            try {
                connection.setRequestMethod(surl[1]);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // Try connecting
            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // If we are making a post request
            if(surl[1].equals("POST")) {
                OutputStreamWriter out = null;
                try {
                    out = new OutputStreamWriter(
                            connection.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Write json if necessary
                try {
                    out.write(surl[2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Create input reader
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Read response from server and return full response
            String content = "", line;
            try {
                while ((line = in.readLine()) != null) {
                    content += line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
        @Override
        protected void onPostExecute(String result) {
        }
        private void putRequest(String url, String json){
            String[] request = {url,"POST",json};
            String response = this.doInBackground(request);
        }
        private String getRequest(String url){
            String[] request = {url,"GET",null};
            String response = this.doInBackground(request);
            return response;
        }
    }
    /*
    Class to parse response - Takes form of responses.
     */
    private class HttpResponse {
        public String _source;
        public String _index;
    }
}
