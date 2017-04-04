package com.example.android.inventoryapp.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.inventoryapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Girish on 9/15/2016.
 */
public class ProductListViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.product_name)
    public TextView ProductName;

    @InjectView(R.id.product_price)
    public TextView ProductPrice;

    @InjectView(R.id.product_quantity)
    public TextView productQuantity;

    @InjectView(R.id.product_image)
    public ImageView productImage;

    @InjectView(R.id.make_sale)
    public TextView makeSale;

    public ProductListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }

}
