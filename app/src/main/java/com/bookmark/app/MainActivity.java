package com.bookmark.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmark.Abstract.Book;
import com.bookmark.adapters.BookCardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbRef, dbRefRod;
    DatabaseReference ReadOfTheDayRef;
    View bookOfTheDay_Card;
    RecyclerView recommendationList_Recycler;
    RecyclerView.LayoutManager adapterLayoutManager;

    TextView BookOfTheDay_Label;
    TextView recommendation_Label;

    TextView bookTitle_Card;
    TextView bookAuthor_Card;
    TextView bookDescription_Card;

    FloatingActionButton addBook_Button;
    FloatingActionButton settings_Button;
    ImageView logo_Image;
    ProgressBar splashLoader_ProgressBar;

    BookCardAdapter adapter;

    ArrayList<Book> listOfBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindAllViews();
        hideAllViews();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("books");
        ReadOfTheDayRef = db.getReference().child("bookOfTheDay");
        final String[] bookOfTheDay = {"1"};
        ReadOfTheDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookOfTheDay[0] =snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRefRod = db.getReference().child("books").child(bookOfTheDay[0]);

        dbRefRod.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Book temp = new Book(snapshot.child("title").getValue().toString(),
                        snapshot.child("author").getValue().toString(),
                        snapshot.child("description").getValue().toString());

                bookTitle_Card.setText(temp.getTitle());
                bookAuthor_Card.setText(temp.getAuthor());
                bookDescription_Card.setText(temp.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listOfBooks = new ArrayList<Book>();
        adapterLayoutManager = new LinearLayoutManager(this);
        adapter = new BookCardAdapter(listOfBooks);
        recommendationList_Recycler.setHasFixedSize(true);
        recommendationList_Recycler.setLayoutManager(adapterLayoutManager);
        recommendationList_Recycler.setAdapter(adapter);

        final ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfBooks.clear();
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Book temp = new Book(ds.child("title").getValue().toString(), ds.child("author").getValue().toString(), ds.child("description").getValue().toString());
                    listOfBooks.add(temp);
                }
                adapter.notifyDataSetChanged();
                hideAllViews();
                toggleLogo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong...\nPlease try again later.", Toast.LENGTH_SHORT).show();
            }
        };



        adapter.setOnItemClickListener(new BookCardAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplicationContext(), BookInfoActivity.class).putExtra("index", position));
            }
        });

        bookOfTheDay_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BookInfoActivity.class).putExtra("index", Integer.parseInt(bookOfTheDay[0])));

            }
        });

        addBook_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settings_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        dbRef.addValueEventListener(bookListener);
    }

    private void bindAllViews() {

        BookOfTheDay_Label = findViewById(R.id.BookOfTheDay_Label);
        recommendation_Label = findViewById(R.id.recommendation_Label);

        bookOfTheDay_Card = findViewById(R.id.bookOfTheDay_Card);
        recommendationList_Recycler = findViewById(R.id.recommendationList_Recycler);

        bookTitle_Card = bookOfTheDay_Card.findViewById(R.id.bookTitle_Card) ;
        bookAuthor_Card = bookOfTheDay_Card.findViewById(R.id.bookAuthor_Card);
        bookDescription_Card = bookOfTheDay_Card.findViewById(R.id.bookDescription_Card);

        addBook_Button = findViewById(R.id.addBook_Button);
        settings_Button = findViewById(R.id.settings_Button);

        logo_Image = findViewById(R.id.logo_Image);
        splashLoader_ProgressBar = findViewById(R.id.splashLoader_ProgressBar);
    }

    public void toggleLogo(){
        logo_Image.setVisibility(logo_Image.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        splashLoader_ProgressBar.setVisibility(splashLoader_ProgressBar.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
    }

    public void hideAllViews(){

        BookOfTheDay_Label.setVisibility(BookOfTheDay_Label.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        recommendation_Label.setVisibility(recommendation_Label.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);


        bookOfTheDay_Card.setVisibility(bookOfTheDay_Card.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        recommendationList_Recycler.setVisibility(recommendationList_Recycler.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);

        bookTitle_Card.setVisibility(bookTitle_Card.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        bookAuthor_Card.setVisibility(bookAuthor_Card.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        bookDescription_Card.setVisibility(bookDescription_Card.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);

        addBook_Button.setVisibility(addBook_Button.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
        settings_Button.setVisibility(settings_Button.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
  }

}