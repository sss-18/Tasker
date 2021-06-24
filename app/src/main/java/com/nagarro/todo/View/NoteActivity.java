package com.nagarro.todo.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nagarro.todo.R;
import com.nagarro.todo.data.model.DataManager;
import com.nagarro.todo.data.model.Task;

public class NoteActivity extends AppCompatActivity {

    private static final int POSITION_NOT_SET = -1;
    private static final String NOTE_POSITION = "com.nagarro.todo.View.MainActivity.position";

    EditText mtaskTitle;
    TextView mtaskStatus;
    ImageView taskStatusDoneIcon;
    ImageButton deleteButton;

    private Task mtask;
    private String persisttitle ;
    private boolean persiststatus;

    private int mposition ;
    private boolean isCancel = false;
    private static final String TAG = "NoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        readDisplayStateValues();
        Log.i(TAG, "onCreate: Inside oncreate of NoteActivity");
        mtaskTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                taskStatusDoneIcon.setImageDrawable(getResources().getDrawable(getIconAndSetStatus()));
            }
            private int getIconAndSetStatus() {
                if (mtask.isCompleted()) {
                    mtask.setCompleted(false);
                    mtaskStatus.setText("NOT COMPLETED");
                    return R.drawable.ic_baseline_check_24;
                } else {
                    mtask.setCompleted(true);
                    mtaskStatus.setText("COMPLETED");
                    return R.drawable.ic_done;
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel= true;
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTask();
    }

    private void saveTask() {
        switch (mtaskStatus.getText().toString()) {

            case "COMPLETED":
                DataManager.getInstance().getTaskList().get(mposition).setCompleted(true);
                break;

            case "NOT COMPLETED": DataManager.getInstance().getTaskList().get(mposition).setCompleted(false);
                break;
        }
        if(!isCancel){
            DataManager.getInstance().getTask(mposition).setTitle(mtaskTitle.getText().toString());
        }else{
            DataManager.getInstance().setTask(mposition, persisttitle, persiststatus);
        }
    }

    private void setUpViews() {
        setUpContent();
    }

    public void setUpContent() {
        mtaskTitle = findViewById(R.id.taskTitle);
        mtaskStatus = findViewById(R.id.taskStatus);
        taskStatusDoneIcon = findViewById(R.id.taskStatusDoneIcon);
        mtaskTitle.setText(mtask.getTitle());
        deleteButton = findViewById(R.id.deleteButton);
        if (mtask.isCompleted()) {
            mtaskStatus.setText("COMPLETED");
            taskStatusDoneIcon.setImageDrawable(ContextCompat.getDrawable(NoteActivity.this, R.drawable.ic_done));
        } else {
            mtaskStatus.setText("NOT COMPLETED");
            taskStatusDoneIcon.setImageDrawable(ContextCompat.getDrawable(NoteActivity.this, R.drawable.ic_baseline_check_24));
        }
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mposition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mtask = DataManager.getInstance().getTask(mposition);
        persiststatus = mtask.isCompleted();
        persisttitle = mtask.getTitle();

        setUpViews();
    }
}