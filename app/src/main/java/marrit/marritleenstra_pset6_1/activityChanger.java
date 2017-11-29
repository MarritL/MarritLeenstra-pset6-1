package marrit.marritleenstra_pset6_1;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Marrit on 28-11-2017.
 */

public class activityChanger implements View.OnClickListener {

    Button mSignInButton;
    Button mRegisterButton;
    TextView mGoToRegisterView;


    @Override
    public void onClick(View view) {

        SignInActivity actSignIn = (SignInActivity) view.getContext();
        RegisterActivity actRegister = (RegisterActivity) view.getContext();
        MainActivity actMain = (MainActivity) view.getContext();

        mSignInButton = actSignIn.mEmailSignInButton;
        mRegisterButton = actRegister.mEmailRegisterButton;
        mGoToRegisterView = actSignIn.mGoToRegisterView;

        Intent intent;

        if (view == mSignInButton) {
            actSignIn.attemptLogin();
            intent = new Intent(actSignIn, MainActivity.class);
        }
        else if (view == mRegisterButton) {
            intent = new Intent(actRegister, MainActivity.class);
        }
        else if (view == mGoToRegisterView) {
            intent = new Intent(actSignIn, RegisterActivity.class);
        }
        else {
            intent = new Intent();
        }

        view.getContext().startActivity(intent);

    }
}
