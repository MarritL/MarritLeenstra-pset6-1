package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {

    // firebase references
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseAnalytics mFirebaseAnalytics;
    public String TAG = "AUTHENTICATION";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public TextView mGoToRegisterView;
    public Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mGoToRegisterView = (TextView) findViewById(R.id.go_to_register);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        // set listeners
        mEmailSignInButton.setOnClickListener(new attemptLoginOnClick());
        mGoToRegisterView.setOnClickListener(new goToRegisterOnClick());
        //mGoToRegisterView.setOnClickListener(new activityChanger());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        mAuth = FirebaseAuth.getInstance();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public class attemptLoginOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            attemptLogin();

            mPasswordView.getText().clear();
        }

    }

    public class goToRegisterOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            SignInActivity.this.startActivity(intent);
        }
    }

    public void attemptLogin() {

        // reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // store values at the time of the login attempt
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // check if password and email are filled in
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            System.out.println("before return");
            return;
        } else {
            // try to sign in the user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                            } else {
                                Toast.makeText(SignInActivity.this, "signed in",
                                        Toast.LENGTH_SHORT).show();

                                // go to mainActivity
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                SignInActivity.this.startActivity(intent);
                            }

                            // ...
                        }
                    });

        }
    }

}
