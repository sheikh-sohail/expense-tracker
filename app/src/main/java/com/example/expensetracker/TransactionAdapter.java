package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import java.util.Locale;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final Context context;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactions.get(position);

        holder.tvName.setText(t.getName());
        holder.tvDate.setText(t.getDate());

        if (t.isIncome()) {
            holder.tvAmount.setText(String.format(Locale.US, "₹%.2f", t.getAmount()));
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income_green));
        } else {
            holder.tvAmount.setText(String.format(Locale.US, "-₹%.2f", t.getAmount()));
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_red));
        }

        // Set icon background color and letter
        String name = t.getName().toLowerCase();
        int bgColor;
        switch (name) {
            case "salary":       bgColor = Color.parseColor("#2D9596"); break;
            case "shopping":     bgColor = Color.parseColor("#8BC34A"); break;
            case "grocery":      bgColor = Color.parseColor("#FF9800"); break;
            case "food":           bgColor = Color.parseColor("#F4511E"); break;
            case "transport":      bgColor = Color.parseColor("#039BE5"); break;
            case "health":         bgColor = Color.parseColor("#E53935"); break;
            case "entertainment":  bgColor = Color.parseColor("#8E24AA"); break;
            case "electricity":    bgColor = Color.parseColor("#FFB300"); break;
            case "rent":           bgColor = Color.parseColor("#6D4C41"); break;
            default:             bgColor = Color.parseColor("#2D9595"); break;
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(24f);
        bg.setColor(bgColor);
        holder.ivIcon.setBackground(bg);

        // First letter
        holder.tvIconLetter.setText(String.valueOf(t.getName().charAt(0)).toUpperCase());
    }

    @Override
    public int getItemCount() { return transactions.size(); }

    public void updateData(List<Transaction> newData) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return transactions.size(); }

            @Override
            public int getNewListSize() { return newData.size(); }

            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                return transactions.get(oldPos).getId() == newData.get(newPos).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                Transaction o = transactions.get(oldPos);
                Transaction n = newData.get(newPos);
                return o.getName().equals(n.getName())
                        && o.getAmount() == n.getAmount()
                        && o.getDate().equals(n.getDate())
                        && o.getType().equals(n.getType());
            }
        });

        this.transactions = newData;
        result.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvDate, tvAmount, tvIconLetter;

        ViewHolder(View v) {
            super(v);
            ivIcon    = v.findViewById(R.id.iv_icon);
            tvName    = v.findViewById(R.id.tv_name);
            tvDate    = v.findViewById(R.id.tv_date);
            tvAmount  = v.findViewById(R.id.tv_amount);
            tvIconLetter = v.findViewById(R.id.tv_icon_letter);
        }
    }
}