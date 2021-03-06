package com.example.androiddb.Activities;

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

public class Registration extends AppCompatActivity {
    EditText Login, Password, RepeatPassword, Phone, Email, LastName, FirstName, MiddleName;
    Button Reg, Cancel;
    AppDatabase database;
    UsersDao usersDao;
    Users users;
    Thread DBThread;

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

        Reg = findViewById(R.id.btnReg);
        Cancel = findViewById(R.id.btnCancel);

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
        Reg.setClickable(true);
        Cancel.setClickable(true);
    }

    private void BlockClick() {
        Reg.setClickable(false);
        Cancel.setClickable(false);
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

    public void RegUser(View view)
    {

        if(!CheckFields())
            return;

        if(!CheckExcistedUser())
            return;

        if(!CheckRepeatPassword(Password.getText().toString(), RepeatPassword.getText().toString()))
            return;

        AddNewUser();

        GoBack();
    }

    boolean CheckFields()
    {
        EditText[] UserAttributes = new EditText[]
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

        StringBuilder Fields = new StringBuilder();

        for (EditText userAttribute : UserAttributes) {
            if (!userAttribute.toString().equals(""))
                continue;

            if (Fields.length() > 0)
                Fields.append(", ");

            Fields.append(userAttribute.getTag().toString());
        }

        if(Fields.length() > 0)
        {
            Toast.makeText(this, "Заполните поля: " + Fields, Toast.LENGTH_SHORT).show();
            return false;
        }

        Runnable GetDB = () ->
                users = usersDao.GetByLogin(Login.getText().toString());

        DBThread = new Thread(GetDB);
        DBThread.start();
        return true;
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
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return users != null;
    }

    private boolean CheckRepeatPassword(String password, String repeatPassword) {
        if(repeatPassword.equals(password))
            return true;

        Toast.makeText(this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void AddNewUser() {
        Users user = new Users
                (
                        Login.getText().toString(),
                        Password.getText().toString(),
                        Phone.getText().toString(),
                        Email.getText().toString(),
                        LastName.getText().toString(),
                        FirstName.getText().toString(),
                        MiddleName.getText().toString(),
                        "User"
                );

        DBThread = new Thread(() ->
                usersDao.Insert(user));
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void GoBack() {
        this.finish();
    }

    public void Cancel(View view) {
        GoBack();
    }
}