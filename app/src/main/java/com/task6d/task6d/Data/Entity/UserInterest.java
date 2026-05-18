package com.task6d.task6d.Data.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(
        foreignKeys = {@ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"
        ), @ForeignKey(
                entity = Interest.class,
                parentColumns = "id",
                childColumns = "interestId"
        )},
        primaryKeys = {"userId", "interestId"})
public class UserInterest {
    @NotNull
    public int userId;
    @NotNull
    public int interestId;
}
