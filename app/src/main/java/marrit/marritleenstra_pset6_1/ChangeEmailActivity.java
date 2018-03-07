package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends AppCompatActivity {

    private String TAG = "CHANGEEMAILACTIVITY";

    // UI references
    EditText mNewEmail;
    Button mChangeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        // initiate UI references
        mNewEmail = (EditText) findViewById(R.id.ET_email);
        mChangeEmail = (Button) findViewById(R.id.BUTTON_change_email);

        mChangeEmail.setOnClickListener(new changeEmailOnClick());

    }

    private class changeEmailOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            // block of code from firebase guide on user management
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // TODO: check if new email is valid

            // TODO: let the user know that he has to login with the new email adress

            user.updateEmail(mNewEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");

                                // TODO: update email address also in database

                                // return to settings
                                Intent intent = new Intent(ChangeEmailActivity.this, SettingsActivity.class);
                                ChangeEmailActivity.this.startActivity(intent);
                            }

                        }
                    });
        }
    }

}
