package app.calcounterapp.com.ruby.EventBus;

import java.util.List;

import app.calcounterapp.com.ruby.Model.Product;

public class ProductLoadEvent {

    private boolean success;
    private String message;
    private List<Product> productList;

    public ProductLoadEvent(boolean success, List<Product> productList) {
        this.success = success;
        this.productList = productList;
    }

    public ProductLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
