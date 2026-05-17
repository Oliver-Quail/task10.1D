package com.example.task6d.Data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.task6d.Data.DAO.AnswerDAO;
import com.example.task6d.Data.DAO.InterestDAO;
import com.example.task6d.Data.DAO.QuestionDAO;
import com.example.task6d.Data.DAO.TaskDAO;
import com.example.task6d.Data.DAO.UserDAO;
import com.example.task6d.Data.DAO.UserInterestDAO;
import com.example.task6d.Data.Entity.Answer;
import com.example.task6d.Data.Entity.Interest;
import com.example.task6d.Data.Entity.Question;
import com.example.task6d.Data.Entity.Task;
import com.example.task6d.Data.Entity.User;
import com.example.task6d.Data.Entity.UserInterest;

@Database(entities = {User.class, Interest.class, UserInterest.class, Task.class, Answer.class, Question.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();
    public abstract InterestDAO interestDAO();
    public abstract UserInterestDAO userInterestDAO();
    public abstract TaskDAO taskDAO();
    public abstract AnswerDAO answerDAO();
    public abstract QuestionDAO questionDAO();



}
