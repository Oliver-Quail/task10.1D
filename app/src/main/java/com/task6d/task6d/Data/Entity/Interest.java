package com.task6d.task6d.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Interest {
    @PrimaryKey(autoGenerate = true)
    public int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
