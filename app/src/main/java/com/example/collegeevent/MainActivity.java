package com.example.collegeevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editTextEvent,editTextVenue,editTextDate,editTextLink,editTextStart_time,editTextEnd_time,editTextDesc;
    private Spinner spinnerDept;
    private TextView textViewShow;
    private ImageView imageView;
    ProgressDialog progressDialog1;
    Handler handler;
    StorageTask mstorage;
    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    String[] dept = {"Department","CSE","ECE","CIVIL","MECH"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        editTextLink=findViewById(R.id.Link);
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        imageView =  findViewById(R.id.imageView);
        editTextEvent = findViewById(R.id.event);
        editTextVenue=findViewById(R.id.venue);
        spinnerDept=findViewById(R.id.dept);
        editTextDate= findViewById(R.id.date);
        editTextStart_time=findViewById(R.id.start_time);
        editTextEnd_time=findViewById(R.id.end_time);
        editTextDesc=findViewById(R.id.description);
        textViewShow = findViewById(R.id.textViewShow);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dept);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(spinnerArrayAdapter);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cld = Calendar.getInstance();
                final int day=cld.get(Calendar.DAY_OF_MONTH);
                int month=cld.get(Calendar.MONTH);
                int year=cld.get(Calendar.YEAR);
                DatePickerDialog picker=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth +"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });
        editTextStart_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int time=calendar.get(Calendar.MINUTE);
                TimePickerDialog pickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        editTextStart_time.setText(String.format(hourOfDay+":"+minute)+" "+format);
                    }
                },hour,time,false);
                pickerDialog.show();
            }
        });
        editTextEnd_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int time=calendar.get(Calendar.MINUTE);
                TimePickerDialog pickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        editTextEnd_time.setText(String.format(hourOfDay+":"+minute)+" "+format);
                    }
                },hour,time,false);
                pickerDialog.show();
            }
        });



        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        textViewShow.setOnClickListener(this);

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile_cse() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            progressDialog1=new ProgressDialog(this);
            progressDialog1.setTitle("Updating...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog1.setProgress(0);
            progressDialog1.setMax(100);
            progressDialog1.setCancelable(false);
            progressDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mstorage.cancel();
                    progressDialog1.dismiss();
                }
            });
            progressDialog1.show();
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS_CSE + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
           mstorage= sRef.putFile(filePath)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            if (!editTextLink.getText().toString().startsWith("http://")){
                                editTextLink.setText("http://"+editTextLink.getText());
                            }
                            Upload upload = new Upload(editTextEvent.getText().toString(), url.toString(), editTextVenue.getText().toString()
                                    , spinnerDept.getSelectedItem().toString(), editTextStart_time.getText().toString(), editTextDate.getText().toString(),
                                    editTextLink.getText().toString(), editTextDesc.getText().toString(), editTextEnd_time.getText().toString());

                            mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                            //dismissing the progress dialog
                            progressDialog1.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog1.setProgress(currentProgress);
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog1.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
    private void uploadFile_ece() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            progressDialog1=new ProgressDialog(this);
            progressDialog1.setTitle("Updating...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog1.setProgress(0);
            progressDialog1.setMax(100);
            progressDialog1.setCancelable(false);
            progressDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mstorage.cancel();
                    progressDialog1.dismiss();
                }
            });
            progressDialog1.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (progressDialog1.getProgress()<=progressDialog1.getMax()){
                            Thread.sleep(200);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog1.incrementProgressBy(2);
                                }
                            });
                            if (progressDialog1.getProgress()==progressDialog1.getMax()){
                                progressDialog1.dismiss();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS_ECE + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            mstorage= sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            Upload upload = new Upload(editTextEvent.getText().toString(), url.toString(), editTextVenue.getText().toString()
                                    , spinnerDept.getSelectedItem().toString(), editTextStart_time.getText().toString(), editTextDate.getText().toString(),
                                    editTextLink.getText().toString(), editTextDesc.getText().toString(), editTextEnd_time.getText().toString());

                            mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                            //dismissing the progress dialog
                            progressDialog1.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog1.setProgress(currentProgress);
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog1.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
    private void uploadFile_civil() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            progressDialog1=new ProgressDialog(this);
            progressDialog1.setTitle("Updating...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog1.setProgress(0);
            progressDialog1.setMax(100);
            progressDialog1.setCancelable(false);
            progressDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mstorage.cancel();
                    progressDialog1.dismiss();
                }
            });
            progressDialog1.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (progressDialog1.getProgress()<=progressDialog1.getMax()){
                            Thread.sleep(200);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog1.incrementProgressBy(2);
                                }
                            });
                            if (progressDialog1.getProgress()==progressDialog1.getMax()){
                                progressDialog1.dismiss();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS_CIVIL + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            mstorage= sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            Upload upload = new Upload(editTextEvent.getText().toString(), url.toString(), editTextVenue.getText().toString()
                                    , spinnerDept.getSelectedItem().toString(), editTextStart_time.getText().toString(), editTextDate.getText().toString(),
                                    editTextLink.getText().toString(), editTextDesc.getText().toString(), editTextEnd_time.getText().toString());

                            mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                            //dismissing the progress dialog
                            progressDialog1.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog1.setProgress(currentProgress);
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog1.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
    private void uploadFile_mech() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            progressDialog1=new ProgressDialog(this);
            progressDialog1.setTitle("Updating...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog1.setProgress(0);
            progressDialog1.setMax(100);
            progressDialog1.setCancelable(false);
            progressDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mstorage.cancel();
                    progressDialog1.dismiss();
                }
            });
            progressDialog1.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (progressDialog1.getProgress()<=progressDialog1.getMax()){
                            Thread.sleep(200);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog1.incrementProgressBy(2);
                                }
                            });
                            if (progressDialog1.getProgress()==progressDialog1.getMax()){
                                progressDialog1.dismiss();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS_MECH + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            mstorage= sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            Upload upload = new Upload(editTextEvent.getText().toString(), url.toString(), editTextVenue.getText().toString()
                                    , spinnerDept.getSelectedItem().toString(), editTextStart_time.getText().toString(), editTextDate.getText().toString(),
                                    editTextLink.getText().toString(), editTextDesc.getText().toString(), editTextEnd_time.getText().toString());

                            mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                            //dismissing the progress dialog
                            progressDialog1.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog1.setProgress(currentProgress);
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog1.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

        @Override
        public void onClick (View view){
            if (view == buttonChoose) {
                showFileChooser();
            } else if (view == buttonUpload) {
                String txt_spin=spinnerDept.getSelectedItem().toString();
                if (txt_spin.equals("CSE")){
                    storageReference = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CSE);
                    uploadFile_cse();
                }
                if (txt_spin.equals("ECE")){
                    storageReference = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_ECE);
                    uploadFile_ece();
                }
                if (txt_spin.equals("CIVIL")){
                    storageReference = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CIVIL);
                    uploadFile_civil();
                }
                if (txt_spin.equals("MECH")){
                    storageReference = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_MECH);
                    uploadFile_mech();
                }
            } else if (view == textViewShow) {
                Intent intent = new Intent(getApplicationContext(), ShowImagesActivity.class);
                startActivity(intent);

            }
        }


}

