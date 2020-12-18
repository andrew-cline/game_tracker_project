package com.example.gametracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.CustomViewHolder> {
    private ArrayList<GameItem> mGameList;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface  OnItemLongClickListener{
        void onLongItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {mLongListener = listener;}

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView mGameNameTextView;
        public TextView mPlayTimeTextView;
        public TextView mTimeLeftTextView;
        public ImageView mCompletedCheckImage;

        public CustomViewHolder(@NonNull View itemView, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            super(itemView);
            mGameNameTextView = itemView.findViewById(R.id.gameNameTextView);
            mPlayTimeTextView = itemView.findViewById(R.id.playtimeTextView);
            mCompletedCheckImage = itemView.findViewById(R.id.imageViewCompletedCheck);
            mTimeLeftTextView = itemView.findViewById(R.id.TimeLeftTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        longListener.onLongItemClick(position);
                    }
                    return true;
                }
            });
        }
    }

    public LibraryRecyclerAdapter(ArrayList<GameItem> gameList){
        mGameList = gameList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(V, mListener, mLongListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        GameItem currentItem = mGameList.get(position);

        holder.mGameNameTextView.setText(currentItem.getGameName());
        holder.mPlayTimeTextView.setText(currentItem.getPlayTime());
        if(currentItem.getCompleted()){
            holder.mTimeLeftTextView.setVisibility(View.INVISIBLE);
            holder.mCompletedCheckImage.setVisibility(View.VISIBLE);
        }else{
            holder.mTimeLeftTextView.setText(currentItem.getmTimeLeft());
            holder.mTimeLeftTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }
}
