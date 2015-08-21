package rocks.gdgmaceio.firedroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatActivityFragment extends Fragment {

    private EditText message;
    private TextView messages;
    private Button send;

    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message = (EditText) view.findViewById(R.id.message);
        messages = (TextView) view.findViewById(R.id.messages);
        send = (Button) view.findViewById(R.id.send);

        send.setOnClickListener(onSendClickListener);
    }

    private View.OnClickListener onSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Firebase firebase = FirebaseHelper.get();
            Firebase messageRef = firebase.child("messages").push();
            messageRef.child("text").setValue(message.getText().toString());
            messageRef.child("author").setValue(firebase.getAuth().getUid());
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        FirebaseHelper.get().child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                StringBuilder builder = new StringBuilder();
                for (DataSnapshot child : children) {
                    builder.append(child.child("text").getValue());
                    builder.append("\n");
                }
                messages.setText(builder.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
