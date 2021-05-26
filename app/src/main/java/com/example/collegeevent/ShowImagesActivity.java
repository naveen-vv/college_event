package com.example.collegeevent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowImagesActivity extends AppCompatActivity {
    //recyclerview object
    ViewFlipper slider;
    Button cse, ece, mech, civil;
    ImageView imageView1, imageView2, imageView3, imageView4, add;
    private RecyclerView recyclerView;
    private ValueEventListener mDBLitenser;


    private MyAdapter adapter;

    //adapter object
    //private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    //---slides---
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    DatabaseReference silde1=databaseReference.child("Slide1");
    DatabaseReference silde2=databaseReference.child("Slide2");
    DatabaseReference silde3=databaseReference.child("Slide3");
    DatabaseReference silde4=databaseReference.child("Slide4");


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_images);
        add = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cse = findViewById(R.id.cse);
        ece = findViewById(R.id.ece);
        civil = findViewById(R.id.civil);
        mech = findViewById(R.id.mech);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), add);
                popupMenu.inflate(R.menu.add);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Network networkInfo = connectivityManager.getActiveNetwork();
        if (networkInfo != null) {
            progressDialog = new ProgressDialog(this);
            uploads = new ArrayList<>();

            //displaying progress dialog while fetching images
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            recyclerView.setHasFixedSize(true);
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CSE);
            //adding an event listener to fetch values
            mDBLitenser = mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //dismissing the progress dialog
                    uploads.clear();
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        upload.setKey(postSnapshot.getKey());
                        uploads.add(upload);
                    }
                    //creating adapter
                    adapter = new MyAdapter(getApplicationContext(), uploads);
                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });
            cse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploads = new ArrayList<>();
                    //displaying progress dialog while fetching images
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    recyclerView.setHasFixedSize(true);

                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CSE);
                    //adding an event listener to fetch values
                    mDBLitenser = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //dismissing the progress dialog
                            uploads.clear();
                            //iterating through all the values in database
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                uploads.add(upload);
                            }
                            //creating adapter
                            adapter = new MyAdapter(getApplicationContext(), uploads);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);

                            progressDialog.dismiss();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }

                    });

                }
            });
            ece.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploads = new ArrayList<>();

                    //displaying progress dialog while fetching images
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    recyclerView.setHasFixedSize(true);
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_ECE);
                    //adding an event listener to fetch values
                    mDBLitenser = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //dismissing the progress dialog

                            uploads.clear();
                            //iterating through all the values in database
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                uploads.add(upload);
                            }
                            //creating adapter
                            adapter = new MyAdapter(getApplicationContext(), uploads);

                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            });
            civil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploads = new ArrayList<>();

                    //displaying progress dialog while fetching images
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    recyclerView.setHasFixedSize(true);
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CIVIL);
                    //adding an event listener to fetch values
                    mDBLitenser = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //dismissing the progress dialog
                            uploads.clear();
                            //iterating through all the values in database
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                uploads.add(upload);
                            }
                            //creating adapter
                            adapter = new MyAdapter(getApplicationContext(), uploads);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            });
            mech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    uploads = new ArrayList<>();

                    //displaying progress dialog while fetching images
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    recyclerView.setHasFixedSize(true);
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_MECH);
                    //adding an event listener to fetch values
                    mDBLitenser = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //dismissing the progress dialog
                            uploads.clear();
                            //iterating through all the values in database
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                uploads.add(upload);
                            }
                            //creating adapter
                            adapter = new MyAdapter(getApplicationContext(), uploads);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);


                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), Error_connection.class);
            startActivity(intent);
        }

        slider =findViewById(R.id.slider);
        imageView1=findViewById(R.id.imageView1);
        imageView2=findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);
        silde1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });
        silde2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        silde3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        silde4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
