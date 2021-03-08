package com.example.androiddb.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Database.Repository;
import com.example.androiddb.Database.AppDatabase;
import com.example.androiddb.Database.Entities.Users.UsersDao;
import com.example.androiddb.R;

import java.text.MessageFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GeneralUserData extends AppCompatActivity {
    TextView CountUsers;
    AppDatabase database;
    UsersDao usersDao;
    int CountRecords;
    Thread DBThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user_data);

        CountUsers = findViewById(R.id.txtCountUsers);

        GetDB();

        getCount();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void GetDB() {
        DBThread = new Thread(() ->
        {
            database = Repository.getInstance().getDatabase();
            usersDao = database.usersDao();
        });
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getCount() {
        usersDao.GetCount()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        CountRecords = integer;

                        setCountUsers();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ERROR-GetCount", e.getMessage());
                    }
                });
    }

    private void setCountUsers() {
        CountUsers.setText(MessageFormat.format("{0}{1}", CountUsers.getText(), String.valueOf(CountRecords)));
    }
}