package com.example.androiddb.Activities;

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

public class Registration extends AppCompatActivity {
    EditText Login, Password, RepeatPassword, Phone, Email, LastName, FirstName, MiddleName;
    AppDatabase database;
    UsersDao usersDao;
    Users users;
    Thread SecondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Login = findViewById(R.id.edtxtLogin);
        Password = findViewById(R.id.edtxtPassword);
        RepeatPassword = findViewById(R.id.edtxtRepeatPassword);
        Phone = findViewById(R.id.edtxtPhone);
        Email = findViewById(R.id.edtxtEmail);
        LastName = findViewById(R.id.edtxtLastName);
        FirstName = findViewById(R.id.edtxtFirstName);
        MiddleName = findViewById(R.id.edtxtMiddleName);

        Runnable InitDB = () ->
        {
            database = App.getInstance().getDatabase();
            usersDao = database.usersDao();
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
                    Login.getHint().toString(),

                    Password.getText().toString(),
                    Password.getHint().toString(),

                    RepeatPassword.getText().toString(),
                    RepeatPassword.getHint().toString(),

                    Phone.getText().toString(),
                    Phone.getHint().toString(),

                    Email.getText().toString(),
                    Email.getHint().toString(),

                    LastName.getText().toString(),
                    LastName.getHint().toString(),

                    FirstName.getText().toString(),
                    FirstName.getHint().toString(),

                    MiddleName.getText().toString(),
                    MiddleName.getHint().toString()
            };

        if(!CheckFields(UserAttributes))
            return;

        if(!CheckExcistedUser())
            return;

        if(!CheckRepeatPassword(UserAttributes[2], UserAttributes[4]))
            return;

        AddNewUser(UserAttributes);

        GoBack();
    }

    private boolean CheckRepeatPassword(String password, String repeatPassword) {
        if(repeatPassword.equals(password))
            return true;

        Toast.makeText(this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void GoBack() {
        onBackPressed();
    }

    private void AddNewUser(String[] UserAttributes) {
        Users user = new Users
                (
                        UserAttributes[0],
                        UserAttributes[2],
                        UserAttributes[6],
                        UserAttributes[8],
                        UserAttributes[10],
                        UserAttributes[12],
                        UserAttributes[14],
                        "User"
                );

        Runnable AddUser = () ->
            usersDao.Insert(user);

        SecondThread = new Thread(AddUser);
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean CheckFields(String[] UserAttributes)
    {
        StringBuilder Fields = new StringBuilder();

        for (int i = 0; i < UserAttributes.length; i += 2) {
            if (!UserAttributes[i].equals(""))
                continue;

            if (Fields.length() > 0)
                Fields.append(", ");

            Fields.append(UserAttributes[i+1]);
        }

        if(Fields.length() == 0)
        {
            Runnable InitDB = () ->
                users = usersDao.GetByLogin(UserAttributes[0]);

            SecondThread = new Thread(InitDB);
            SecondThread.start();
            return true;
        }

        Toast.makeText(this, "Заполните поля: " + Fields, Toast.LENGTH_SHORT).show();
        return false;
    }

    boolean CheckExcistedUser()
    {
        if(!FindUser())
            return true;

        Toast.makeText(this, "Пользователь с таким логином уже существует",
                Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean FindUser() {
        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return users != null;
    }
}