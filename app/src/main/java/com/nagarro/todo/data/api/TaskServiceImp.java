package com.nagarro.todo.data.api;

import com.nagarro.todo.data.model.Task;

import java.util.List;

import retrofit2.Call;

public class TaskServiceImp implements TaskService{
    @Override
    public Call<List<Task>> getTaskList() {
        return  TaskApi.getService().getTaskList();
    }
}
