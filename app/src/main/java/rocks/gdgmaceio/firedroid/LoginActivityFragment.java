package rocks.gdgmaceio.firedroid;

import android.app.Activity;
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

    private Listener listener;

    public LoginActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener)
            listener = (Listener) activity;
        else
            throw new IllegalArgumentException("Must implement the LoginActivityFragment.Listener");
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
            lock();

            Firebase firebase = FirebaseHelper.get();
            firebase.unauth();
            firebase.authWithPassword(emailAsString(), passwordAsString(), signInListener);
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
            goToChat();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Can't auth user", Toast.LENGTH_SHORT).show();
            unLock();
        }
    };
    private View.OnClickListener onResetPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseHelper.get().resetPassword(emailAsString(), resetPasswordListener);
            lock();
        }
    };
    private Firebase.ResultHandler resetPasswordListener = new Firebase.ResultHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Hey, look at your email!", Toast.LENGTH_SHORT).show();
            unLock();
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Error segind password change email", Toast.LENGTH_SHORT).show();
            unLock();
        }
    };
    private View.OnClickListener onSignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseHelper.get().createUser(emailAsString(), passwordAsString(), registerListener);
            lock();
        }
    };
    private Firebase.ResultHandler registerListener = new Firebase.ResultHandler() {
        @Override
        public void onSuccess() {
            onSignInClickListener.onClick(signIn);
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Error creating new user", Toast.LENGTH_SHORT).show();
            unLock();
        }
    };
    
    private void lock(){
        changeViewsEnabled(false);
    }

    private void unLock(){
        changeViewsEnabled(true);
    }

    private void changeViewsEnabled(boolean isEnabled) {
        email.setEnabled(isEnabled);
        password.setEnabled(isEnabled);
        signIn.setEnabled(isEnabled);
        signUp.setEnabled(isEnabled);
        resetPassword.setEnabled(isEnabled);
    }

    public void goToChat() {
        listener.goToChat();
    }

    public static interface Listener {
        void goToChat();
    }
}
