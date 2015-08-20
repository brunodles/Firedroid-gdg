package rocks.gdgmaceio.firedroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class LoginActivityFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private Button resetPassword;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);

        signIn = (Button) view.findViewById(R.id.sign_in);
        signUp = (Button) view.findViewById(R.id.sign_up);
        resetPassword = (Button) view.findViewById(R.id.reset_password);

        signIn.setOnClickListener(onSignInClickListener);
        signUp.setOnClickListener(onSignUpClickListener);
        resetPassword.setOnClickListener(onResetPasswordClickListener);
    }

    private View.OnClickListener onSignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseHelper.get().authWithPassword(emailAsString(), passwordAsString(), signInListener);
        }
    };

    @NonNull
    private String passwordAsString() {
        return password.getText().toString();
    }

    @NonNull
    private String emailAsString() {
        return email.getText().toString();
    }

    private Firebase.AuthResultHandler signInListener = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {

        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Can't auth user", Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener onResetPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            
        }
    };
    private View.OnClickListener onSignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseHelper.get().createUser(emailAsString(), passwordAsString(), registerListener);
        }
    };
    private Firebase.ResultHandler registerListener = new Firebase.ResultHandler() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Error creating new user", Toast.LENGTH_SHORT).show();
        }
    };
}
