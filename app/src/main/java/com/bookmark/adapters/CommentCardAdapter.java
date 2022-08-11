package com.bookmark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookmark.Abstract.Comment;
import com.bookmark.app.R;
import com.bookmark.Abstract.Comment;

import java.io.PipedOutputStream;
import java.util.ArrayList;

public class CommentCardAdapter extends RecyclerView.Adapter<CommentCardAdapter.CommentCardViewHolder> {

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    private ArrayList<Comment> mItemList;

    private onItemClickListener mListener;

    public static class CommentCardViewHolder extends RecyclerView.ViewHolder{

        public TextView CommentBody_viewHolder;
        public ProgressBar CommentRating_viewHolder;
        public TextView CommentDesc_viewHolder;

        public CommentCardViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            CommentBody_viewHolder = itemView.findViewById(R.id.commentBody_Card);
            CommentRating_viewHolder = itemView.findViewById(R.id.commentRating_Card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public CommentCardAdapter(ArrayList<Comment> inventoryList) {
        mItemList = inventoryList;
    }

    @NonNull
    @Override
    public CommentCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        CommentCardViewHolder invHolder = new CommentCardViewHolder(v, mListener);
        return invHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentCardViewHolder holder, int position) {
        Comment currentItem = mItemList.get(position);

        holder.CommentBody_viewHolder.setText(currentItem.getBody());
        holder.CommentRating_viewHolder.setProgress(currentItem.rating);

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}