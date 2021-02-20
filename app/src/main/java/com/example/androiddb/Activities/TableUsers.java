package com.example.androiddb.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Entities.App;
import com.example.androiddb.Entities.AppDatabase;
import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;
import com.example.androiddb.R;

import java.util.List;

public class TableUsers extends AppCompatActivity {
    TableLayout Table;
    AppDatabase database;
    UsersDao usersDao;
    List<Users> users;
    Thread SecondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_users);

        Table = findViewById(R.id.Table);

        Runnable InitDB = () ->
        {
            database = App.getInstance().getDatabase();
            usersDao = database.usersDao();

            users = usersDao.GetAll();
        };
        SecondThread = new Thread(InitDB);
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < users.size(); i++)
        {
            TableRow row = new TableRow(this);

            String[] UserAttributes = new String[]
                    {
                            users.get(i).getLogin(),
                            users.get(i).getPassword(),
                            users.get(i).getPhone(),
                            users.get(i).getEmail(),
                            users.get(i).getLastName(),
                            users.get(i).getFirstName(),
                            users.get(i).getMiddleName()
                    };

            for(int j = 0; j < 7; j++)
            {
                Context ThemeContext = new ContextThemeWrapper(this, R.style.TableTextView);

                TextView UserAttribute = new TextView(ThemeContext);
                UserAttribute.setText(UserAttributes[j]);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                        (
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                1
                        );
                layoutParams.setMargins(5,5,5,5);

                UserAttribute.setLayoutParams(layoutParams);
                row.addView(UserAttribute);
            }

            Table.addView(row);
        }
    }
}