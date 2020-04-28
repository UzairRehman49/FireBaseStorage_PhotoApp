package com.example.demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    Button upload;
    Uri imageUri;
    Button move;
    ImageView image;
    private static final int iPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.display);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Images/");
        ref = database.getReference("uploads");
        upload = findViewById(R.id.btnUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, iPick);
            }
        });
        move = findViewById(R.id.moveScreen);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, show.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "Uploading image", Toast.LENGTH_SHORT).show();
        if (requestCode == iPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }

    }

    public void uploadImage(View view)
    {


        if(imageUri!=null)
        {
            StorageReference imgRef = mStorageRef.child(imageUri.getLastPathSegment());
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            createNotificationChaneel();
//                            UploadData upload = new UploadData(imageUri.getLastPathSegment(),taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
//                            String uploadid = ref.push().getKey();
//                            ref.child(uploadid).setValue(upload);
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                            UploadData upload = new UploadData(imageUri.getLastPathSegment(), downloadUrl.toString());
                            String uploadId = ref.push().getKey();
                            ref.child(uploadId).setValue(upload);
                            Glide.with(MainActivity.this).load(imageUri).into(image);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No image selected. Select an image", Toast.LENGTH_SHORT).show();
        }


    }


    public void createNotificationChaneel() {

        String name = "Upload Channel";
        String channelid = "1";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        int notification_id = 1122;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel uploadChannel = new NotificationChannel(channelid, name, importance);

            uploadChannel.enableLights(true);
            uploadChannel.setLightColor(Color.BLUE);
            uploadChannel.enableVibration(true);
            uploadChannel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(uploadChannel);
            }

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelid)
                .setContentTitle("Upload Complete")
                .setContentText("Image has been uploaded to Firebase")
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(getResources().getColor(android.R.color.holo_blue_light));


        if (notificationManager != null) {
            notificationManager.notify(notification_id, builder.build());
        }

    }


}