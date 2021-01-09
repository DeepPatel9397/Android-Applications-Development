package com.example.stockwatch;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private List<Stock> stockList;
    private MainActivity mainActivity;
    private DecimalFormat df = new DecimalFormat("##.##");

    public StockAdapter(List<Stock> stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mConstraintLayout;
        public TextView symbolTextView;
        public TextView companyTextView;
        public TextView priceTextView;
        public TextView changedPriceTextView;
        public ImageView changeImageView;

        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);
            mConstraintLayout = itemView;
            symbolTextView = itemView.findViewById(R.id.Symbol);
            companyTextView = itemView.findViewById(R.id.CompanyName);
            priceTextView = itemView.findViewById(R.id.Price);
            changedPriceTextView= itemView.findViewById(R.id.PriceChange);
            changeImageView= (ImageView) itemView.getViewById(R.id.changeImage);

        }
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row_layout, parent, false);
        v.setOnClickListener(mainActivity);
        v.setOnLongClickListener(mainActivity);
        ViewHolder viewhold = new ViewHolder(v);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Stock stock = stockList.get(i);

        if(Math.abs(stock.getPriceChange())==stock.getPriceChange()){
            viewHolder.symbolTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_green_dark));
            viewHolder.companyTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_green_dark));
            viewHolder.priceTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_green_dark));
            viewHolder.changedPriceTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_green_dark));
            viewHolder.changeImageView.setBackgroundResource(R.drawable.icon_up);

        }else{
            //viewHolder.symbolTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorRedDark));
            viewHolder.symbolTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.companyTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.priceTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.changedPriceTextView.setTextColor(mainActivity.getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.changeImageView.setBackgroundResource(R.drawable.icon_down);
        }

        viewHolder.symbolTextView.setText(stock.getStockSymbol());
        viewHolder.companyTextView.setText(stock.getCompanyName());
        viewHolder.priceTextView.setText(""+stock.getPrice());
        viewHolder.changedPriceTextView.setText(df.format(stock.getPriceChange()) + "(" + df.format(stock.getPercentageChange()) + "%)");
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
