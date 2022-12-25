package com.example.myapplication2.SidePages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.ScrollView.FormsScrollView;
import com.example.myapplication2.ViewPages.ViewProfile;
import com.google.firebase.auth.FirebaseAuth;

public class Search extends AppCompatActivity {

    Spinner mMainTitle_spinner, mSearchField_spinner,mLostFound_spinner;
    EditText mFreeSearch;
    Button mSearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        mSearchButton=findViewById(R.id.SearchButton);
        mFreeSearch=findViewById(R.id.FreeSearch);
        mMainTitle_spinner = findViewById(R.id.MainTitle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MainTitleSpinner, R.layout.support_simple_spinner_dropdown_item);
        mMainTitle_spinner.setAdapter(adapter);
        mLostFound_spinner=findViewById(R.id.LostFoundSpinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.Whathappened, R.layout.support_simple_spinner_dropdown_item);
        mLostFound_spinner.setAdapter(adapter4);
        mSearchField_spinner = findViewById(R.id.SearchCategory);
        //ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.SearchCategoryUserSpinner, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.SearchCategoryFormSpinner, R.layout.support_simple_spinner_dropdown_item);
        mSearchField_spinner.setAdapter(adapter3);
        mMainTitle_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        mLostFound_spinner.setVisibility(View.GONE);
                        mSearchField_spinner.setVisibility(View.GONE);
                        mFreeSearch.setHint("Type eMail..");
                        break;
                    case 2:
                        mSearchField_spinner.setVisibility(View.VISIBLE);
                        mLostFound_spinner.setVisibility(View.VISIBLE);
                        mFreeSearch.setHint("Type..");
                        break;
                    default:
                        mSearchField_spinner.setVisibility(View.GONE);
                        mLostFound_spinner.setVisibility(View.GONE);
                        mFreeSearch.setHint("Type..");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainTitle_spinner.getSelectedItem().equals("Choose User or Form")){
                    ((TextView) mMainTitle_spinner.getSelectedView()).setError("");
                    return;
                }
                if(mLostFound_spinner.getVisibility()==View.VISIBLE&&mLostFound_spinner.getSelectedItem().equals("Choose Lost or Found")){
                    ((TextView) mLostFound_spinner.getSelectedView()).setError("");
                    return;
                }
                if(mLostFound_spinner.getVisibility()==View.VISIBLE&& mSearchField_spinner.getSelectedItem().equals("Choose Field")){
                    ((TextView) mSearchField_spinner.getSelectedView()).setError("");
                    return;
                }
                if(TextUtils.isEmpty(mFreeSearch.getText())){
                    mFreeSearch.setError("Search Value Fill is required");
                    return;
                }
                String mainTitle=mMainTitle_spinner.getSelectedItem().toString();
                Intent intent;
                if(mainTitle.equals("Form")){
                    intent=new Intent(Search.this, FormsScrollView.class);
                    intent.putExtra("LostFound",mLostFound_spinner.getSelectedItem().toString());
                    intent.putExtra("SearchField",mSearchField_spinner.getSelectedItem().toString());
                }
                else{
                    intent=new Intent(Search.this, ViewProfile.class);
                }
                intent.putExtra("FreeSearch",mFreeSearch.getText().toString());
                intent.putExtra("CALLED","Search");
                startActivity(intent);
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
                startActivity(new Intent(getApplicationContext(), Search.class));
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
}
