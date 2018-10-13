package docsapp.com.chatbot.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import docsapp.com.chatbot.R;
import docsapp.com.chatbot.adapter.MessageListAdapter;

public class MainActivity extends Activity {


    private MessageListAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_messages);
        adapter = new MessageListAdapter(this, messageList);


    }
}
