package app.calcounterapp.com.ruby.CartDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

@Entity(tableName = "Cart")
public class CartItem {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "sku")
    private String sku;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "manufacturer")
    private String manufacturer;

    @ColumnInfo(name = "useremail")
    private String useremail;

    public CartItem() {
    }


    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

}
