package com.nagarro.todo.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nagarro.todo.data.model.Task;

import java.util.List;

public class TaskList {
    @SerializedName("taskList")
    @Expose
    private List<Task> taskList;
}
