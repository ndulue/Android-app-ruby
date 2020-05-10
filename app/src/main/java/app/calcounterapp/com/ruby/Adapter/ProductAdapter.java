package app.calcounterapp.com.ruby.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.calcounterapp.com.ruby.EventBus.ProductDetailEvent;
import app.calcounterapp.com.ruby.Interface.IProductClickListener;
import app.calcounterapp.com.ruby.Model.Product;
import app.calcounterapp.com.ruby.ProductDetailsActivity;
import app.calcounterapp.com.ruby.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(productList.get(position).getImage()).into(holder.product_image);
        holder.price.setText(new StringBuilder("$").append(productList.get(position).getPrice()));
        holder.sku.setText(new StringBuilder(productList.get(position).getSku()));
        holder.tag.setText(new StringBuilder(productList.get(position).getTag()));
        holder.title.setText(new StringBuilder(productList.get(position).getTitle()));

        holder.setListener(new IProductClickListener() {
            @Override
            public void onClick(View view, int position) {
                context.startActivity(new Intent(context, ProductDetailsActivity.class));
                EventBus.getDefault().postSticky(new ProductDetailEvent(true,productList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.product_image)
        ImageView product_image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.sku)
        TextView sku;
        Unbinder unbinder;
        IProductClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void setListener(IProductClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }
}
