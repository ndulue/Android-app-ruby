package app.calcounterapp.com.ruby.Model;

public class Favourite {
    private int id;
    private String image;
    private int price;
    private String sku;
    private String tag;
    private String title;
    private String manufacturer;

    public Favourite(int id, String tag, String image, int price, String sku, String title, String manufacturer) {
        this.id = id;
        this.image = image;
        this.price = price;
        this.tag = tag;
        this.sku = sku;
        this.title = title;
        this.manufacturer = manufacturer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
