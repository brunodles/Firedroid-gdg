package rocks.gdgmaceio.firedroid.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bruno on 22/08/15.
 */
public class LoginPreference {

    private static final String PREF_PASSWORD = "password";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_NAME_AUTH = "auth.preferences";

    private final SharedPreferences preferences;

    private String email;
    private String password;

    public LoginPreference(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME_AUTH, Context.MODE_PRIVATE);
        loadData();
    }

    private void loadData() {
        email = preferences.getString(PREF_EMAIL, "");
        password = preferences.getString(PREF_PASSWORD, "");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void apply() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }
}
