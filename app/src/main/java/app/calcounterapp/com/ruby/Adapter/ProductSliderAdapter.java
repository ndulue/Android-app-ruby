package app.calcounterapp.com.ruby.Adapter;

import java.util.List;

import app.calcounterapp.com.ruby.Model.Product;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ProductSliderAdapter extends SliderAdapter {

    List<Product> productList;

    public ProductSliderAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(productList.get(position).getImage());
    }
}
