package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        //initiate UI references
        mChangeDisplayname = (TextView) findViewById(R.id.change_displayname);
        mChangeEmail = (TextView) findViewById(R.id.change_email);
        mChangePassword = (TextView) findViewById(R.id.change_password);
        mUnsubscribe = (TextView) findViewById(R.id.unsubscribe);

        //set listeners on UI references

    }

    /*public class goToNextActivity implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent newIntent;

            if (view == mChangeDisplayname) {

            }

        }
    }*/
}
