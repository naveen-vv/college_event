package com.example.collegeevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashSet;

public class Preview extends AppCompatActivity {
    TextView venue,dept,date,start_time,end_time,Link,description,event_name;
    ImageView Event_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);
        venue=findViewById(R.id.VENUE);
        event_name=findViewById(R.id.Enent_Name);
        Event_img=findViewById(R.id.EVENT_Img);
        dept=findViewById(R.id.DEPT);
        date=findViewById(R.id.DATE);
        start_time=findViewById(R.id.Start_Time);
        end_time=findViewById(R.id.End_Time);
        Link=findViewById(R.id.Link);
        description=findViewById(R.id.Description);

        final Upload upload = (Upload) getIntent().getSerializableExtra("name");
        venue.setText(upload.getVenue());
        event_name.setText(upload.getName());
        dept.setText(upload.getDept());
        date.setText(upload.getDate());
        start_time.setText(upload.getStart_time());
        end_time.setText(upload.getEnd_time());
        Link.setText(upload.getLink());
        description.setText(upload.getDesc());

        Picasso.get().load(upload.getUrl()).into(Event_img);

        Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link=upload.getLink();
                Uri uri=Uri.parse(link);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}
