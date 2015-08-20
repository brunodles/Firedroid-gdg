package rocks.gdgmaceio.firedroid;

import com.firebase.client.Firebase;

/**
 * Created by bruno on 19/08/15.
 */
public class FirebaseHelper {

    public static Firebase get() {
        return new Firebase("https://firedroid.firebaseio.com/");
    }
}
