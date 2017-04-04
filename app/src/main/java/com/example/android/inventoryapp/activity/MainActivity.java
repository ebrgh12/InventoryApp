package com.example.android.inventoryapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.adapter.ProductListAdapter;
import com.example.android.inventoryapp.constants.CommonConstants;
import com.example.android.inventoryapp.database.MainDataBase;
import com.example.android.inventoryapp.model.databaseModel.ProductGetModel;
import com.example.android.inventoryapp.model.parsingModel.ProductModel;
import com.example.android.inventoryapp.serviceAndGeneralInterface.CustomItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.product_list)
    RecyclerView productList;

    @InjectView(R.id.no_data)
    TextView noDataFound;

    @OnClick(R.id.add_product)
    void addNewProduct(){
        Intent intent = new Intent(MainActivity.this,AddNewProductActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.view_order_list)
    void viewOrderList(){
        Intent intent = new Intent(MainActivity.this,SupplierOrderActivity.class);
        startActivity(intent);
        finish();
    }
    MainDataBase mainDataBase;
    ProductListAdapter productListAdapter;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<ProductModel>();
    ArrayList<ProductGetModel> productGetModelArrayList = new ArrayList<ProductGetModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        productList.setLayoutManager(new LinearLayoutManager(this));

        mainDataBase = new MainDataBase(this);

        mainDataBase.open();
        productGetModelArrayList = mainDataBase.productGetModels();
        if(productGetModelArrayList.size() != 0){
            for(int i=0;i<productGetModelArrayList.size();i++){
                productModelArrayList.add(new ProductModel(
                        productGetModelArrayList.get(i).getId(),
                        productGetModelArrayList.get(i).getProductName(),
                        productGetModelArrayList.get(i).getProductQuantity(),
                        productGetModelArrayList.get(i).getProductPrice(),
                        productGetModelArrayList.get(i).getProductDescription(),
                        productGetModelArrayList.get(i).getProductImage()));
            }

            noDataFound.setVisibility(View.GONE);
            productList.setVisibility(View.VISIBLE);

            productListAdapter = new ProductListAdapter(this,productModelArrayList,MainActivity.this, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    /* handle list click function hear */
                    CommonConstants.productID = productModelArrayList.get(position).getId();
                    CommonConstants.productName = productModelArrayList.get(position).getProductName();
                    CommonConstants.productQuantity = productModelArrayList.get(position).getProductQuantity();
                    CommonConstants.productPrice = productModelArrayList.get(position).getProductPrice();
                    CommonConstants.productDetails = productModelArrayList.get(position).getProductDescription();
                    CommonConstants.productImageData = productGetModelArrayList.get(position).getProductImage();

                    Intent intent = new Intent(MainActivity.this,ProductDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            productList.setAdapter(productListAdapter);
        }else {
            noDataFound.setVisibility(View.VISIBLE);
            productList.setVisibility(View.GONE);
        }
        mainDataBase.close();

    }

    public void UpdateProductQuantity(){
        productModelArrayList.clear();
        productGetModelArrayList.clear();

        mainDataBase.open();
        productGetModelArrayList = mainDataBase.productGetModels();
        if(productGetModelArrayList.size() != 0){
            for(int i=0;i<productGetModelArrayList.size();i++){
                productModelArrayList.add(new ProductModel(
                        productGetModelArrayList.get(i).getId(),
                        productGetModelArrayList.get(i).getProductName(),
                        productGetModelArrayList.get(i).getProductQuantity(),
                        productGetModelArrayList.get(i).getProductPrice(),
                        productGetModelArrayList.get(i).getProductDescription(),
                        productGetModelArrayList.get(i).getProductImage()));
            }

            productListAdapter.notifyDataSetChanged();

        }
    }
}
