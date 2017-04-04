package com.example.android.inventoryapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.adapter.SupplierOrderListAdapter;
import com.example.android.inventoryapp.database.MainDataBase;
import com.example.android.inventoryapp.model.databaseModel.SupplierOrderGetModel;
import com.example.android.inventoryapp.model.parsingModel.SupplierOrderDisplayModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Girish on 9/18/2016.
 */
public class SupplierOrderActivity extends Activity {
    @InjectView(R.id.order_list)
    RecyclerView orderList;

    @InjectView(R.id.no_data)
    TextView noDataFound;

    MainDataBase mainDataBase;

    List<SupplierOrderGetModel> supplierOrderGetModels = new ArrayList<SupplierOrderGetModel>();
    List<SupplierOrderDisplayModel> supplierOrderDisplayModels = new ArrayList<SupplierOrderDisplayModel>();

    SupplierOrderListAdapter supplierOrderListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_order_list);
        ButterKnife.inject(this);
        orderList.setLayoutManager(new LinearLayoutManager(this));

        mainDataBase = new MainDataBase(this);

        mainDataBase.open();
        supplierOrderGetModels = mainDataBase.getSupplierOrder();
        mainDataBase.close();

        if(supplierOrderGetModels.size() != 0){
            for(int i=0;i<supplierOrderGetModels.size();i++){
                supplierOrderDisplayModels.add(new SupplierOrderDisplayModel(
                        supplierOrderGetModels.get(i).getProductName(),
                        supplierOrderGetModels.get(i).getProductQuantity(),
                        supplierOrderGetModels.get(i).getProductImage()));
            }

            noDataFound.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);

            /* call load list method */
            LoadListData();
        }else {
            noDataFound.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        }
    }

    private void LoadListData() {
        supplierOrderListAdapter = new SupplierOrderListAdapter(SupplierOrderActivity.this,supplierOrderDisplayModels);
        orderList.setAdapter(supplierOrderListAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SupplierOrderActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
