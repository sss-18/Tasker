package com.nagarro.todo.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskApi {
    private static final String url = "https://jsonplaceholder.typicode.com/";
    public static TaskService taskService = null;

    public static TaskService getService(){
        if(taskService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            taskService = (TaskService) retrofit.create(TaskService.class);

        }
        return taskService;
    }
}
