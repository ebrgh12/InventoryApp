package com.example.android.inventoryapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.constants.CommonConstants;
import com.example.android.inventoryapp.database.MainDataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by girish on 9/14/2016.
 */
public class AddNewProductActivity extends Activity {
    @InjectView(R.id.product_name)
    EditText productName;

    @InjectView(R.id.product_price)
    EditText productPrice;

    @InjectView(R.id.product_quantity)
    EditText productQuantity;

    @InjectView(R.id.product_description)
    EditText productDescription;

    @InjectView(R.id.add_product)
    Button addProduct;

    @InjectView(R.id.take_picture)
    CircleImageView productImage;

    @OnClick(R.id.take_picture)
    void takeProductImage(){
        /**
         * add image to the product */
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);
    }

    @OnClick(R.id.add_product)
    void addProduct(){
        /* add the new product to db */
        if(productName.getText().toString() != null && !productName.getText().toString().isEmpty()
                && productPrice.getText().toString() != null && !productPrice.getText().toString().isEmpty()
                && productQuantity.getText().toString() != null && !productQuantity.getText().toString().isEmpty()
                && productDescription.getText().toString() != null && !productDescription.getText().toString().isEmpty()){

            if(CommonConstants.productImage != null){
                if(CommonConstants.productUpdate == true){
                /* update the existing product */
                    mainDataBase.open();
                    mainDataBase.updateProductValue(CommonConstants.productID,productName.getText().toString(),productQuantity.getText().toString(),
                            productPrice.getText().toString(),productDescription.getText().toString(),CommonConstants.productImage);
                    mainDataBase.close();
                }else {
                /* insert to db hear once you have all the information */
                    mainDataBase.open();
                    mainDataBase.insertProduct(productName.getText().toString(),productQuantity.getText().toString(),
                            productPrice.getText().toString(),productDescription.getText().toString(),CommonConstants.productImage);
                    mainDataBase.close();
                }

                CommonConstants.productImage = null;

                onBackPressed();
            }else {
                Toast.makeText(AddNewProductActivity.this,"Please adda product image",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(AddNewProductActivity.this,"Please enter all the above product information",Toast.LENGTH_LONG).show();
        }
    }

    MainDataBase mainDataBase;
    Uri selectedImageUri;
    String selectedPath;

    String[] perms = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);
        ButterKnife.inject(this);
        mainDataBase = new MainDataBase(this);

        /**
         * call permission check method
         * */
        CheckPermission();

        if(CommonConstants.productUpdate == true){

            productName.setText(CommonConstants.productName);
            productQuantity.setText(CommonConstants.productQuantity);
            productPrice.setText(CommonConstants.productPrice);
            productDescription.setText(CommonConstants.productDetails);

            byte[] decodeString = Base64.decode(CommonConstants.productImageData, Base64.DEFAULT);
            Bitmap decodedByte = null;
            decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

            productImage.setImageBitmap(decodedByte);
            productImage.setRotation(360);

            CommonConstants.productImage = CommonConstants.productImageData;

            addProduct.setText("UPDATE PRODUCT");
        }else {
            addProduct.setText("ADD PRODUCT");
        }

    }

    @SuppressLint("NewApi")
    private void CheckPermission() {
        /**
         *  check the runtime permission once accepted,
         *  then only allow the user to enter the next screen
         *  */
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {

            requestPermissions(perms, permsRequestCode);

        }else {
            /**
             *  don't do any thing
             *  */
        }
    }

    /**
     * on activity result
     * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(data.getData() != null){
                selectedImageUri = data.getData();

                if (requestCode == 100 && resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedPath = getPath(selectedImageUri);
                    productImage.setImageURI(selectedImageUri);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    CommonConstants.productImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                }

            }else{
                Toast.makeText(AddNewProductActivity.this, "failed to get Image!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        CommonConstants.productUpdate = false;

        Intent intent = new Intent(AddNewProductActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* runtime permission */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(permsRequestCode){
            case 200:
                boolean camera = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                boolean storage_write = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                boolean storage_read = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                /**
                 * check the permission check result if all permission granted,
                 * then only allow the user to navigate to next screen */
                if(camera == true && storage_write == true && storage_read == true){

                }else {
                    if(camera == false){
                        Toast.makeText(AddNewProductActivity.this,
                                "Please enable the Camera permission to proceed",Toast.LENGTH_LONG).show();
                        /**
                         * call permission check method
                         * */
                        CheckPermission();
                    }else if(storage_write == false){
                        Toast.makeText(AddNewProductActivity.this,
                                "Please enable the Storage permission to proceed",Toast.LENGTH_LONG).show();
                        /**
                         * call permission check method
                         * */
                        CheckPermission();
                    }else if(storage_read == false){
                        Toast.makeText(AddNewProductActivity.this,
                                "Please enable the Storage permission to proceed",Toast.LENGTH_LONG).show();
                        /**
                         * call permission check method
                         * */
                        CheckPermission();
                    }
                }
                break;
        }
    }
    private boolean canMakeSmores(){
        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private boolean hasPermission(String permission){
        if(canMakeSmores()){
            return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }
    public int checkSelfPermission(String permission) {
        return 1;
    }
}
