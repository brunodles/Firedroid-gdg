package rocks.gdgmaceio.firedroid.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.LinkedList;
import java.util.List;

import rocks.gdgmaceio.firedroid.R;

/**
 * Created by bruno on 21/08/15.
 */
public class ChatListAdapter extends BaseAdapter implements ChildEventListener {

    private List<String> keys;
    private List<DataSnapshot> models;

    public ChatListAdapter(Query query) {
        keys = new LinkedList<>();
        models = new LinkedList<>();
        query.addChildEventListener(this);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();

        if (s == null) {
            keys.add(0, key);
            models.add(0, dataSnapshot);
        } else {
            int previousIndex = key.indexOf(s);
            int nextIndex = previousIndex + 1;
            if (nextIndex == models.size()) {
                keys.add(key);
                models.add(dataSnapshot);
            } else {
                keys.add(nextIndex, key);
                models.add(dataSnapshot);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        int index = keys.indexOf(key);
        models.set(index, dataSnapshot);
        notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        int index = keys.indexOf(key);
        keys.remove(index);
        models.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_chat, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.update(models.get(i));
        return view;
    }

    public static class ViewHolder {
        TextView message;

        public ViewHolder(View view) {
            message = (TextView) view.findViewById(R.id.message);
        }

        public void update(DataSnapshot dataSnapshot) {
            String text = (String) dataSnapshot.child("text").getValue();
            message.setText(text);
        }
    }
}
