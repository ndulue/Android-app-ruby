package app.calcounterapp.com.ruby.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.CartDB.CartDAO;
import app.calcounterapp.com.ruby.CartDB.CartDataSource;
import app.calcounterapp.com.ruby.CartDB.CartDatabase;
import app.calcounterapp.com.ruby.CartDB.CartItem;
import app.calcounterapp.com.ruby.CartDB.LocalCartDataSource;
import app.calcounterapp.com.ruby.EventBus.CalculateEvent;
import app.calcounterapp.com.ruby.Interface.IOnCartClickListener;
import app.calcounterapp.com.ruby.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartDataSource cartDataSource;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_card, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(cartItems.get(position).getImage()).into(holder.image);
        holder.price.setText(new StringBuilder("$").append(cartItems.get(position).getPrice()));
        holder.quantity.setText(new StringBuilder(cartItems.get(position).getQuantity()));
        holder.title.setText(new StringBuilder(cartItems.get(position).getTitle()));

        int finalprice = cartItems.get(position).getPrice()*cartItems.get(position).getQuantity();
        holder.total.setText(String.valueOf(finalprice));

        holder.setListener(new IOnCartClickListener() {
            @Override
            public void onCalculatePriceListener(View view, int position, boolean isDecrease, boolean isDelete) {
                if (!isDelete){

                    if (isDecrease){
                        if (cartItems.get(position).getQuantity() > 1){
                            cartItems.get(position).setQuantity(cartItems.get(position).getQuantity()-1);
                        }
                    }
                    else
                    {
                        if (cartItems.get(position).getQuantity() < 10){
                            cartItems.get(position).setQuantity(cartItems.get(position).getQuantity()+1);
                        }
                    }

                    cartDataSource.updateCart(cartItems.get(position))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    holder.quantity.setText(cartItems.get(position).getQuantity());
                                    EventBus.getDefault().postSticky(new CalculateEvent());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                }else{

                    cartDataSource.deleteCart(cartItems.get(position))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    notifyItemRemoved(position);
                                    EventBus.getDefault().postSticky(new CalculateEvent());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.remove)
        TextView remove;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.minus)
        TextView minus;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.add)
        TextView add;
        @BindView(R.id.image)
        ImageView image;

        Unbinder unbinder;
        IOnCartClickListener listener;

        public void setListener(IOnCartClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);

            minus.setOnClickListener(this);
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == minus){
                listener.onCalculatePriceListener(v,getAdapterPosition(),true, false);
            }else if (v == add){
                listener.onCalculatePriceListener(v,getAdapterPosition(),false,false);
            }else if (v ==  remove){
                listener.onCalculatePriceListener(v,getAdapterPosition(),false, true);
            }
        }
    }
}
