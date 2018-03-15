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
    EditText mRepeatPassword;
    Button mChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // initiate UI references
        mNewPassword = (EditText) findViewById(R.id.ET_password);
        mRepeatPassword = (EditText) findViewById(R.id.ET_repeat_password);
        mChangePassword = (Button) findViewById(R.id.BUTTON_change_password);


        mChangePassword.setOnClickListener(new changePasswordOnClick());
    }

    // method to check if password is long enough
    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    // method to check if password is equal to repeated password (no typos)
    private boolean isPasswordSame(String password, String repeat) {
        return password.equals(repeat);
    }

    private class changePasswordOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String newPassword = mNewPassword.getText().toString();
            String newRepeat = mRepeatPassword.getText().toString();

            View focusView = null;


            // check if passwords are given and the same
            if (TextUtils.isEmpty(newPassword)) {
                mNewPassword.setError(getString(R.string.error_field_required));
                focusView = mNewPassword;
                focusView.requestFocus();
            } else if(!isPasswordValid(newPassword)) {
                mNewPassword.setError(getString(R.string.error_incorrect_password));
                focusView = mNewPassword;
                focusView.requestFocus();
            } else if (TextUtils.isEmpty(newRepeat) || !isPasswordSame(newPassword, newRepeat)){
                mRepeatPassword.setError(getString(R.string.error_invalid_repeat));
                focusView = mRepeatPassword;
                focusView.requestFocus();
            } else {
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
