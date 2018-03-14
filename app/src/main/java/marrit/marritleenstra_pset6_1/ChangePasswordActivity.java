package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    // variables
    private String TAG = "CHANGEPASSWORDACTIVITY";

    // UI references
    EditText mNewPassword;
    Button mChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // initiate UI references
        mNewPassword = (EditText) findViewById(R.id.ET_password);
        mChangePassword = (Button) findViewById(R.id.BUTTON_change_password);

        mChangePassword.setOnClickListener(new changePasswordOnClick());
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private class changePasswordOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String mUID = user.getUid();

            String newPassword = mNewPassword.getText().toString();

            View focusView = null;

            //TODO: make a repeat password field and check if it is the same.

            if (TextUtils.isEmpty(newPassword)) {
                mNewPassword.setError(getString(R.string.error_field_required));
                focusView = mNewPassword;
                focusView.requestFocus();
            }
            else if(!isPasswordValid(newPassword)) {
                mNewPassword.setError(getString(R.string.error_incorrect_password));
                focusView = mNewPassword;
                focusView.requestFocus();
            }
            else {
                // block of code from firebase guide on user management
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");

                                    // return to settings
                                    Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                                    ChangePasswordActivity.this.startActivity(intent);
                                }
                            }
                        });
            }

        }
    }
}
