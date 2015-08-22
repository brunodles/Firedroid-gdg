package rocks.gdgmaceio.firedroid.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rocks.gdgmaceio.firedroid.R;
import rocks.gdgmaceio.firedroid.helper.FirebaseHelper;
import rocks.gdgmaceio.firedroid.preference.LoginPreference;


public class LoginActivityFragment extends Fragment {

    @Bind(R.id.email) EditText email;
    @Bind(R.id.password) EditText password;

    private Listener listener;
    private ProgressDialog progressDialog;

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
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.sign_in)
    public void onSignInClick() {
        showProgressDialog();
        Firebase firebase = FirebaseHelper.get();
        firebase.unauth();
        firebase.authWithPassword(emailAsString(), passwordAsString(), signInListener);
    }

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
            loginSuccess();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Can't auth user", Toast.LENGTH_SHORT).show();
            dissmissProgressDialog();
        }
    };

    @OnClick(R.id.reset_password)
    public void onResetPasswordClick() {
        FirebaseHelper.get().resetPassword(emailAsString(), resetPasswordListener);
        showProgressDialog();
    }

    private Firebase.ResultHandler resetPasswordListener = new Firebase.ResultHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Hey, look at your email!", Toast.LENGTH_SHORT).show();
            dissmissProgressDialog();
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Error segind password change email", Toast.LENGTH_SHORT).show();
            dissmissProgressDialog();
        }
    };

    @OnClick(R.id.sign_up)
    public void onSignUpClick() {
        FirebaseHelper.get().createUser(emailAsString(), passwordAsString(), registerListener);
        showProgressDialog();
    }

    private Firebase.ResultHandler registerListener = new Firebase.ResultHandler() {
        @Override
        public void onSuccess() {
            onSignUpClick();
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Toast.makeText(LoginActivityFragment.this.getActivity(), "Error creating new user", Toast.LENGTH_SHORT).show();
            dissmissProgressDialog();
        }
    };

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), "Login", "Please wait", true, false);
        progressDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void loginSuccess() {
        saveLoginData();
        dissmissProgressDialog();
        listener.goToChat();
    }

    private void saveLoginData() {
        LoginPreference preference = new LoginPreference(getActivity());
        preference.setEmail(emailAsString());
        preference.setPassword(passwordAsString());
        preference.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreLoginData();
    }

    private void restoreLoginData() {
        LoginPreference preferences = new LoginPreference(getActivity());
        email.setText(preferences.getEmail());
        password.setText(preferences.getPassword());
    }

    public static interface Listener {
        void goToChat();
    }
}
