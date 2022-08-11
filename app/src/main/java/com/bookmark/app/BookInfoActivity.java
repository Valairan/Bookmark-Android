package com.bookmark.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmark.Abstract.Book;
import com.bookmark.Abstract.Comment;
import com.bookmark.adapters.BookCardAdapter;
import com.bookmark.adapters.CommentCardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;

public class BookInfoActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbRef;
    DatabaseReference dbRefComment;

    View bookOfTheDayInfo_Card;
    RecyclerView recommendationList_Recycler;
    RecyclerView.LayoutManager adapterLayoutManager;

    ImageView bookThumbsInfo_Image;
    TextView bookTitle_Card;
    TextView bookAuthor_Card;
    TextView bookDescription_Card;


    CommentCardAdapter adapter;

    ArrayList<Comment> listOfComments;


    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        bindAllViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
            //The key argument here must match that used in the other activity
        }

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("books").child(String.valueOf(index));
        dbRefComment = db.getReference().child("books").child(String.valueOf(index)).child("comments");


        final String[] index = {"1"};
        final int[] rating = {0};
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //index[0] =snapshot.getValue().toString();
                Book temp = new Book(snapshot.child("title").getValue().toString(),
                        snapshot.child("author").getValue().toString(),
                        snapshot.child("description").getValue().toString());

                rating[0] = Integer.parseInt(snapshot.child("rating").getValue().toString());

                bookTitle_Card.setText(temp.getTitle());
                bookAuthor_Card.setText(temp.getAuthor());
                bookDescription_Card.setText(temp.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listOfComments = new ArrayList<Comment>();
        adapterLayoutManager = new LinearLayoutManager(this);
        adapter = new CommentCardAdapter(listOfComments);
        recommendationList_Recycler.setHasFixedSize(true);
        recommendationList_Recycler.setLayoutManager(adapterLayoutManager);
        recommendationList_Recycler.setAdapter(adapter);

        final ValueEventListener commentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfComments.clear();
                int index = 0;
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Log.e("---------->", ds.toString());
                    Comment temp = new Comment(ds.getValue().toString(), rating[0]);
                    listOfComments.add(temp);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong...\nPlease try again later.", Toast.LENGTH_SHORT).show();
            }
        };


        dbRefComment.addValueEventListener(commentListener);


    }
    void bindAllViews() {

        bookOfTheDayInfo_Card = findViewById(R.id.bookOfTheDayInfo_Card);
        recommendationList_Recycler = findViewById(R.id.commentList_Recycler);

        bookTitle_Card = bookOfTheDayInfo_Card.findViewById(R.id.bookTitle_Card) ;
        bookAuthor_Card = bookOfTheDayInfo_Card.findViewById(R.id.bookAuthor_Card);
        bookDescription_Card = bookOfTheDayInfo_Card.findViewById(R.id.bookDescription_Card);


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}