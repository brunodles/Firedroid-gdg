package rocks.gdgmaceio.firedroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginActivityFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void goToChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}
