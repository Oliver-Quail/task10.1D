package com.example.task6d;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.Interest;
import com.example.task6d.Data.Entity.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppDatabase db = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();


        if(db.interestDAO().getInterests().size() == 0) {
            String[] interests = getResources().getStringArray(R.array.interests);

            for(String interest : interests) {
                Interest newInterest = new Interest();
                newInterest.setName(interest);

                db.interestDAO().createInterest(newInterest);
            }


        }




    }
}