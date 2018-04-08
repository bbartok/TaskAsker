/*
 * Copyright (c) 2018 Brendan Bartok, Christopher Wood, Dylan Alcock, Lucas Gauk, Thomas Mackay,
 * Tyler Strembitsky, CMPUT301, University of Alberta - All Rights Reserved. You may use,
 * distribute, or modify this code under terms and conditions of the Code of Student Behaviour
 *  at University of Alberta. You can find a copy of the license on this project.
 */

package com.cmput301w18t07.taskasker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Purpose:
 * Activity that displays the details of a task
 *
 * Design Rationale:
 * Having a way to display the information from a task in a user friendly way.
 *
 * @author Dylan
 * @version 1.5
 * @see Task
 */
public class MyTaskDetailsActivity extends AppCompatActivity {

    private String url = "http://cmput301.softwareprocess.es:8080/cmput301w18t07";
    private SearchController controller = new SearchController(url);
    private EditText username;
    private TextView errorMessage;
    private MyTaskDetailsActivity activity = this;
    private User check = null;
    Task task;
    private ConnectivityManager cm;
    private ArrayList<Bitmap> imageFolder = new ArrayList<>();
    private ArrayList<String> base64Folder = new ArrayList<>();
    private int arrayIndex = 0;


    /**
     * Purpose:
     * Sets up the view when the task details activity is started
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_task_details);

        final int taskID = getIntent().getIntExtra("task ID", 0);

        task = controller.getTaskById(taskID);
        //final int index = getIntent().getIntExtra("Index", -1);

        base64Folder = task.getImageFolder();
        for(int i = 0; i < base64Folder.size(); i++){
            String base64Image = base64Folder.get(i);
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageFolder.add(i, bm);
        }

        final Button doneButton = findViewById(R.id.doneTaskButton);
        final Button deleteButton = findViewById(R.id.deleteTaskButton);
        final Button editButton = findViewById(R.id.editTaskButton);
        final Button lastPhoto = findViewById(R.id.lastPhoto);
        final Button nextPhoto = findViewById(R.id.nextPhoto);

        lastPhoto.setText("<");
        nextPhoto.setText(">");


        final TextView title = findViewById(R.id.title);
        final TextView status = findViewById(R.id.status);
        final TextView lowestBid = findViewById(R.id.lowestbid);
        final TextView description = findViewById(R.id.description);
        final TextView output = findViewById(R.id.output);

        final ImageView imageView = findViewById(R.id.imageView);

        if(imageFolder.isEmpty()){
            Toast.makeText(getApplicationContext(), "No Picture Found", Toast.LENGTH_LONG).show();
        }else{
            imageView.setImageBitmap(imageFolder.get(0));
        }

        if (!task.getStatus().equals("Assigned")) {
            doneButton.setText("View Bids");
        }
        else {
            doneButton.setText("Mark as Done");
        }


        title.setText(task.getName());
        status.setText(task.getStatus());
        lowestBid.setText("$" + String.format("%.2f", task.getLowestBid()));
        description.setText(task.getDescription());

        nextPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayIndex < imageFolder.size() - 1){
                    arrayIndex++;
                    imageView.setImageBitmap(imageFolder.get(arrayIndex));
                }else{
                    arrayIndex = 0;
                    imageView.setImageBitmap(imageFolder.get(arrayIndex));
                }

            }
        });

        lastPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayIndex > 0){
                    arrayIndex--;
                    imageView.setImageBitmap(imageFolder.get(arrayIndex));
                }else{
                    arrayIndex = imageFolder.size() - 1;
                    imageView.setImageBitmap(imageFolder.get(arrayIndex));
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doneButton.getText().equals("View Bids")){
                    Intent intent = new Intent(activity, AcceptBidActivity.class);
                    intent.putExtra("taskID", taskID);
                    startActivity(intent);
                }
                else {
                    task.setStatus("Done");
                    controller.updateTask(task);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditTask.class);
                intent.putExtra("task ID", taskID);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            controller.deleteTaskById(taskID);
            setResult(RESULT_OK);
            finish();
            }
        });


    }
}
