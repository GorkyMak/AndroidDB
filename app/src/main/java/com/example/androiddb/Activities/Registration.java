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

import java.util.List;

public class Registration extends AppCompatActivity {
    EditText Login, Password, RepeatPassword, Phone, Email, LastName, FirstName, MiddleName;
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
        EditText[] UserAttributes = GetArrayEditTexts();

        if(!CheckFields(UserAttributes))
            return;

        if(!CheckExcistedUser(UserAttributes[0].getText().toString()))
            return;

        if(!CheckRepeatPassword(UserAttributes))
            return;

        AddNewUser(UserAttributes);

        GoBack();
    }

    private boolean CheckRepeatPassword(EditText[] userAttributes) {
        if(userAttributes[2].getText().toString().equals(userAttributes[1].getText().toString()))
            return true;

        Toast.makeText(this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void GoBack() {
        onBackPressed();
    }

    private EditText[] GetArrayEditTexts() {
        return new EditText[]
                    {
                            Login,
                            Password,
                            RepeatPassword,
                            Phone,
                            Email,
                            LastName,
                            FirstName,
                            MiddleName
                    };
    }

    private void AddNewUser(EditText[] userAttributes) {
        Users user = new Users();
        user.setLogin(userAttributes[0].getText().toString());
        user.setPassword(userAttributes[1].getText().toString());
        user.setPhone(userAttributes[3].getText().toString());
        user.setEmail(userAttributes[4].getText().toString());
        user.setLastName(userAttributes[5].getText().toString());
        user.setFirstName(userAttributes[6].getText().toString());
        user.setMiddleName(userAttributes[7].getText().toString());

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

    boolean CheckFields(EditText[] UserAttributes)
    {
        StringBuilder Fields = new StringBuilder();

        for (EditText userAttribute : UserAttributes) {
            if (!userAttribute.getText().toString().equals(""))
                continue;

            if (Fields.length() > 0)
                Fields.append(", ");

            Fields.append(userAttribute.getTag().toString());
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