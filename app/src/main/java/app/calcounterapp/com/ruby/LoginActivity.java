package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.Model.LoginUserModel;
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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;
    IMyRubyAPI myRubyAPI;
    Snackbar snackbar;

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
        setContentView(R.layout.activity_login);

        init();

        initView();

        login();

        redirect();
    }

    private void initView() {
        ButterKnife.bind(this);

        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init(){

        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

    }

    private void login(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                compositeDisposable.add(myRubyAPI.getUser(
                        email.getText().toString(),
                        password.getText().toString()
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoginUserModel>() {
                        @Override
                        public void accept(LoginUserModel loginUserModel) throws Exception {
                            if (loginUserModel.isSuccess()){
                                Common.currentUser = loginUserModel.getResult().get(0);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, ""+loginUserModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                                }
                            }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(LoginActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }));
            }
        });

    }

    private void redirect(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
