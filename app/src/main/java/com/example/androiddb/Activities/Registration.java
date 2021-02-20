package com.example.androiddb.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Entities.App;
import com.example.androiddb.Entities.AppDatabase;
import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;
import com.example.androiddb.R;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    EditText Login, Password, Phone, Email, LastName, FirstName, MiddleName;
    AppDatabase database;
    UsersDao usersDao;
    List<Users> users;
    Thread SecondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Login = findViewById(R.id.edtxtLogin);
        Password = findViewById(R.id.edtxtPassword);
        Phone = findViewById(R.id.edtxtPhone);
        Email = findViewById(R.id.edtxtEmail);
        LastName = findViewById(R.id.edtxtLastName);
        FirstName = findViewById(R.id.edtxtFirstName);
        MiddleName = findViewById(R.id.edtxtMiddleName);

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
    }

    public void RegUser(View view)
    {
        String[] UserAttributes = new String[]
                {
                        Login.getText().toString(),
                        Password.getText().toString(),
                        Phone.getText().toString(),
                        Email.getText().toString(),
                        LastName.getText().toString(),
                        FirstName.getText().toString(),
                        MiddleName.getText().toString(),
                };

        if(!CheckFields(UserAttributes))
            return;

        if(!CheckExcistedUser(UserAttributes[0]))
            return;

        Users user = new Users();
        user.setLogin(Login.getText().toString());
        user.setPassword(Password.getText().toString());
        user.setPhone(Phone.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setLastName(LastName.getText().toString());
        user.setFirstName(FirstName.getText().toString());
        user.setMiddleName(MiddleName.getText().toString());

        Runnable AddUser = () ->
        {
            usersDao.Insert(user);
        };
        SecondThread = new Thread(AddUser);
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onBackPressed();
    }

    boolean CheckFields(String[] UserAttributes)
    {
        String[] EmptyFields = new String[]
                {
                        UserAttributes[0],
                        "Логин",
                        UserAttributes[1],
                        "Пароль",
                        UserAttributes[2],
                        "Телефон",
                        UserAttributes[3],
                        "Электронная почта",
                        UserAttributes[4],
                        "Фамилия",
                        UserAttributes[5],
                        "Имя",
                        UserAttributes[6],
                        "Отчество"
                };

        StringBuilder Fields = new StringBuilder();

        for(int i = 0; i < EmptyFields.length; i += 2)
        {
            if(!EmptyFields[i].equals(""))
                continue;

            if(Fields.length() > 0)
                Fields.append(", ");

            Fields.append(EmptyFields[i + 1]);
        }

        if(Fields.length() == 0)
            return true;

        Toast.makeText(this, "Заполните поля: " + Fields, Toast.LENGTH_SHORT).show();
        return false;
    }

    boolean CheckExcistedUser(String login)
    {
        if(!FindUser(login))
            return true;

        Toast.makeText(this, "Пользователь с таким логином уже существует",
                Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean FindUser(String login) {
        for(int i = 0; i < users.size(); i++)
        {
            if(users.get(i).getLogin().equals(login))
                return true;
        }
        return false;
    }
}