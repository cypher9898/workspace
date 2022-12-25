package com.example.myapplication2.AddPages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddReport extends AppCompatActivity {
    Button mSubmit;
    FirebaseDatabase FBDB;
    DatabaseReference DBRF;
    Spinner mSubject_spinner;
    TextView imageuploadtext;
    EditText mDescription;
    ImageView ObjectImage;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    String DR;
    Uri imageUri;
    StorageReference fileRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FBDB=FirebaseDatabase.getInstance();
        DBRF=FBDB.getReference("report");
        setContentView(R.layout.activity_report);
        mSubject_spinner = findViewById(R.id.subject_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reportsubject, R.layout.support_simple_spinner_dropdown_item);
        mSubject_spinner.setAdapter(adapter);
        mDescription = findViewById(R.id.full_description);
        ObjectImage = findViewById(R.id.ReportImageView);
        fAuth = FirebaseAuth.getInstance();
        mSubmit = findViewById(R.id.reportSubmit);
        imageuploadtext=findViewById(R.id.textView8);
        storageReference = FirebaseStorage.getInstance().getReference();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject_spinner=mSubject_spinner.getSelectedItem().toString();
                String description=mDescription.getText().toString().trim();
                String userID=fAuth.getCurrentUser().getUid();
                if(TextUtils.isEmpty(description)){
                    mDescription.setError("장소를 입력해주세요");
                    return;
                }
                if(subject_spinner.equals("신고내역")){
                    ((TextView)mSubject_spinner.getSelectedView()).setError("");
                    return;
                }
                Map<String,Object> forms = new HashMap<>();
                forms.put("UserID",userID);
                //forms.put("Report Subject",subject_spinner);
                forms.put("Description",description);
                String x= DBRF.child(subject_spinner).push().getKey()+"";

                DBRF.child(subject_spinner).child(x).setValue(forms).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddReport.this,"완료",Toast.LENGTH_SHORT).show();
                        if(ObjectImage!=null) {
                            Log.d(x,"TAG");
                            fileRef = storageReference.child("report/"+x+"/ReportIMG.jpg");
                            uploadImageToFirebase(imageUri);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddReport.this,"실패, 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ObjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.Search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Search.class));
                return true;
            case R.id.refresh:
                Toast.makeText(this,"Refresh",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AddReport.class));
                return true;
            case R.id.Credits:
                Toast.makeText(this,"Credits",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Credits.class));
                return true;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                imageuploadtext.setHint("이미지가 업로드 되었습니다");
            }
        }

    }
    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        //final StorageReference fileRef = storageReference.child("report/"+DR+"/ReportIMG.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(uri).into(ObjectImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
