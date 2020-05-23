package com.example.mycostcalendar;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycostcalendar.model.Cost;

import java.util.List;
import java.util.stream.Collectors;

// The adapter connects the list of data (costs) with the View elements
public class CostListAdapter extends RecyclerView.Adapter<CostListAdapter.CostViewHolder> {

    // LayoutInflator reads a layout XML description and converts it into the corresponding View items
    private final LayoutInflater inflater;
    private List<Cost> costs;
    private OnEditCostListener editListener;

    CostListAdapter(Context context, OnEditCostListener editCostListener) {
        inflater = LayoutInflater.from(context);
        editListener = editCostListener;
    }

    @NonNull
    @Override
    public CostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CostViewHolder holder, final int position) {
        if (costs != null) {
            Cost currentCost = costs.get(position);

            String dayOfTheWeek = (String) DateFormat.format("EEE", currentCost.getDate());
            String day = (String) DateFormat.format("dd", currentCost.getDate());
            holder.day.setText(day);
            holder.dayOfWeek.setText(dayOfTheWeek);
            holder.title.setText(currentCost.getTitle());
            holder.description.setText(currentCost.getDescription());
            holder.price.setText(String.valueOf(currentCost.getPrice()));
        } else { // TODO: is this needed for the empty DB case?
            holder.title.setText("No cost added yet!");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                editListener.editCost(costs.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (costs != null) {
            return costs.size();
        }
        return 0;
    }

    public boolean isCostsEmpty() {
        return costs.isEmpty();
    }

    public Cost getCostAtPosition(int position) {
        return costs.get(position);
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
        notifyDataSetChanged();
    }

    public double getCostsTotal() {
        double total = 0;
        for(int i = 0; i < costs.size(); i++) {
            total += costs.get(i).getPrice();
        }
        return total;
    }

    class CostViewHolder extends RecyclerView.ViewHolder {

        private final TextView day;
        private final TextView dayOfWeek;
        private final TextView title;
        private final TextView description;
        private final TextView price;

        private CostViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.item_day);
            dayOfWeek = itemView.findViewById(R.id.item_day_of_week);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            price = itemView.findViewById(R.id.item_price);
        }
    }
}
