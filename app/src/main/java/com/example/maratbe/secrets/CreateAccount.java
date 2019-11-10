package com.example.maratbe.secrets;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import java.util.Arrays;


public class CreateAccount extends AppCompatActivity implements Constants, View.OnClickListener, View.OnTouchListener{
    private LinearLayout mainLayout, progressBarLayout, formLayout;
    private LinearLayout.LayoutParams lParams;
    private EditText userEdit;
    private Theme theme;
    private TextView statusUserTxt, displayTxt;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ShowAlertDialog alertDialog;
    private CreateAccount account;
    private String email = "", method = "";
    private char idType = ' ';
    private static int RC_SIGN_IN = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        method = getIntent().getStringExtra("METHOD");
        account = this;
        setContentView(R.layout.activity_add_account);
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        LoginManager.getInstance().logOut();
        theme = TabNavigator.getThemeAt(MainActivity.getActiveThemeNumber());
        mainLayout = (LinearLayout) findViewById(R.id.main_create_account_layout);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setOnTouchListener(this);
        createProgressBar();
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenWidth(),(int)(MainActivity.getScreenHeight()*0.75));
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(lParams);
        relativeLayout.setGravity(Gravity.CENTER);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        formLayout = createLinearLayout(LinearLayout.VERTICAL, Color.TRANSPARENT, 0, 0, View.VISIBLE, 11);
        if (method.equals("register"))
        {
            setRegisterFields();
            Util.openKeyboard(this);
        }
        else
        {
            setLoginFields();
        }
        mainLayout.addView(relativeLayout);

        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(MainActivity.getScreenWidth(),(int)(MainActivity.getScreenHeight()*0.75)) ;
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(formLayout, rParams);
        relativeLayout.addView(progressBarLayout, rParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void createProgressBar() {
        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenHeight()/7, MainActivity.getScreenHeight()/7);
        lParams.setMargins(0,0,100,0);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(lParams);
        progressBar.setBackgroundColor(Color.TRANSPARENT);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(theme.getSelectedTitleColor()[2], android.graphics.PorterDuff.Mode.MULTIPLY);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView txt = createTextView(R.string.loading_user_data, 15, Color.BLACK);
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = createLinearLayout(LinearLayout.HORIZONTAL, ContextCompat.getColor(this, R.color.ultra_light_gray), 0, 0, View.VISIBLE, 12);
        layout.setGravity(Gravity.CENTER);
        layout.addView(progressBar);
        layout.addView(txt);
        layout.setPadding(40,0,40,0);
        progressBarLayout = new LinearLayout(this);
        progressBarLayout.setVisibility(View.GONE);
        progressBarLayout.setGravity(Gravity.CENTER);
        progressBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        progressBarLayout.addView(layout);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            email = account.getEmail();
            String fName = account.getGivenName();
            String lName = account.getFamilyName();
            String id = account.getId();
            idType = 'G';
            displayTxt.setText("Sign in with Google:\nEmail = "+email+"\nName = "+fName+" "+lName + "\nId = "+ id+"\nId type = "+idType);
            saveUserData();
        } catch (ApiException e) {
            Log.w("Google Exception", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void setRegisterFields() {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20, 70, 20, 60);
        TextView title = createTextView(R.string.add_account_register_title, TEXT_SIZE + 12, ContextCompat.getColor(this, R.color.colorPrimary));
        title.setTypeface(theme.getMainTypeface());

        lParams = new LinearLayout.LayoutParams(MainActivity.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(10, 0, 10, 50);
        TextView remarkTxt = createTextView(R.string.create_account_remark, TEXT_SIZE - 5, ContextCompat.getColor(this, R.color.light_black));
        remarkTxt.setPadding(40, 10, 40, 10);
        remarkTxt.setBackgroundColor(ContextCompat.getColor(this, R.color.ultra_light_gray));

        mainLayout.addView(title);
        mainLayout.addView(remarkTxt);

        createNickname();
        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.8), MainActivity.getScreenHeight()/13);
        lParams.setMargins(0, 0, 0, 20);
        createButton(R.id.account_facebook_login_btn, R.drawable.facebook_signin);
        createButton(R.id.account_google_signin_btn, R.drawable.google_signin);

        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.91), LinearLayout.LayoutParams.WRAP_CONTENT);
        displayTxt = new TextView(this);
        displayTxt.setLayoutParams(lParams);
        mainLayout.addView(displayTxt);
    }

    private void setLoginFields() {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(20, 80, 20, 80);
        TextView title = createTextView(R.string.add_account_login_title, TEXT_SIZE + 12, ContextCompat.getColor(this, R.color.colorPrimary));
        title.setTypeface(theme.getMainTypeface());
        mainLayout.addView(title);

        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.8), MainActivity.getScreenHeight()/13);
        lParams.setMargins(0, 0, 0, 20);
        createButton(R.id.account_facebook_login_btn, R.drawable.facebook_signin);
        createButton(R.id.account_google_signin_btn, R.drawable.google_signin);

        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.91), LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0, 0, 0, 60);
        displayTxt = new TextView(this);
        displayTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        displayTxt.setLayoutParams(lParams);
        mainLayout.addView(displayTxt);
    }

    private void createNickname() {
        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.85), MainActivity.getScreenHeight() / 12);
        lParams.gravity = Gravity.CENTER;
        lParams.setMargins(0, 0, 0, 50);
        LinearLayout layout = createLinearLayout(LinearLayout.HORIZONTAL, Color.TRANSPARENT, 0, 10, View.VISIBLE, 10);
        userEdit = createEditText(R.string.account_nickname_name, R.id.add_account_nickname_edit);
        TextInputLayout editLayout = CreateTextInputLayout(userEdit, 20);
        statusUserTxt = createTextView(R.id.add_account_user_txt, 0);
        layout.addView(editLayout);
        layout.addView(statusUserTxt);
        formLayout.addView(layout);
    }

    private TextInputLayout CreateTextInputLayout(EditText edit, int id) {
        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER_VERTICAL;
        TextInputLayout editLayout = new TextInputLayout(this);
        editLayout.addView(edit);
        editLayout.setId(id);
        editLayout.setLayoutParams(lParams);
        return editLayout;
    }

    private LinearLayout createLinearLayout(int orientation, int color, int border, int radius, int visible, int id) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(orientation);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setVisibility(visible);
        layout.setBackground(Util.createBorder(radius, color, false, null, border));
        layout.setLayoutParams(lParams);
        layout.setId(id);
        layout.setClickable(true);
        layout.setOnClickListener(this);
        return layout;
    }

    private TextView createTextView(int text, int size, int color) {
        TextView txt = new TextView(this);
        txt.setTextColor(color);
        txt.setText(text);
        txt.setTextSize(size);
        txt.setLayoutParams(lParams);
        return txt;
    }

    private TextView createTextView(int id, int image) {
        lParams = new LinearLayout.LayoutParams((int) ((MainActivity.getScreenWidth() * 0.85) - (MainActivity.getScreenWidth() * 0.80)),
                MainActivity.getScreenHeight() / 30);
        lParams.gravity = Gravity.CENTER;
        TextView txt = new TextView(this);
        txt.setId(id);
        txt.setLayoutParams(lParams);
        setBackground(txt, image);
        return txt;
    }

    private EditText createEditText(int text, int id) {
        lParams = new LinearLayout.LayoutParams((int) (MainActivity.getScreenWidth() * 0.8), MainActivity.getScreenHeight() / 14);
        lParams.gravity = Gravity.CENTER_VERTICAL;
        lParams.setMargins(10, 0, 0, 0);
        EditText edit = new EditText(this);
        edit.setLayoutParams(lParams);
        edit.setHint(text);
        edit.setId(id);
        edit.setTextColor(ContextCompat.getColor(this, R.color.light_black));
        edit.setPadding(20, 0, 20, 0);
        edit.setFocusableInTouchMode(true);
        edit.setOnTouchListener(this);
        return edit;
    }

    private void createButton(int id, int image) {
        Button button = new Button(this);
        button.setPadding(0, 0, 0, 0);
        button.setLayoutParams(lParams);
        button.setBackground(ContextCompat.getDrawable(this, image));
        button.setId(id);
        button.setOnClickListener(this);
        formLayout.addView(button);
    }

    private void saveUserData() {
        if (method.equals("register")) {
            if (userEdit.getText().toString().equals("")) {
                Toast.makeText(this, "User name is mandatory", Toast.LENGTH_LONG).show();
                setBackground(statusUserTxt, R.drawable.x);
            } else {
                setBackground(statusUserTxt, R.drawable.check);
                MainActivity.setUser(new User(userEdit.getText().toString(), email, idType), 0);
                if (MainActivity.getDbInstance().insertUser(MainActivity.getUser(0)) == 0) {
                    MainActivity.getLocalStorage().storeUserData(MainActivity.getUser(0));
                    Toast.makeText(this, "Welcome to secrets " + userEdit.getText().toString(), Toast.LENGTH_LONG).show();
                    TabNavigator.getTabNavigatorInstance().updateLogin(true);
                    this.finish();
                } else {
                    setBackground(statusUserTxt, R.drawable.x);
                    Toast.makeText(this, "User with this username already exists, pick different username", Toast.LENGTH_LONG).show();
                }
            }
            Util.closeKeyboard(userEdit, this);
        } else {
            if (MainActivity.getDbInstance().selectLoginUser(email, idType) == 1) {
                MainActivity.getUser(0).logIn(true);
               // MainActivity.getLocalStorage().setUserData(MainActivity.getUser(0));
               // MainActivity.getLocalStorage().storeUserData(MainActivity.getUser(0));
                new ProgressTask().execute();
            } else {

                String str1 = idType == 'F' ? "Facebook" : "Google";
                String str2 = idType == 'F' ? "Google" : "Facebook";
                String body = "Could not login with " + str1 + "\nThe email is not registered, you can try to login with " + str2;
                alertDialog = new ShowAlertDialog(this) {
                    @Override
                    protected void buttonPressed(int id) {
                        if (id == 0) {
                            Intent intent = new Intent(account, CreateAccount.class);
                            intent.putExtra("METHOD", "register");
                            startActivity(intent);
                            account.finish();
                        } else if (id == 2) {
                            account.finish();
                        }
                        alertDialog.dismiss();
                    }
                };
                alertDialog.setTexts("Login error", body, new int[]{R.string.register, R.string.sign_in, R.string.not_now});
                alertDialog.setCanceledOnTouchOutside(false);
                Util.setDialogColors(account, alertDialog);
                alertDialog.show();
            }
        }
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setFacebookData(loginResult);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }

    public void setFacebookData(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            try {
                Log.i("Response", response.toString());
                email = object.getString("email");
                String firstName = object.getString("first_name");
                String lastName = object.getString("last_name");
                String gender = "";
                if(object.has("birthday")){
                    gender = object.getString("gender");
                }


                Profile profile = Profile.getCurrentProfile();
                String id = profile.getId();
                idType = 'F';
                displayTxt.setText("Sign in with Facebook:\nEmail = "+email+"\nName = "+firstName+" "+lastName +
                        "\nId = "+ id+"\nId type = "+idType);
                String link = profile.getLinkUri().toString();
                Log.i("Link", link);
                if (Profile.getCurrentProfile() != null) {
                    Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                }

                Log.i("Login" + "Email", email);
                Log.i("Login" + "FirstName", firstName);
                Log.i("Login" + "LastName", lastName);
                Log.i("Login" + "Gender", gender);
                saveUserData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.add_account_private_radio:
                privateFormLayout.setVisibility(View.VISIBLE);
               // publicFormLayout.setVisibility(View.GONE);
                ((TextView)mainLayout.getChildAt(1)).setText(R.string.private_account_remark); break;

            case R.id.add_account_public_radio:
               // privateFormLayout.setVisibility(View.VISIBLE);
               // publicFormLayout.setVisibility(View.VISIBLE);
                ((TextView)mainLayout.getChildAt(1)).setText(R.string.public_account_remark); break;*/
            case R.id.account_facebook_login_btn:
                signInWithFacebook();
                break;
            case R.id.account_google_signin_btn:
         //       requestCode = RC_SIGN_IN;
                signInWithGoogle();
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Util.closeKeyboard(userEdit, this);
        return false;
    }

    public void setBackground(TextView txt, int image) {
        if (image != 0) {
            txt.setBackground(ContextCompat.getDrawable(this, image));
        } else {
            txt.setBackground(null);
        }
    }

    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MainActivity.selectUserData(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBarLayout.setVisibility(View.GONE);
            Toast.makeText(account, "Welcome to secrets " + MainActivity.getUser(0).getUserName(), Toast.LENGTH_LONG).show();
            TabNavigator.getTabNavigatorInstance().updateLogin(true);
            account.finish();
        }
    }
}
