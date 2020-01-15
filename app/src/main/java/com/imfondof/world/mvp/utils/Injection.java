package com.imfondof.world.mvp.utils;

/**
 * Imfondof on 2019/12/31 14:21
 * description:
 */

import android.content.Context;

import androidx.annotation.NonNull;

import com.imfondof.world.mvp.data.Task;
import com.imfondof.world.mvp.data.TasksDataSource;
import com.imfondof.world.mvp.data.TasksRepository;

import static androidx.core.util.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        TasksRepository tasksDataSource=TasksRepository.getInstance(new TasksDataSource() {
            @Override
            public void getTasks(@NonNull LoadTasksCallback callback) {

            }

            @Override
            public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

            }

            @Override
            public void saveTask(@NonNull Task task) {

            }

            @Override
            public void completeTask(@NonNull Task task) {

            }

            @Override
            public void completeTask(@NonNull String taskId) {

            }

            @Override
            public void activateTask(@NonNull Task task) {

            }

            @Override
            public void activateTask(@NonNull String taskId) {

            }

            @Override
            public void clearCompletedTasks() {

            }

            @Override
            public void refreshTasks() {

            }

            @Override
            public void deleteAllTasks() {

            }

            @Override
            public void deleteTask(@NonNull String taskId) {

            }
        }, new TasksDataSource() {
            @Override
            public void getTasks(@NonNull LoadTasksCallback callback) {

            }

            @Override
            public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

            }

            @Override
            public void saveTask(@NonNull Task task) {

            }

            @Override
            public void completeTask(@NonNull Task task) {

            }

            @Override
            public void completeTask(@NonNull String taskId) {

            }

            @Override
            public void activateTask(@NonNull Task task) {

            }

            @Override
            public void activateTask(@NonNull String taskId) {

            }

            @Override
            public void clearCompletedTasks() {

            }

            @Override
            public void refreshTasks() {

            }

            @Override
            public void deleteAllTasks() {

            }

            @Override
            public void deleteTask(@NonNull String taskId) {

            }
        });
        return tasksDataSource;
    }
}

