package rocks.gdgmaceio.firedroid.helper;

import com.firebase.client.Firebase;

import rocks.gdgmaceio.firedroid.model.Message;

/**
 * Created by bruno on 19/08/15.
 */
public class FirebaseHelper {

    public static Firebase get() {
        return new Firebase("https://firedroid.firebaseio.com/");
    }

    public static Firebase messages() {
        return get().child("messages");
    }

    public static void sendMessage(String text) {
        Firebase firebase = FirebaseHelper.get();
        String uid = firebase.getAuth().getUid();
        FirebaseHelper.messages()
                .push()
                .setValue(new Message(text, uid));
    }
}
