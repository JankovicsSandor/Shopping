package sanyi.shop.com.shop;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sanyi on 14/10/2017.
 */

public class ShopItem  implements Parcelable{
    private int productNo;
    private String picture;
    private String name;
    private String detail;
    private int price;
    private byte isAvailable;
    private String category;

    public ShopItem(int productNo, String picture, String name, String detail, int price, byte isAvailable, String category) {
        this.productNo = productNo;
        this.picture = picture;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.isAvailable = isAvailable;
        this.category = category;
    }

    protected ShopItem(Parcel in) {
        productNo = in.readInt();
        picture = in.readString();
        name = in.readString();
        detail = in.readString();
        price = in.readInt();
        isAvailable = in.readByte();
        category = in.readString();
    }

    public static final Creator<ShopItem> CREATOR = new Creator<ShopItem>() {
        @Override
        public ShopItem createFromParcel(Parcel in) {
            return new ShopItem(in);
        }

        @Override
        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };

    public int getProductNo() {
        return productNo;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int getPrice() {
        return price;
    }

    public byte isAvailable() {
        return isAvailable;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productNo);
        dest.writeString(this.picture);
        dest.writeString(this.name);
        dest.writeString(this.detail);
        dest.writeInt(this.price);
        dest.writeByte(this.isAvailable);
        dest.writeString(this.category);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("name " +this.name);
        return sb.toString();
    }
}
