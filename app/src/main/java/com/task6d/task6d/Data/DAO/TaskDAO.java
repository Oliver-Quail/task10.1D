package com.task6d.task6d.Data.DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.task6d.task6d.Data.Entity.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void createTask(Task task);


    @Query("SELECT * FROM task")
    List<Task> getTasks();

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    Task getTaskById(int taskId);

    @Query("SELECT * FROM task WHERE assignedTo = :id AND isComplete != true")
    List<Task> getUserTasks(int id);

    @Query("SELECT * FROM task WHERE assignedTo = :id AND isComplete = true")
    List<Task> getCompletedUserTasks(int id);

    @Query("SELECT taskId FROM task WHERE assignedTo = :id")
    List<Integer> getUserTasksId(int id);

    @Update
    void updateTask(Task task);

}
