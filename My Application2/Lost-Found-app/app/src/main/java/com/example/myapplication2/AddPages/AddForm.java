package com.example.myapplication2.AddPages;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.ClassObject.ObjectForm;
import com.example.myapplication2.ClassObject.ObjectUser;
import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddForm extends AppCompatActivity  {
    public static final String TAG = "TAG";
    EditText mObject, mDescription,mPlace;
    Button mSubmit;
    FirebaseDatabase FBDB;
    DatabaseReference DBRF;
    Spinner Happened_spinner, Category_spinner, Status_spinner;
    ImageView ObjectImage;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    Uri imageUri;
    String GeneratedKey,happened,category;
    StorageReference fileRef;
    TextView mDate, imageuploadtext,StatusText;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Intent intent=getIntent();
        ObjectForm CurrForm=(ObjectForm) intent.getSerializableExtra("ObjectForm");
        if(CurrForm!=null) {
            GeneratedKey = CurrForm.getGeneratedKey();
        }
        FBDB=FirebaseDatabase.getInstance();
        DBRF=FBDB.getReference("forms");
        storageReference = FirebaseStorage.getInstance().getReference();
        Happened_spinner = findViewById(R.id.spinner1);
        Status_spinner = findViewById(R.id.spinner4);
        Category_spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.Whathappened, R.layout.support_simple_spinner_dropdown_item);
        Happened_spinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter3= ArrayAdapter.createFromResource(this, R.array.status, R.layout.support_simple_spinner_dropdown_item);
        Status_spinner.setAdapter(adapter3);
        ArrayAdapter<CharSequence> adapter2= ArrayAdapter.createFromResource(this, R.array.Category, R.layout.support_simple_spinner_dropdown_item);
        Category_spinner.setAdapter(adapter2);
        mObject = findViewById(R.id.ObjectName);
        mPlace=findViewById(R.id.place);
        mDescription=findViewById(R.id.description);
        mSubmit = findViewById(R.id.submit);
        ObjectImage = findViewById(R.id.ObjectImageView);
        fAuth = FirebaseAuth.getInstance();
        imageuploadtext=findViewById(R.id.textView7);
        mDate = (TextView) findViewById(R.id.date);
        StatusText=findViewById(R.id.textView25);

        if(intent.getStringExtra("CALLED").equals("Main")){
            Status_spinner.setVisibility(View.GONE);
            StatusText.setVisibility(View.GONE);
        }
        mDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddForm.this, mDateSetListener, year,month,day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDate.setText(date);
            }
        };
        if(GeneratedKey !=null) {
            //Log.d("x is:", x);
                    mObject.setText(CurrForm.getObjectTitle());
                    mDate.setText(CurrForm.getDate());
                    mDescription.setText(CurrForm.getDescription());
                    mPlace.setText(CurrForm.getPlace());
                    int status=adapter3.getPosition(CurrForm.getStatus());
                    int lostfound = adapter.getPosition(CurrForm.getHappend());
                    int categ = adapter2.getPosition(CurrForm.getCategory());
/*                    Log.d(String.valueOf(databaseReference.getParent().getParent()), String.valueOf(databaseReference.getParent()));
                    Log.d("Happened_spinner: " + lostfound,"category_spinner: " + categ);*/
                    Happened_spinner.setSelection(lostfound);
                    Category_spinner.setSelection(categ);
                    Status_spinner.setSelection(status);
                    imgUri=CurrForm.getImg();
                    if(imgUri!=null){
                        imageuploadtext.setHint("이미지가 업로드 되었습니다!");
                    }

        }
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String object = mObject.getText().toString().trim();
                happened = Happened_spinner.getSelectedItem().toString();
                category = Category_spinner.getSelectedItem().toString();
                String userID = fAuth.getCurrentUser().getUid();
                String place = mPlace.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String date = mDate.getText().toString().trim();
                String status=Status_spinner.getSelectedItem().toString();
                if(Status_spinner.getVisibility()!=View.GONE&&status.equals("상태")){
                    ((TextView) Status_spinner.getSelectedView()).setError("");
                    return;
                }
                if (TextUtils.isEmpty(object)) {
                    mObject.setError("물건이름을 입력해주세요");
                    return;
                }
                if (TextUtils.isEmpty(place)) {
                    mPlace.setError("장소를 입력해주세요");
                    return;
                }
                if (happened.equals("분실 또는 습득")) {
                    ((TextView) Happened_spinner.getSelectedView()).setError("");
                    return;
                }
                if (category.equals("카테고리")) {
                    ((TextView) Category_spinner.getSelectedView()).setError("");
                    return;
                }
                if(GeneratedKey ==null) {
                    GeneratedKey = DBRF.child(happened).child(category).push().getKey() + "";
                }
                Map<String, Object> forms = new HashMap<>();
                forms.put("UserID", userID);
                forms.put("ObjectTitle", object);
                //forms.put("Lost or Found",happened);
                //forms.put("Category",category);
                forms.put("place", place);
                forms.put("description", description);
                forms.put("date", date);
//                if(imageUri!=null){
//                    forms.put("imgUrl",imageUri.toString());
//                }
//                else{
//                    forms.put("imgUrl","");
//                }
                if(Status_spinner.getVisibility()!=View.GONE){
                    forms.put("status",status);
                }
                else {
                    forms.put("status", "활성");
                }

                //forms.put("Generated Key",x);
                //DBRF.child(happened).child(category).push().setValue(forms);
                //fStore.collection("forms").document(happened).collection(category).add(forms).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                    DBRF.child(happened).child(category).child(GeneratedKey).setValue(forms).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddForm.this, "완료", Toast.LENGTH_SHORT).show();
                            if (ObjectImage != null) {
                                fileRef = storageReference.child("forms/" + GeneratedKey + "/ObjectIMG.jpg");
                                uploadImageToFirebase(imageUri);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                return;

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddForm.this, "실패, 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    });


            }

        });
        ObjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
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
                startActivity(new Intent(getApplicationContext(),AddForm.class));
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
        // upload image to firebase storage
        //final StorageReference fileRef = storageReference.child("forms/"+DR+"/ObjectIMG.jpg");
        if(imageUri!=null) {
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        imgUri= uri.toString();
                        DBRF.child(happened).child(category).child(GeneratedKey).child("img").setValue(imgUri);
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
}
