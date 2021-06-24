package com.nagarro.todo.data.repository;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.nagarro.todo.data.api.TaskApi;
import com.nagarro.todo.data.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MainRepository<TAG> {
    private  List<Task> tasks ;
    private Task task;
    private MutableLiveData<List<Task>> mutableLiveData = new MutableLiveData<>();
    private Application application;
    private static final String TAG = "Main Repo";

    public MainRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<Task>> getMutableLiveDataTask(){

        Call<List<Task>> taskList = TaskApi.getService().getTaskList();
        taskList.enqueue(new Callback<List<Task>>() {

            @Override
            public void onResponse(retrofit2.Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        tasks = response.body();
                        mutableLiveData.setValue(tasks);
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Task>> call, Throwable t) {
                tasks = null;
            }
        });
        return mutableLiveData;
    }
}
