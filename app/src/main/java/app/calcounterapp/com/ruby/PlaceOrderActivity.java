package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.CartDB.CartDatabase;
import app.calcounterapp.com.ruby.CartDB.LocalCartDataSource;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.Retrofit.IMyRubyAPI;
import app.calcounterapp.com.ruby.Retrofit.RetrofitClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class PlaceOrderActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.proceed)
    Button proceed;
    @BindView(R.id.pod)
    RadioButton pod;
    @BindView(R.id.online_payment)
    RadioButton online_payment;

    CompositeDisposable compositeDisposable;
    AlertDialog dialog;
    IMyRubyAPI myRubyAPI;
    CartDataSource cartDataSource;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(true);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        init();

        initView();
    }

    private void initView(){
        toolbar.setTitle("Place Your order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total.setEnabled(false);

                if (address.getText() == null || date.getText() == null){
                    Toast.makeText(PlaceOrderActivity.this, "Ensure you fill in all blank spaces", Toast.LENGTH_LONG).show();
                }

                if (pod.isChecked()){

                }else if (online_payment.isChecked()){

                }
            }
        });
    }

    private void init(){

        ButterKnife.bind(this);

        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(PlaceOrderActivity.this).cartDAO());

    }
}
