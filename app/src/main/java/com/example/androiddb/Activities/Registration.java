package com.example.androiddb.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Database.Repository;
import com.example.androiddb.Database.AppDatabase;
import com.example.androiddb.Database.Entities.Users.Users;
import com.example.androiddb.Database.Entities.Users.UsersDao;
import com.example.androiddb.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class Registration extends AppCompatActivity {
    EditText Login, Password, RepeatPassword, Phone, Email, LastName, FirstName, MiddleName;
    Button Reg, Cancel;
    AppDatabase database;
    UsersDao usersDao;
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

    public void RegUser(View view)
    {
        if(!CheckFields())
            return;

        FindUser();
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
            if (!userAttribute.getText().toString().equals(""))
                continue;

            if (Fields.length() > 0)
                Fields.append(", ");

            Fields.append(userAttribute.getHint().toString());
        }

        if(Fields.length() > 0)
        {
            Toast.makeText(this, "Заполните поля: " + Fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void FindUser() {
        usersDao.GetByLogin(Login.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Users>() {
                    @Override
                    public void onSuccess(@NonNull Users users) {
                        Toast.makeText(Registration.this, "Пользователь "+ users.getLogin() +" уже существует",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ERROR-GetByLogin", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if(!CheckRepeatPassword(Password.getText().toString(), RepeatPassword.getText().toString()))
                            return;

                        AddNewUser();

                        GoBack();
                    }
                });
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