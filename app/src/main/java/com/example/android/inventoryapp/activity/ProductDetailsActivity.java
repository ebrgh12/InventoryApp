package com.example.android.inventoryapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.constants.CommonConstants;
import com.example.android.inventoryapp.database.MainDataBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Girish on 9/16/2016.
 */
public class ProductDetailsActivity extends Activity {
    @InjectView(R.id.product_name)
    TextView productName;

    @InjectView(R.id.product_quantity)
    TextView productQuantity;

    @InjectView(R.id.product_price)
    TextView productPrice;

    @InjectView(R.id.product_description)
    TextView productDescription;

    @InjectView(R.id.product_image)
    ImageView productImageView;

    @OnClick(R.id.delete_product)
    void deleteProduct(){

        /* give a prompt before deleting the product */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
        alertDialog.setCancelable(false);

        //set dialog title
        alertDialog.setTitle("Delete Product !!!");
        //set dialog message
        alertDialog.setMessage("Are you sure you want to delete the product...");
        //setting icon to dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
        //set positive button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                /* delete product from db */
                mainDataBase.open();
                mainDataBase.deleteProduct(CommonConstants.productID);
                mainDataBase.close();

                onBackPressed();
            }
        });

        alertDialog.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @OnClick(R.id.modify_quantity)
    void modifyQuantity(){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.update_quantity_popup, null);
        alertD = new AlertDialog.Builder(ProductDetailsActivity.this).create();

        final EditText productQuantityNew = (EditText) PromtView.findViewById(R.id.product_quantity);
        Button updateQuantity = (Button) PromtView.findViewById(R.id.update_product);

        productQuantityNew.setText(productQuantity.getText().toString());

        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDataBase.open();
                mainDataBase.updateProductQuantity(CommonConstants.productID,productQuantityNew.getText().toString());
                mainDataBase.close();

                alertD.dismiss();

                productQuantity.setText(productQuantityNew.getText().toString());

            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    @OnClick(R.id.bulk_order)
    void bulkOrder(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","email@email.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "bulk order");
        intent.putExtra(Intent.EXTRA_TEXT, "Please order this product in quantity 100");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    @OnClick(R.id.update_product)
    void updateProduct(){
        CommonConstants.productUpdate = true;

        Intent intent = new Intent(ProductDetailsActivity.this,AddNewProductActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.oder_product_sellet)
    void orderProductSuppler(){

        Intent intent = new Intent(ProductDetailsActivity.this,OrderProductSupplier.class);
        startActivity(intent);
        finish();
    }

    MainDataBase mainDataBase;
    AlertDialog alertD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        ButterKnife.inject(this);
        mainDataBase = new MainDataBase(this);

        productName.setText(CommonConstants.productName);
        productQuantity.setText(CommonConstants.productQuantity);
        productPrice.setText(CommonConstants.productPrice+ " $");
        productDescription.setText(CommonConstants.productDetails);

        byte[] decodeString = Base64.decode(CommonConstants.productImageData, Base64.DEFAULT);
        Bitmap decodedByte = null;
        decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

        productImageView.setImageBitmap(decodedByte);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductDetailsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
