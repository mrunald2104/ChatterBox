package com.example.fire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fire.databinding.ActivityMain2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private static  int PICK_IMAGE=123;
    private Uri imagepath;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading message");
        dialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = database.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent , PICK_IMAGE);
            }
        });

        binding.setupProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString();

                if(name.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Please type a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();

                if(imagepath != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(imagepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        String uid = auth.getUid();
                                        String id=auth.getCurrentUser().getEmail();
                                        String name= binding.nameBox.getText().toString();

                                        User us=new User(uid , name , id , imageUrl);

                                        database.getReference()
                                                .child("user")
                                                .child(uid)
                                                .setValue(us)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Toast.makeText(MainActivity2.this , "Done !" , Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MainActivity2.this , homeScreenActivity2.class));
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }

                        }
                    });
                } else {


                    String uid = auth.getUid();
                    String id=auth.getCurrentUser().getEmail();

                    User us=new User(uid , name , id , "no image");

                    database.getReference()
                            .child("user")
                            .child(uid)
                            .setValue(us)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity2.this , "Done !" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity2.this , homeScreenActivity2.class));
                                    finish();
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == PICK_IMAGE && resultCode== RESULT_OK) {
            imagepath=data.getData();
            binding.profilePic.setImageURI(imagepath);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}