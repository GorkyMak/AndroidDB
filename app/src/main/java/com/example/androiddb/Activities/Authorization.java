package com.example.androiddb.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Database.AppDatabase;
import com.example.androiddb.Database.Entities.Users.Users;
import com.example.androiddb.Database.Entities.Users.UsersDao;
import com.example.androiddb.Database.Repository;
import com.example.androiddb.R;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class Authorization extends AppCompatActivity {
    EditText Login, Password;
    Button Auth, Reg;
    AppDatabase database;
    UsersDao usersDao;
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
            database = Repository.getInstance().getDatabase();
            usersDao = database.usersDao();
    }

    public void OpenMainForm(View view)
    {
        String login = Login.getText().toString(), password = Password.getText().toString();

        if(!CheckFields(login, password)) {
            return;
        }

        FindUser(login, password);
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

    private void FindUser(String login, String password) {
        usersDao.GetByLogin(login)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Users>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull Users users) {
                        user = users;

                        if(!CheckPassword(user.getPassword(), password))
                            return;

                        LogIn(user.getRole());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ERROR-GetByLogin", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(Authorization.this, "Пользователь с таким логином не найден",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    boolean CheckPassword(String userPassword, String password)
    {
        if(userPassword.equals(password))
            return true;

        Toast.makeText(Authorization.this, "Неверный пароль", Toast.LENGTH_LONG).show();
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