package com.nagarro.todo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class SingletonNameViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    MainViewModel mainViewModel;
    Application application;
    public SingletonNameViewModelFactory(Application application) {
        //  t = provideNameViewModelSomeHowUsingDependencyInjection
        this.application = application;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new  MainViewModel(application);
    }
}
