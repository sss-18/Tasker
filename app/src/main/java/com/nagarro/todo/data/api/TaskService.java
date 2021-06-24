package com.nagarro.todo.data.api;

import com.nagarro.todo.data.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET("todos")
    Call<List<Task>> getTaskList();
}
