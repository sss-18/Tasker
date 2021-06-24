package com.nagarro.todo.ViewModel;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nagarro.todo.data.model.DataManager;
import com.nagarro.todo.data.model.Task;
import com.nagarro.todo.data.repository.MainRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MainViewModel extends AndroidViewModel {
    private MainRepository mainRepository;
    private List<Task> task = new ArrayList<>();
    Application application;
    ConnectivityManager myConnectivityManager;
    NetworkInfo myNetworkInfo;
    MutableLiveData<List<Task>> mutableTasks=new MutableLiveData<>();
    MutableLiveData<Integer> mutableCount = new MutableLiveData<>();
    private String TAG = "Main View Model";
    private boolean isConnected ;
    private int count = 0;

    public void setCount(int count){
        mutableCount.setValue(count);
    }
    public MutableLiveData<Integer> getCount(int count){
        this.count = count;
        mutableCount.setValue(this.count);
        return mutableCount;
    }

    public boolean isConnected() {
        myConnectivityManager = (ConnectivityManager) application.getSystemService(CONNECTIVITY_SERVICE);
        myNetworkInfo = myConnectivityManager.getActiveNetworkInfo();
        if(myNetworkInfo !=null && myNetworkInfo.isConnected()) {
            isConnected = true;
        }else{
            isConnected = false;
        }
        return isConnected;
    }

    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application = application;
        mainRepository = new MainRepository(application);
        isConnected();
    }
    public MutableLiveData<List<Task>> getAllTask(){

        if(isConnected()){
            if(mutableTasks.getValue() == null){
                Log.i(TAG, "getAllTask: get Value works");
                mutableTasks = mainRepository.getMutableLiveDataTask();
            }
        }
        return mutableTasks;
    }


    public MutableLiveData<List<Task>> getTasks(){
        return mutableTasks;
    }

    public void updateLiveData(List<Task> tasks){
        if(mutableTasks.getValue() == tasks){
            Log.i(TAG, "values are same and running");
        }
        mutableTasks.setValue(tasks);
    }


    public void refresh(){
        mutableTasks = new MutableLiveData<>();
        if(isConnected()){
            Log.i(TAG, "getAllTask: get Value works");
            mutableTasks = mainRepository.getMutableLiveDataTask();

        }
    }

    public void moveDoneTaskUp(){
        task = mutableTasks.getValue();
        Boolean flag;
        Task temp;
        int size = task.size();
        if(task != null){
            for(int i = 0; i<size;i++){
                //flag = task.get(i).isCompleted();
                if(task.get(i).isCompleted()){
                    for(int j = i+1;j<size;j++){
                        if(!task.get(j).isCompleted()){
                            Collections.swap(task,i,j);
                            break;
                        }
                    }
                }
            }
        }
        mutableTasks.setValue(task);
    }




}
