package com.nagarro.todo.data.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nagarro.todo.R;

import java.util.List;

import static com.nagarro.todo.R.drawable.*;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    public interface OnItemClickListner{
        void onItemClick(int position);
    }
    public void setOnItemClickListner(OnItemClickListner listner){
        mlistner = listner;
    }
    @NonNull
    @org.jetbrains.annotations.NotNull
    private static final String TAG = "TaskAdapter";
    List<Task> data;
    private OnItemClickListner mlistner;

    private Context context;

    public TaskAdapter(Context context,List<Task> data, OnItemClickListner listner){
        this.context = context;
        this.data = data;
        this.mlistner = listner;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(view,mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull TaskAdapter.TaskViewHolder holder, int position) {
        String title = data.get(position).getTitle();
        Log.i(TAG, title);
        holder.textTitle.setText(title);
        if (data.get(position).isCompleted()) {
            holder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, ic_done));
        }else{
            holder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,ic_baseline_check_24));
        }
        //holder.imgIcon.;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon ;
        TextView textTitle;
        public TaskViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView,OnItemClickListner listner) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            textTitle = itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        int pos = getAdapterPosition();
                        if(pos!= RecyclerView.NO_POSITION) listner.onItemClick(pos);
                    }
                }
            });
        }


    }
}
