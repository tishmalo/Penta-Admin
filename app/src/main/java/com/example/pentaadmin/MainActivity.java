package com.example.pentaadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pentaadmin.Model.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    EditText LecName, description;
    CircleImageView patientpicture;
    Button post_pic, proceed;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LecName = findViewById(R.id.LecName);
        description = findViewById(R.id.description);
        patientpicture = findViewById(R.id.patientpicture);
        post_pic = findViewById(R.id.post_pic);
        proceed = findViewById(R.id.proceed);


        choosePic();


    }

    private void choosePic() {

        post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 2) {

            Uri fullPhotouri = data.getData();
            final String photouri = fullPhotouri.getPath().trim();

            Glide.with(MainActivity.this).load(fullPhotouri).into(patientpicture);

            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String VITAL=LecName.getText().toString();

                    if(TextUtils.isEmpty(VITAL)){

                        LecName.setError("Enter Title");
                    }else {


                        if (fullPhotouri != null) {
                            final StorageReference filepath = FirebaseStorage.getInstance().getReference()
                                    .child("profile_images").child(LecName.getText().toString());
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), fullPhotouri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                            byte[] data1 = byteArrayOutputStream.toByteArray();

                            UploadTask uploadTask = filepath.putBytes(data1);

                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                    Toast.makeText(MainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();

                                }
                            });

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {


                                                reference = FirebaseDatabase.getInstance().getReference("notification");
                                                String imageuri = uri.toString();
                                                String TITLE = LecName.getText().toString();
                                                String DESCRIPTION = description.getText().toString();

                                                PostModel pm = new PostModel(imageuri, TITLE, DESCRIPTION);

                                                reference.push().setValue(pm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                                Intent intent = new Intent(MainActivity.this, UpdatePosts.class);
                                                startActivity(intent);
                                                finish();


                                            }
                                        });

                                    }


                                }
                            });


                        }
                    }
                }
            });
        }

    }

}