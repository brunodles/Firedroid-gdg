package rocks.gdgmaceio.firedroid.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import rocks.gdgmaceio.firedroid.adapter.ChatListAdapter;
import rocks.gdgmaceio.firedroid.helper.FirebaseHelper;
import rocks.gdgmaceio.firedroid.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatActivityFragment extends Fragment {

    private static final String TAG = "ChatActivityFragment";

    private EditText message;
    private ListView messages;
    private Button send;

    private ChatListAdapter adapter;

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
        messages = (ListView) view.findViewById(R.id.messages);
        send = (Button) view.findViewById(R.id.send);

        send.setOnClickListener(onSendClickListener);
        message.setOnEditorActionListener(onEditorActionListener);

        final Firebase messagesRef = FirebaseHelper.get().child("messages");
        adapter = new ChatListAdapter(messagesRef.limitToLast(50));
        messages.setAdapter(adapter);
        adapter.registerDataSetObserver(dataSetObserver);
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            Log.d(TAG, "onEditorAction actionId " + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener onSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendMessage();
        }
    };

    private void sendMessage() {
        Firebase firebase = FirebaseHelper.get();
        Firebase messageRef = firebase.child("messages").push();
        messageRef.child("text").setValue(message.getText().toString());
        messageRef.child("author").setValue(firebase.getAuth().getUid());

        message.setText("");
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            messages.post(new Runnable() {
                @Override
                public void run() {
                    messages.smoothScrollToPosition(adapter.getCount() - 1);
                }
            });
        }
    };
}
