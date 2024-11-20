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
import com.example.travel.databinding.ViewholderRecommendedBinding;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.Viewholder> {
    ArrayList<ItemDomain> items;
    Context context;
    ViewholderRecommendedBinding binding;
    private String selectedCurrency = "LKR"; // Default currency

    // Constructor to accept items and selected currency
    public RecommendedAdapter(ArrayList<ItemDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecommendedAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();

        // Retrieve the selected currency from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        selectedCurrency = prefs.getString("selected_currency", "LKR");

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedAdapter.Viewholder holder, int position) {

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
    // Method to update the price text based on the selected currency
    private void updatePriceText(double price) {
        if ("USD".equals(selectedCurrency)) {
            double priceInUSD = convertLKRtoUSD(price);
            binding.priceTxt.setText("USD " + String.format("%.2f", priceInUSD));
        } else {
            // Default is LKR if no conversion is needed
            binding.priceTxt.setText("LKR " + price);
        }
    }

    // Placeholder method for LKR to USD conversion
    private double convertLKRtoUSD(double lkrPrice) {
        double exchangeRate = 0.0031; // Example conversion rate from LKR to USD
        return lkrPrice * exchangeRate;
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(ViewholderRecommendedBinding binding) {
            super(binding.getRoot());
        }
    }
}
