package com.example.androiddb.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Database.InstanceDB;
import com.example.androiddb.Database.AppDatabase;
import com.example.androiddb.Database.Entities.Users.Users;
import com.example.androiddb.Database.Entities.Users.UsersDao;
import com.example.androiddb.R;

public class Authorization extends AppCompatActivity {
    EditText Login, Password;
    Button Auth, Reg;
    AppDatabase database;
    UsersDao usersDao;
    Thread DBThread;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        Login = findViewById(R.id.edtxtLogin);
        Password = findViewById(R.id.edtxtPassword);

        Auth = findViewById(R.id.btnOpenForm);
        Reg = findViewById(R.id.btnOpenReg);

        GetDB();
    }

    @Override
    public void onResume() {
        super.onResume();

        RestoreClick();
    }

    @Override
    protected void onPause() {
        super.onPause();

        BlockClick();
    }

    private void RestoreClick() {
        Auth.setClickable(true);
        Reg.setClickable(true);
    }

    private void BlockClick() {
        Auth.setClickable(false);
        Reg.setClickable(false);
    }

    private void GetDB() {
        DBThread = new Thread(() ->
        {
            database = InstanceDB.getInstance().getDatabase();
            usersDao = database.usersDao();
        });
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void OpenMainForm(View view)
    {
        String login = Login.getText().toString(), password = Password.getText().toString();

        if(!CheckFields(login, password)) {
            return;
        }

        FindUser(login);

        if(!CheckExistUser(user))
            return;

        if(!CheckPassword(user.getPassword(), password))
            return;

        LogIn(user.getRole());
    }

    boolean CheckFields(String login, String password)
    {
        if(login.equals(""))
        {
            Toast.makeText(this, password.equals("") ? "Заполните поля" : "Заполните логин", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.equals(""))
        {
            Toast.makeText(this, "Заполните пароль", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void FindUser(String login) {
        Runnable UpdateUsers = () ->
            user = usersDao.GetByLogin(login);

        DBThread = new Thread(UpdateUsers);
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean CheckExistUser(Users user)
    {
        if(user != null)
            return true;

        Toast.makeText(this, "Пользователь с таким логином не найден", Toast.LENGTH_LONG).show();
        return false;
    }

    boolean CheckPassword(String userPassword, String password)
    {
        if(userPassword.equals(password))
            return true;

        Toast.makeText(this, "Неверный пароль", Toast.LENGTH_LONG).show();
        return false;
    }

    private void LogIn(String role) {
        Intent intent = new Intent(Authorization.this, MainForm.class);

        if(role.equals("Admin"))
            intent.putExtra("Role", role);

        startActivity(intent);
    }

    public void OpenReg(View view)
    {
        Intent intent = new Intent(Authorization.this, Registration.class);
        startActivity(intent);
    }
}