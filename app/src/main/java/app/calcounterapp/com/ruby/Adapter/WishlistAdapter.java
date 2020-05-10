package app.calcounterapp.com.ruby.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.CartActivity;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.CartDB.CartDatabase;
import app.calcounterapp.com.ruby.CartDB.CartItem;
import app.calcounterapp.com.ruby.CartDB.LocalCartDataSource;
import app.calcounterapp.com.ruby.Common.Common;
import app.calcounterapp.com.ruby.Interface.IWishlistClickListener;
import app.calcounterapp.com.ruby.Model.Favourite;
import app.calcounterapp.com.ruby.Model.FavouriteModel;
import app.calcounterapp.com.ruby.R;
import app.calcounterapp.com.ruby.Retrofit.IMyRubyAPI;
import app.calcounterapp.com.ruby.Retrofit.RetrofitClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {

    private Context context;
    private List<Favourite> favouriteList;
    private CompositeDisposable compositeDisposable;
    private AlertDialog dialog;
    private CartDataSource cartDataSource;
    private IMyRubyAPI myRubyAPI;

    public WishlistAdapter(Context context, List<Favourite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
        dialog = new SpotsDialog.Builder().setContext(context).setCancelable(false).build();
        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
        myRubyAPI = RetrofitClient.getInstance(Common.BASE_URL).create(IMyRubyAPI.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.wishlist_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.wishlist_price.setText(favouriteList.get(position).getPrice());
       // holder.wishlist_tag.setText(favouriteList.get(position).getTag());
        holder.wishlist_title.setText(favouriteList.get(position).getTitle());

        Picasso.get().load(favouriteList.get(position).getImage()).into(holder.wishlist_image);
        holder.setListener(new IWishlistClickListener() {
            @Override
            public void onClick(View view, int position, boolean isAddtoCart, boolean isDelete) {
                if (isDelete){
                    compositeDisposable.add(myRubyAPI.deleteFav(Common.API_KEY, Common.currentUser.getEmail(), favouriteList.get(position).getTag())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(favouriteModel -> {
                                if (favouriteModel.isSuccess()) {
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Item successfully removed from wishlist", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "" + favouriteModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }, throwable -> Toast.makeText(context, "" + throwable.getMessage(), Toast.LENGTH_LONG).show()));

                }else if (isAddtoCart){
                    CartItem cartItem = new CartItem();
                    cartItem.setEmail(Common.currentUser.getEmail());
                    cartItem.setImage(favouriteList.get(position).getImage());
                    cartItem.setManufacturer(favouriteList.get(position).getManufacturer());
                    cartItem.setPrice(favouriteList.get(position).getPrice());
                    cartItem.setQuantity(1);
                    cartItem.setSku(favouriteList.get(position).getSku());
                    cartItem.setTitle(favouriteList.get(position).getTitle());

                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe( ()-> context.startActivity(new Intent(context.getApplicationContext(), CartActivity.class)),
                                    throwable -> Toast.makeText(context, ""+throwable.getMessage(), Toast.LENGTH_LONG).show()
                            )
                    );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.wishlist_image)
        ImageView wishlist_image;
        @BindView(R.id.wishlist_price)
        TextView wishlist_price;
        @BindView(R.id.wishlist_cart)
        TextView wishlist_cart;
        @BindView(R.id.remove)
        TextView remove;
        @BindView(R.id.wishlist_title)
        TextView wishlist_title;
        IWishlistClickListener listener;
        Unbinder unbinder;


        private void setListener(IWishlistClickListener listener) {
            this.listener = listener;
        }

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            remove.setOnClickListener(this);
            wishlist_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == remove){
                listener.onClick(v,getAdapterPosition(),false, true);
            }else if (v == wishlist_cart){
                listener.onClick(v,getAdapterPosition(),true,false);
            }

        }
    }
}
