package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.CartDB.CartDatabase;
import app.calcounterapp.com.ruby.CartDB.CartItem;
import app.calcounterapp.com.ruby.CartDB.LocalCartDataSource;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.EventBus.CalculateEvent;
import app.calcounterapp.com.ruby.EventBus.SendCashEvent;
import app.calcounterapp.com.ruby.Retrofit.IMyRubyAPI;
import app.calcounterapp.com.ruby.Retrofit.RetrofitClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_wishlist)
    RecyclerView recycler_cart;
    @BindView(R.id.price)
    TextView totalprice;
    @BindView(R.id.checkout)
    Button checkout;

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
        setContentView(R.layout.activity_cart);

        init();

        initView();

        loadAllCart();
    }

    private void loadAllCart() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getEmail())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<CartItem>>() {
            @Override
            public void accept(List<CartItem> cartItems) throws Exception {
                if (cartItems.isEmpty()) {
                    checkout.setText("Empty cart");
                    checkout.setEnabled(false);
                } else {
                    checkout.setText("Proceed to checkout");
                    checkout.setEnabled(false);
                    calculateTotalPrice();
                }
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(CartActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void calculateTotalPrice() {

        cartDataSource.cartSum(Common.currentUser.getEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {

                        if (aLong <= 0){
                            checkout.setEnabled(false);
                            checkout.setText("Empty");
                        }else {
                            checkout.setEnabled(true);
                            checkout.setText("Proceed to checkout");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
            });

    }

    private void initView() {
        ButterKnife.bind(this);

        toolbar.setTitle("Cart items");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SendCashEvent(totalprice.getText().toString()));
                startActivity(new Intent(CartActivity.this, PlaceOrderActivity.class));
            }
        });
    }

    private void init(){

        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(CartActivity.this).cartDAO());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    private void calculatePrice(CalculateEvent event){
        if (event != null){
            calculateTotalPrice();
        }
    }

}
