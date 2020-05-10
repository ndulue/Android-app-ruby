package app.calcounterapp.com.ruby.EventBus;

import app.calcounterapp.com.ruby.Model.Product;

public class ProductDetailEvent {

    private boolean success;
    private Product product;

    public ProductDetailEvent(boolean success, Product product) {
        this.success = success;
        this.product = product;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
