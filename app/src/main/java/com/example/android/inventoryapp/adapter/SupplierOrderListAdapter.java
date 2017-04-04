package com.example.android.inventoryapp.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.activity.SupplierOrderActivity;
import com.example.android.inventoryapp.model.parsingModel.SupplierOrderDisplayModel;
import com.example.android.inventoryapp.viewHolder.SupplierOrderListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girish on 9/18/2016.
 */
public class SupplierOrderListAdapter extends RecyclerView.Adapter<SupplierOrderListViewHolder> {

    List<SupplierOrderDisplayModel> supplierOrderDisplayModels = new ArrayList<SupplierOrderDisplayModel>();
    Activity activity;
    SupplierOrderListViewHolder supplierOrderListViewHolder;

    public SupplierOrderListAdapter(Activity activity,
                                    List<SupplierOrderDisplayModel> supplierOrderDisplayModels) {
        this.activity = activity;
        this.supplierOrderDisplayModels = supplierOrderDisplayModels;
    }

    @Override
    public SupplierOrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_order_list_cell,parent,false);
        supplierOrderListViewHolder = new SupplierOrderListViewHolder(view);
        return supplierOrderListViewHolder;
    }

    @Override
    public void onBindViewHolder(SupplierOrderListViewHolder holder, int position) {
        holder.productName.setText(supplierOrderDisplayModels.get(position).getProductName());
        holder.productQuantity.setText(supplierOrderDisplayModels.get(position).getProductQuantity());

        byte[] decodeString = Base64.decode(supplierOrderDisplayModels.get(position).getProductImage(), Base64.DEFAULT);
        Bitmap decodedByte = null;
        decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

        holder.productImage.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {
        return supplierOrderDisplayModels.size();
    }
}
