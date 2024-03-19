package com.mv.cp_dbms_master;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GuestsRecyclerAdapter extends RecyclerView.Adapter<GuestsRecyclerAdapter.GuestsRecyclerViewHolder> {

    private List<String> namesOfGuests = new ArrayList<>();
    private List<Long> startTime = new ArrayList<>();
    private List<Long> endTime = new ArrayList<>();
    private List<Integer> totalNumberOfGuests = new ArrayList<>();
    private List<Boolean> parkingRequiredList = new ArrayList<>();
    private List<Integer> parkingTypeList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    GuestsRecyclerAdapter(Context context, List<GuestsClass> guests) {
        this.mInflater = LayoutInflater.from(context);
        for(int i=0; i<guests.size(); i++){
            this.namesOfGuests.add(guests.get(i).nameOfGuest);
            this.startTime.add(guests.get(i).startTime);
            this.endTime.add(guests.get(i).endTime);
            this.totalNumberOfGuests.add(guests.get(i).numberOfGuests);
            this.parkingRequiredList.add(guests.get(i).parkingRequired);
            this.parkingTypeList.add(guests.get(i).parkingType);
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public GuestsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.guests_recyclerrow, parent, false);
        return new GuestsRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(GuestsRecyclerViewHolder holder, int position) {

        holder.recyclerRowGuestName.setText(namesOfGuests.get(position));
        LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime.get(position)), ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime.get(position)), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm  dd-MM");
        holder.recyclerRowTiming.setText(startDate.format(formatter) + "   to   " + endDate.format(formatter));
        holder.recyclerRowNumberOfMembers.setText(String.valueOf(totalNumberOfGuests.get(position)));
        if (parkingRequiredList.get(position) == GuestsClass.PARKING_NOT_REQUIRED){
            holder.recyclerRowParkingTypeImage.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
        }
        else{
            if(parkingTypeList.get(position) == GuestsClass.FOUR_WHEELER){
                holder.recyclerRowParkingTypeImage.setBackgroundResource(R.drawable.ic_baseline_directions_car_24);
            }
            else if(parkingTypeList.get(position) == GuestsClass.TWO_WHEELER){
                holder.recyclerRowParkingTypeImage.setBackgroundResource(R.drawable.ic_baseline_two_wheeler_24);
            }
            else{
                holder.recyclerRowParkingTypeImage.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
            }
        }
        //if(wins.get(position) == MatchClass.MATCH_LOSE){ // LOSS
            holder.recyclerHomeCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF7979")));
        //}
        //else if(wins.get(position) == MatchClass.MATCH_WIN){ // WIN
            holder.recyclerHomeCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#96FF79")));
            //Log.d("QWER", position + " Match Win!");
        //}
        //else { // ELSE
            holder.recyclerHomeCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CACACA")));
        //}
        //Log.d("QWER", "Title : " + titles.get(position));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return namesOfGuests.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class GuestsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerRowGuestName;
        TextView recyclerRowTiming;
        TextView recyclerRowNumberOfMembers;
        CardView recyclerHomeCardView;
        ImageView recyclerRowParkingTypeImage;

        GuestsRecyclerViewHolder(View itemView) {
            super(itemView);
            recyclerRowGuestName = itemView.findViewById(R.id.recyclerRowGuestName);
            recyclerRowTiming = itemView.findViewById(R.id.recyclerRowTiming);
            recyclerRowNumberOfMembers = itemView.findViewById(R.id.recyclerRowNumberOfMembers);
            recyclerHomeCardView = itemView.findViewById(R.id.recyclerHomeCardView);
            recyclerRowParkingTypeImage = itemView.findViewById(R.id.recyclerRowParkingTypeImage);
            //recyclerRowTitle.setMovementMethod(new ScrollingMovementMethod());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return namesOfGuests.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
