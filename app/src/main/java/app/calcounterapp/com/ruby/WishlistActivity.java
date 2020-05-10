package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.Adapter.WishlistAdapter;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.EventBus.FavouriteLoadAllEvent;
import app.calcounterapp.com.ruby.Model.Favourite;
import app.calcounterapp.com.ruby.Model.FavouriteModel;
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
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_wishlist)
    RecyclerView recycler_wishlist;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;
    IMyRubyAPI myRubyAPI;
    CartDataSource cartDataSource;

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
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        init();

        initView();

        LoadAllFavs();
    }

    private void LoadAllFavs() {
        dialog.dismiss();
        compositeDisposable.add(myRubyAPI.getAllFav(Common.API_KEY,Common.currentUser.getEmail())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<FavouriteModel>() {
            @Override
            public void accept(FavouriteModel favouriteModel) throws Exception {
                if (favouriteModel.isSuccess()){
                    EventBus.getDefault().post(new FavouriteLoadAllEvent(true, favouriteModel.getFavouriteList()));
                }else{
                    Toast.makeText(WishlistActivity.this, ""+favouriteModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(WishlistActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void init(){
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void LoadWishlistEvent(FavouriteLoadAllEvent event){
        if (event.isSuccess()){
            displayWishlist(event.getFavouriteList());
        }
    }

    private void displayWishlist(List<Favourite> favouriteList){
        WishlistAdapter wishlistAdapter = new WishlistAdapter(WishlistActivity.this, favouriteList);
        recycler_wishlist.setAdapter(wishlistAdapter);
    }
}
