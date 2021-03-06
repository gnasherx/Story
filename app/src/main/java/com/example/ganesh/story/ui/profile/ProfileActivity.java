package com.example.ganesh.story.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganesh.story.R;
import com.example.ganesh.story.activeStory.BottomNavigationViewHelper;
import com.example.ganesh.story.activeStory.MainActivity;
import com.example.ganesh.story.activeStory.bookmark.BookmarkActivity;
import com.example.ganesh.story.ui.authentication.LoginActivity;
import com.example.ganesh.story.ui.like.StoryLikeActivty;
import com.example.ganesh.story.ui.search.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getName();
    /* private static final int GALLERY_REQUEST = 1;
     private Uri imageUri;
     private ImageView imageView;*/
    String name;
    String username;
    private DatabaseReference mDatabase;
    private StorageReference mStoragereference;
    private FirebaseAuth mAuth;
    private TextView mTextViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeScreen();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference userNamereference = mDatabase.child(user_id);
        Log.e(LOG_TAG, "path:" + userNamereference);
        userNamereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("name").getValue();
                mTextViewName.setText(name);


                username = (String) dataSnapshot.child("username").getValue();
                toolbar.setTitle(username);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, "Error while retriving user name");
            }
        });


      /*  imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }
        });*/


        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                break;
                            case R.id.action_like:
                                startActivity(new Intent(ProfileActivity.this, StoryLikeActivty.class));
                                break;
                            case R.id.action_search:
                                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                                break;
                            case R.id.action_profile:

                                break;
                        }
                        return false;
                    }
                });


    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

    }*/

    private void initializeScreen() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mStoragereference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        //   imageView = (ImageView) findViewById(R.id.image_view_choode_profile);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
            case R.id.action_edit_profile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                break;
            case R.id.action_bookmark:
                startActivity(new Intent(ProfileActivity.this, BookmarkActivity.class));
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
    }

}
