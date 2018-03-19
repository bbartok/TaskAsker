/* All Tasks
 *
 * March 2018
 *
 * Copyright (c) 2018 Brendan Bartok, Christopher Wood, Dylan Alcock, Lucas Gauk, Thomas Mackay,
 * Tyler Strembitsky, CMPUT301, University of Alberta - All Rights Reserved. You may use,
 * distribute, or modify this code under terms and conditions of the Code of Student Behaviour
 *  at University of Alberta. You can find a copy of the license on this project.
 */

package com.cmput301w18t07.taskasker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * Purpose:
 *
 * Design Rationale:
 *
 * @author Tyler
 * @version 1.5
 * @see Task
 */
public class AllTasksActivity extends AppCompatActivity {


    /**
     * Purpose:
     * Sets the view when all tasks sub activity is started.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
    }
}
