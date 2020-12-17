package com.example.gametracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<GameItem> mGameList;

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView mGameNameTextView;
        public TextView mPlayTimeTextView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mGameNameTextView = itemView.findViewById(R.id.gameNameTextView);
            mPlayTimeTextView = itemView.findViewById(R.id.playtimeTextView);
        }
    }

    public CustomAdapter(ArrayList<GameItem> gameList){
        mGameList = gameList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(V);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        GameItem currentItem = mGameList.get(position);

        holder.mGameNameTextView.setText(currentItem.getGameName());
        holder.mPlayTimeTextView.setText(currentItem.getPlayTime());
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }
}
