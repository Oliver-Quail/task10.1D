package com.example.task6d.Data.DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task6d.Data.Entity.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void createTask(Task task);


    @Query("SELECT * FROM task")
    List<Task> getTasks();

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    Task getTaskById(int taskId);

    @Query("SELECT * FROM task WHERE assignedTo = :id")
    List<Task> getUserTasks(int id);


}
