package marrit.marritleenstra_pset6_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    // UI references
    TextView mChangePassword;
    TextView mChangeEmail;
    TextView mChangeDisplayname;
    TextView mUnsubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //TODO: how can you on up navigation go to the right tab? (manifest now mainActivity as parent)

        //initiate UI references
        mChangeDisplayname = (TextView) findViewById(R.id.change_displayname);
        mChangeEmail = (TextView) findViewById(R.id.change_email);
        mChangePassword = (TextView) findViewById(R.id.change_password);
        mUnsubscribe = (TextView) findViewById(R.id.unsubscribe);

        //set listeners on UI references
        mChangeEmail.setOnClickListener(new goToNextActivity());
        mChangePassword.setOnClickListener(new goToNextActivity());

        mChangeDisplayname.setOnClickListener(new changeDisplayName());



    }

    public class goToNextActivity implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent newIntent;

            if (view == mChangeEmail) {
                newIntent = new Intent(SettingsActivity.this, ChangeEmailActivity.class);
                SettingsActivity.this.startActivity(newIntent);
            }
            if (view == mChangePassword) {
                newIntent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                SettingsActivity.this.startActivity(newIntent);
            }

        }
    }

    public class changeDisplayName implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showDisplayNameDialog();
        }
    }

    // show a dialog which allows user to change the display name
    private void showDisplayNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_display_name,null);
        dialogBuilder.setView(dialogView);

        final EditText mDisplayName = (EditText) dialogView.findViewById(R.id.ET_displayname);

        // Let the user know what the dialog is for
        dialogBuilder.setMessage("Change Display name");

        // OK-button
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                String mNewDisplayName = mDisplayName.getText().toString();

                // check if user gave To-Do title
                if (!mNewDisplayName.equals("")) {

                    // get database reference
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    // get current user info
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String mUID = user.getUid();

                    // update email address in database as well
                    mDatabase.child("users").child(mUID).child("displayName").setValue(mNewDisplayName);

                    // empty edit-text
                    mDisplayName.getText().clear();

                } else {
                    // TODO: if user gave no title, yell at him with focus etc
                    Toast.makeText(SettingsActivity.this, "Give a display name!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // cancel button
        dialogBuilder.setNegativeButton("Cancel", new CancelListener());

        // when the building is done show the dialog in the app screen
        AlertDialog changeDisplayName = dialogBuilder.create();
        changeDisplayName.show();
    }



    // cancel listener for dialog box
    private class CancelListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int whichButton) {
            // do nothing and go back
        }
    }

    // TODO: add back functionality
}
