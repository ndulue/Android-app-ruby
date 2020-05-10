package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.Model.RegisterUserModel;
import app.calcounterapp.com.ruby.Model.UserModel;
import app.calcounterapp.com.ruby.Retrofit.IMyRubyAPI;
import app.calcounterapp.com.ruby.Retrofit.RetrofitClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.name)
    EditText name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;
    IMyRubyAPI myRubyAPI;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        initView();

        registerUser();

        redirect();
    }


    private void init() {
        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
        dialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setCancelable(false).build();
    }

    private void initView(){

        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.register));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void registerUser(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                compositeDisposable.add(myRubyAPI.postUser(
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterUserModel>() {
                    @Override
                    public void accept(RegisterUserModel registerUserModel) throws Exception {
                        if (registerUserModel.isSuccess()){

                            Toast.makeText(RegisterActivity.this, "You have successfully registered, login to continue", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();

                        }else{
                            Toast.makeText(RegisterActivity.this, ""+registerUserModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(RegisterActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
            }
        });
    }

    private void redirect() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
