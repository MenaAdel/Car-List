package com.example.softxperttask.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softxperttask.R;
import com.example.softxperttask.entites.CarResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder>{
    List<CarResponse.Data> list = new ArrayList<>();
    private Context context;

    public CarsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder holder, int position) {
        CarResponse.Data item = list.get(position);
        holder.brandText.setText(item.brand);
        if (item.constructionYear != null) {
            holder.yearText.setText(item.constructionYear);
        }
        if (item.imageUrl != null) {
            Picasso.get().load(item.imageUrl).placeholder(R.drawable.ic_launcher_background).into(holder.carImage);
        }
        if (item.isUsed) {
            holder.isUsedText.setText("Used");
        } else {
            holder.isUsedText.setText("New");
        }
    }

    void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    void appendToList(List<CarResponse.Data> data) {
        int oldLength = list.size() - 1;
        list.addAll(data);
        notifyItemRangeChanged(oldLength, data.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CarsViewHolder extends RecyclerView.ViewHolder{

        ImageView carImage;
        TextView brandText;
        TextView isUsedText;
        TextView yearText;

        public CarsViewHolder(@NonNull View itemView) {
            super(itemView);

            carImage = itemView.findViewById(R.id.img_car);
            brandText = itemView.findViewById(R.id.brand_textview);
            isUsedText = itemView.findViewById(R.id.isused_textView);
            yearText = itemView.findViewById(R.id.year_textView);
        }
    }
}
