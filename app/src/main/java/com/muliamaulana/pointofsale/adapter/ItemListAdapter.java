package com.muliamaulana.pointofsale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muliamaulana.pointofsale.R;
import com.muliamaulana.pointofsale.UpdateActivity;
import com.muliamaulana.pointofsale.database.ItemModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by muliamaulana on 9/9/2018.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private LinkedList<ItemModel> itemModelArrayList;
    private Activity activity;

    public ItemListAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<ItemModel> getItemModelArrayList() {
        return itemModelArrayList;
    }

    public void setListItem(LinkedList<ItemModel> itemModels) {
        this.itemModelArrayList = itemModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final int id = getItemModelArrayList().get(position).getId();
        final String name = getItemModelArrayList().get(position).getName();
        final int price = getItemModelArrayList().get(position).getPrice();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.tvTitle.setText(name);
        holder.tvPrice.setText(formatRupiah.format(price));
        final byte[] image = getItemModelArrayList().get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);

        holder.imageView.setImageBitmap(bitmap);
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(activity,UpdateActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                intent.putExtra("image", image);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getItemModelArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPrice;
        ImageView imageView;
        CardView cardItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_name);
            tvPrice = itemView.findViewById(R.id.item_price);
            imageView = itemView.findViewById(R.id.image_item);
            cardItem = itemView.findViewById(R.id.card_item);
        }
    }
}
