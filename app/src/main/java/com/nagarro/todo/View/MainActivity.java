package com.nagarro.todo.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.nagarro.todo.ViewModel.MainViewModel;
import com.nagarro.todo.ViewModel.SingletonNameViewModelFactory;
import com.nagarro.todo.data.model.TaskAdapter;
import com.nagarro.todo.R;
import com.nagarro.todo.data.model.DataManager;
import com.nagarro.todo.data.model.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    TextView txtview;
    List<Task> tasks  ;
    RecyclerView recyclerView;
    TextView taskDetail;
    private MainViewModel mainViewModel;
    Intent intent2;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout mainDrawer;
    NavigationView navigationView;
    private static final String TAG = "MyActivity";
    RecyclerView.Adapter madapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    Handler handler = new Handler();
    ConnectivityManager myConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainDrawer = (DrawerLayout) findViewById(R.id.mainDrawer);
        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.todoList);
        taskDetail = findViewById(R.id.taskDetail);
        mainViewModel = ViewModelProviders.of(this, new SingletonNameViewModelFactory(getApplication())).get(MainViewModel.class);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpViews();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.drawerSort) {
                    mainViewModel.moveDoneTaskUp();
                    mShimmerViewContainer.startShimmerAnimation();
                    mShimmerViewContainer.setVisibility(VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }
                    },2000);
                } else if(id == R.id.darkMode) {
                    Toast.makeText(MainActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                } else if(id == R.id.drawerAnalyse) {
                    Toast.makeText(MainActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.drawerNotification) {
                    Toast.makeText(MainActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                }
                mainDrawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        if(!mainViewModel.isConnected() && DataManager.getInstance().getTaskList() == null) {
            intent2 = new Intent(getApplicationContext(), NetworkErrorActivity.class);
            startActivity(intent2);
        }else if(!mainViewModel.isConnected() && DataManager.getInstance().getTaskList() != null){
            mainViewModel.getTasks().observe(this,tasks->{
                int count = 0;
                for(int i= 0;i<tasks.size();i++){
                    if(tasks.get(i).isCompleted()){
                        count ++;
                    }
                }
                setCount(count,tasks.size());
                setUpRecyclerView(tasks);
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            },3000);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(VISIBLE);
        if(mainViewModel.isConnected() && DataManager.getInstance().getTaskList()==null){
            getData();
        }else if(DataManager.getInstance().getTaskList()!=null){
            setCount(intCount(),tasks.size());
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        },3000);
    }

    public int intCount(){
        int count = 0;
        tasks = DataManager.getInstance().getTaskList();
        for(int i= 0;i<tasks.size();i++){
            if(tasks.get(i).isCompleted()){
                count ++;
            }
        }
        return count;
    }

    public void getData(){
        if(mainViewModel.isConnected()) {
            mainViewModel.getAllTask().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(List<Task> tasks) {
                    if (tasks == null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                intent2 = new Intent(getApplicationContext(), NetworkErrorActivity.class);
                                startActivity(intent2);
                            }
                        }, 3000);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int count = 0;
                                for (int i = 0; i < tasks.size(); i++) {
                                    if (tasks.get(i).isCompleted()) {
                                        count++;
                                    }
                                }
                                setCount(count, tasks.size());
                                setUpRecyclerView(tasks);
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000);
                    }

                }
            });
        }
    }

    public void setUpRecyclerView(List<Task> tasks){
        DataManager.getInstance().setTaskList(tasks);
        madapter = new TaskAdapter(MainActivity.this,tasks, new TaskAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("com.nagarro.todo.View.MainActivity.position",position);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(madapter);
    }

    public void setCount(int count,int total){

        mainViewModel.getCount(count).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                taskDetail.setText(Integer.toString(integer)+"/"+Integer.toString(total));
            }
        });
    }

    public void setUpViews(){
        setUpDrawerLayout();
    }

    public void setUpDrawerLayout(){
        setSupportActionBar(findViewById(R.id.appBar));

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mainDrawer,findViewById(R.id.appBar), 0,0);
        mainDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        switch (item.getItemId()) {
            case R.id.refresh:
                mShimmerViewContainer.startShimmerAnimation();
                mShimmerViewContainer.setVisibility(VISIBLE);
                if (mainViewModel.isConnected()){
                    mainViewModel.refresh();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }
                    },3000);
                }else{
                    if(DataManager.getInstance().getTaskList()!=null){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                            }
                        },3000);
                    }else{
                        Intent intent2 = new Intent(getApplicationContext(),NetworkErrorActivity.class);
                        startActivity(intent2);
                        Toast.makeText(MainActivity.this, "ERROR PAGE WILL BE LOADED", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case R.id.moveUp:
                recyclerView.scrollToPosition(0);
                return true;
            case R.id.moveDown:
                recyclerView.scrollToPosition(madapter.getItemCount()-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}