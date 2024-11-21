package com.example.travel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel.Activity.DetailActivity;
import com.example.travel.Domain.ItemDomain;
import com.example.travel.databinding.ViewholderPopularBinding;
import com.example.travel.databinding.ViewholderRecommendedBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<ItemDomain> items;
    Context context;
    ViewholderPopularBinding binding;
    private String selectedCurrency = "LKR"; // Default currency

    public PopularAdapter(ArrayList<ItemDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderPopularBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();

        // Retrieve the selected currency from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        selectedCurrency = prefs.getString("selected_currency", "LKR");

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        ItemDomain currentItem = items.get(position);
        updatePriceText(currentItem.getPrice());
        binding.titleTxt.setText(items.get(position).getTitle());
        //binding.priceTxt.setText("LKR " + items.get(position).getPrice());
        binding.addressTxt.setText(items.get(position).getAddress());
        binding.scoreTxt.setText("" + items.get(position).getScore());

        Glide.with(context)
                .load(items.get(position).getPic())
                .into(binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void updatePriceText(int price) {
        if ("USD".equals(selectedCurrency)) {
            double priceInUSD = convertLKRtoUSD((int) price);
            binding.priceTxt.setText("USD " + String.format("%.2f", priceInUSD));
        } else {
            // Default is LKR if no conversion is needed
            binding.priceTxt.setText("LKR " + price);
        }
    }

    private double convertLKRtoUSD(int lkrPrice) {
        double exchangeRate = 0.0034; //LKR to USD
        return lkrPrice * exchangeRate;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
        }
    }
}
