package com.bookmark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookmark.Abstract.Book;
import com.bookmark.app.R;

import java.io.PipedOutputStream;
import java.util.ArrayList;

public class BookCardAdapter extends RecyclerView.Adapter<BookCardAdapter.BookCardViewHolder> {

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    private ArrayList<Book> mItemList;

    private onItemClickListener mListener;

    public static class BookCardViewHolder extends RecyclerView.ViewHolder{

        public TextView bookTitle_viewHolder;
        public TextView bookAuthor_viewHolder;
        public TextView bookDesc_viewHolder;

        public BookCardViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            bookTitle_viewHolder = itemView.findViewById(R.id.bookTitle_Card);
            bookAuthor_viewHolder = itemView.findViewById(R.id.bookAuthor_Card);
            bookDesc_viewHolder = itemView.findViewById(R.id.bookDescription_Card);


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

    public BookCardAdapter(ArrayList<Book> inventoryList) {
        mItemList = inventoryList;
    }

    @NonNull
    @Override
    public BookCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
        BookCardViewHolder invHolder = new BookCardViewHolder(v, mListener);
        return invHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookCardViewHolder holder, int position) {
        Book currentItem = mItemList.get(position);

        holder.bookTitle_viewHolder.setText(currentItem.getTitle());
        holder.bookAuthor_viewHolder.setText(currentItem.getAuthor());
        holder.bookDesc_viewHolder.setText(currentItem.getDescription());

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}