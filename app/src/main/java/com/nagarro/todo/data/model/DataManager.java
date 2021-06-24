package com.nagarro.todo.data.model;

import java.util.List;

public class DataManager {
    private static DataManager ourInst = null;
    private List<Task> taskList;
    public static DataManager getInstance(){
        if(ourInst == null){
            ourInst = new DataManager();
        }
        return ourInst;
    }

    public Task getTask(int position){
        return this.taskList.get(position);
    }

    public void setTaskList(List<Task> taskList){
        this.taskList = taskList;
    }
    public List<Task> getTaskList(){
        return this.taskList;
    }


    public void setTask(int mposition, String title, boolean status) {
        this.taskList.get(mposition).setCompleted(status);
        this.taskList.get(mposition).setTitle(title);
    }

}
