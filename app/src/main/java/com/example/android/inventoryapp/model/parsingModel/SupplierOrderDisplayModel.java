package com.example.android.inventoryapp.model.parsingModel;

/**
 * Created by Girish on 9/18/2016.
 */
public class SupplierOrderDisplayModel {
    String productName;
    String productQuantity;
    String productImage;

    public SupplierOrderDisplayModel(String productName, String productQuantity, String productImage) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
