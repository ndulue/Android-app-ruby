package app.calcounterapp.com.ruby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.CartDB.CartDatabase;
import app.calcounterapp.com.ruby.CartDB.CartItem;
import app.calcounterapp.com.ruby.CartDB.LocalCartDataSource;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.EventBus.ProductDetailEvent;
import app.calcounterapp.com.ruby.Model.FavouriteModel;
import app.calcounterapp.com.ruby.Model.Product;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.pdtitle)
    TextView title;
    @BindView(R.id.pddesc)
    TextView desc;
    @BindView(R.id.pdmanu)
    TextView manufacturer;
    @BindView(R.id.pdsku)
    TextView sku;
    @BindView(R.id.pdprice)
    TextView price;
    @BindView(R.id.pdtag)
    TextView tag;
    @BindView(R.id.pdimage)
    ImageView image;
    @BindView(R.id.fav)
    Button fav;
    @BindView(R.id.cart)
    Button cart;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    CompositeDisposable compositeDisposable;
    AlertDialog dialog;
    IMyRubyAPI myRubyAPI;
    CartDataSource cartDataSource;

    Product selectedProduct;
    Integer selectedPrice;
    String selectedTitle;
    String selectedTag;
    String selectedDesc;
    String selectedSku;
    String selectedImage;
    String selectedManufacturer;

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
        setContentView(R.layout.activity_product_details);

        init();

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);


    }

    private void init(){

        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(ProductDetailsActivity.this).cartDAO());

    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    private void displayProductDetails(final ProductDetailEvent event){
        if (event.isSuccess()){

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            selectedProduct = event.getProduct();
            selectedPrice = event.getProduct().getPrice();
            selectedTitle = event.getProduct().getTitle();
            selectedTag = event.getProduct().getTag();
            selectedDesc = event.getProduct().getDescription();
            selectedImage = event.getProduct().getImage();
            selectedManufacturer = event.getProduct().getManufacturer();

            title.setText(selectedTitle);
            desc.setText(selectedDesc);
            manufacturer.setText(selectedManufacturer);
            sku.setText(selectedSku);
            price.setText(selectedPrice);
            tag.setText(selectedTag);
            Picasso.get().load(selectedImage).into(image);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //process favourite function
                    handleFav();
                }


            });

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //process cart function
                    handleCart();
                }
            });

        }else{

        }
    }

    private void handleCart() {

        CartItem cartItem = new CartItem();
        cartItem.setUseremail(Common.currentUser.getEmail());
        cartItem.setImage(selectedImage);
        cartItem.setManufacturer(selectedManufacturer);
        cartItem.setPrice(selectedPrice);
        cartItem.setQuantity(1);
        cartItem.setSku(selectedSku);
        cartItem.setTitle(selectedTitle);

        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( ()->{

                        },
                        throwable -> {

                            Toast.makeText(ProductDetailsActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );


        /*
        LayoutInflater inflater = LayoutInflater.from(this);
        final View AddCardDialog = inflater.inflate(R.layout.cart_dialog,null);

        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(AddCardDialog);
        final TextView quantity = AddCardDialog.findViewById(R.id.cartNumber);
        AddCardDialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


         */
    }

    private void handleFav() {
        compositeDisposable.add(myRubyAPI.getFav(Common.API_KEY, Common.currentUser.getEmail(), selectedSku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavouriteModel>() {
                    @Override
                    public void accept(FavouriteModel favouriteModel) throws Exception {
                        //Check if product exist in wishlist
                        if (favouriteModel.isSuccess()){
                            dialog = new AlertDialog.Builder(ProductDetailsActivity.this)
                                    .setMessage("Item already exist in your wishlist")
                                    .setCancelable(false)
                                    .setPositiveButton("Visit Wishlist", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            startActivity(new Intent(ProductDetailsActivity.this, WishlistActivity.class));
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }else {
                            //Add product to wishlist
                            if (favouriteModel.getMessage().contains("item don't exist")){
                                compositeDisposable.add(myRubyAPI.postFav(Common.API_KEY, Common.currentUser.getEmail(),
                                        selectedImage, selectedPrice, selectedSku, selectedTitle, selectedManufacturer)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<FavouriteModel>() {
                                            @Override
                                            public void accept(FavouriteModel favouriteModel) throws Exception {

                                                if (favouriteModel.isSuccess()){
                                                    dialog = new AlertDialog.Builder(ProductDetailsActivity.this)
                                                            .setCancelable(false)
                                                            .setMessage("Product added to wishlist")
                                                            .setPositiveButton("View Wishlist", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    startActivity(new Intent( ProductDetailsActivity.this, WishlistActivity.class));
                                                                    finish();
                                                                }
                                                            })
                                                            .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            }).show();
                                                }else {

                                                    Toast.makeText(ProductDetailsActivity.this, ""+favouriteModel.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Toast.makeText(ProductDetailsActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ProductDetailsActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
    }

}
