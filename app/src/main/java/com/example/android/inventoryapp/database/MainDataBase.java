package com.example.android.inventoryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.android.inventoryapp.model.databaseModel.ProductGetModel;
import com.example.android.inventoryapp.model.databaseModel.SupplierOrderGetModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Girish on 9/13/2016.
 */
public class MainDataBase {
    private static final String DATABASE_NAME = "inventory";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PRODUCT_TABLE = "product";
    private static final String DATABASE_SUPPLIER_ORDER = "supplier_order";

    private static final String TABLE_PRODUCTS = "create table product (_id integer primary key autoincrement, "
            + "product_name varchar, product_quantity varchar, product_price varchar , product_description varchar," +
            "product_image varchar );";
    private static final String TABLE_SUPPLIER = "create table supplier_order (_id integer primary key autoincrement, " +
            "product_name varchar, product_quantity varchar, product_image varchar );";

    private static final String TAG = "DatabaseClass";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final Context context;

    ArrayList<ProductGetModel> productGetModels = new ArrayList<ProductGetModel>();
    ArrayList<SupplierOrderGetModel> supplierOrderGetModels = new ArrayList<SupplierOrderGetModel>();

    public MainDataBase(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    // ---opens the database---
    public MainDataBase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        db = DBHelper.getReadableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //exportDB();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                /* add db.execSQL */
                db.execSQL(TABLE_PRODUCTS);
                db.execSQL(TABLE_SUPPLIER);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void exportDB() {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + "com.example.android.inventoryapp" + "/databases/" + DATABASE_NAME;
            String backupDBPath = "inventory.sqlite";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    /* insert product to db */

    //----product insert query----
    public long insertProduct(String product_name, String product_quantity,
                              String product_price,String product_description,String product_image) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("product_name", product_name);
        initialValues.put("product_quantity", product_quantity);
        initialValues.put("product_price", product_price);
        initialValues.put("product_description",product_description);
        initialValues.put("product_image",product_image);
        return db.insert(DATABASE_PRODUCT_TABLE, null, initialValues);
    }

    public long insertOrderSupplier(String product_name,String product_quantity,String productImage){
        ContentValues initialValues = new ContentValues();
        initialValues.put("product_name", product_name);
        initialValues.put("product_quantity", product_quantity);
        initialValues.put("product_image", productImage);
        return db.insert(DATABASE_SUPPLIER_ORDER, null, initialValues);
    }

    /* read data from data base */
    public ArrayList<ProductGetModel> productGetModels(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_PRODUCT_TABLE, null);
        try {
            if(cursor.moveToFirst()) {
                do{
                    ProductGetModel productGetModel = new ProductGetModel();
                    productGetModel.setId(cursor.getString(0));
                    productGetModel.setProductName(cursor.getString(1));
                    productGetModel.setProductQuantity(cursor.getString(2));
                    productGetModel.setProductPrice(cursor.getString(3));
                    productGetModel.setProductDescription(cursor.getString(4));
                    productGetModel.setProductImage(cursor.getString(5));
                    productGetModels.add(productGetModel);
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
            e.getStackTrace();
        } finally {
            cursor.close();
        }
        return productGetModels;
    }

    public ArrayList<SupplierOrderGetModel> getSupplierOrder(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_SUPPLIER_ORDER, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    SupplierOrderGetModel supplierOrderGetModel = new SupplierOrderGetModel();
                    supplierOrderGetModel.setProductName(cursor.getString(1));
                    supplierOrderGetModel.setProductQuantity(cursor.getString(2));
                    supplierOrderGetModel.setProductImage(cursor.getString(3));
                    supplierOrderGetModels.add(supplierOrderGetModel);
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
            e.getStackTrace();
        } finally {
            cursor.close();
        }
        return supplierOrderGetModels;
    }

    /* update product */
    public void updateProductValue(String id,String productName,String productQuantity,
                                  String productPrice,String productDescription,String productImage){
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_name",productName);
        contentValues.put("product_quantity",productQuantity);
        contentValues.put("product_price",productPrice);
        contentValues.put("product_description",productDescription);
        contentValues.put("product_image",productImage);
        db.update(DATABASE_PRODUCT_TABLE,contentValues,"_id"+"=?",new String[]{id});
        db.close();
    }

    public void updateProductQuantity(String id,String productQuantity){
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_quantity",productQuantity);
        db.update(DATABASE_PRODUCT_TABLE,contentValues,"_id"+"=?",new String[]{id});
        db.close();
    }

    /* delete product */
    public int deleteProduct(String id){
        return db.delete(DATABASE_PRODUCT_TABLE,"_id=?", new String[] { id });
    }

}