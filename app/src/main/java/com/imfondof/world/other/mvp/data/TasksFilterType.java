package com.imfondof.world.other.mvp.data;

/**
 * Imfondof on 2019/12/31 9:49
 * description:
 */
public enum TasksFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}
