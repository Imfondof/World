package com.imfondof.world.mvp;

import androidx.annotation.NonNull;

import com.imfondof.world.mvp.base.BasePresenter;
import com.imfondof.world.mvp.base.BaseView;
import com.imfondof.world.mvp.data.Task;
import com.imfondof.world.mvp.data.TasksFilterType;

import java.util.List;

/**
 * zhaishuo on 2019/12/30 17:59
 * description:
 */
public interface TasksContract {
    interface View extends BaseView<Presenter> {
        void showAddTask();

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {
        void addNewTask();

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void openTaskDetails(@NonNull Task requestedTask);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }
}
