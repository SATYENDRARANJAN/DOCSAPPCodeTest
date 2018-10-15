package docsapp.com.chatbot.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import docsapp.com.chatbot.Constants;
import docsapp.com.chatbot.DBHandler.MessageDbHelper;
import docsapp.com.chatbot.DTO.MessageDTO;
import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.MessageJobService;
import docsapp.com.chatbot.Presenter.MessagePresenter;
import docsapp.com.chatbot.R;
import docsapp.com.chatbot.service.SendPendingMessagesService;
import docsapp.com.chatbot.Util.NetworkUtils;
import docsapp.com.chatbot.Util.OnLoadCompletedListener;
import docsapp.com.chatbot.Util.Utils;
import docsapp.com.chatbot.View.MessageView;
import docsapp.com.chatbot.adapter.MessageListAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener,
        MessageView, OnLoadCompletedListener {

    private static final String ACTION_UPLOAD = "com.docsapp.UPLOAD";
    private MessageListAdapter adapter;
    private RecyclerView recyclerView;
    Button btn_send;
    EditText et_msg_sent;
    MessagePresenter presenter;
    ArrayList<MessageData> messageList = new ArrayList<>();
    MessageDbHelper dbHelper;
    BroadcastReceiver br;
    BroadcastReceiver networkBR;
    List<MessageData> msgListNotUploaded = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_messages);
        dbHelper = MessageDbHelper.getInstance(this);

        //Create a LinearLayout object
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);

        //set it on recyclerVeiw
        recyclerView.setLayoutManager(linearLayoutManager);
        //Create a new  Adapter
        messageList = new ArrayList<>();
        adapter = new MessageListAdapter(this, messageList);//this.initRecyclerViewItemDTOList());
        recyclerView.setAdapter(adapter);

        btn_send = findViewById(R.id.bt_send_msg);
        et_msg_sent = findViewById(R.id.et_send_msg);
        btn_send.setOnClickListener((View.OnClickListener) this);
        et_msg_sent.setOnTouchListener((View.OnTouchListener) this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initMessageList();
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(messageList.size() - 1);

            }
        });

        presenter = new MessagePresenter(this);


        //registering local br . triggers when app is in foreground .
        br= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("BR1","Inside BR");
                msgListNotUploaded = intent.getParcelableArrayListExtra("msg_list");
                sendMessagesToChatBot(context,msgListNotUploaded);
                Toast.makeText(context, "Offline chats updated. ", Toast.LENGTH_LONG).show();
            }
        };

        //registering network br
        networkBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    if (NetworkUtils.isNetworkConnected(context)) {
                        Log.e("BR1","net connected");
                        Intent i = new Intent(context, SendPendingMessagesService.class);
                        i.setAction("com.docsapp.UPLOAD");
                        context.startService(i);
                        Toast.makeText(context, "Conn connected", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context, "Conn disconnected", Toast.LENGTH_LONG).show();

                    }
                }
            }
        };

        
        //JobScehculer for nougat and oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter(ACTION_UPLOAD));
        //this.registerReceiver(br,new IntentFilter(ACTION_UPLOAD));
        this.registerReceiver(networkBR, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, MessageJobService.class))
                .setMinimumLatency(3000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

    private void sendMessagesToChatBot(Context context,List<MessageData> msgListNotUploaded) {
        if(msgListNotUploaded!=null)
        for(MessageData m : msgListNotUploaded) {
            Log.d("BR1","Inside BR2");
            presenter.getMessageResponse(context, m.getMessage());
        }
        dbHelper.updateIsUploadedStatusOfOfflineMessage();

    }

    private void initMessageList() {
        Utils.getMsgFromDB(this, dbHelper);
    }


    private void sendMessageandShowResponse() {
        String send_msg = et_msg_sent.getText().toString();
        if (!TextUtils.isEmpty(send_msg.trim())) {
            MessageData sent_msg_data = new MessageData();
            sent_msg_data.setSenderName(Constants.VALUE_EXT_ID);
            sent_msg_data.setCreatedAt(Utils.getCurrentTimeStamp());
            sent_msg_data.setMessage(send_msg);
            sent_msg_data.setSent(true);

            if(NetworkUtils.isNetworkConnected(this))
                sent_msg_data.setUploaded(true);
            else
                sent_msg_data.setUploaded(false);

            //add the sent message to arrayList
            messageList.add(sent_msg_data);
            adapter.notifyDataSetChanged();

            //add to database
            Utils.addMsgToDB(dbHelper, sent_msg_data);

            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            et_msg_sent.setText("");
            // printMessageList(messageList);
            Log.e("messageList :", "" + messageList.size());
            presenter.getMessageResponse(this, send_msg);
        }

    }

    private void printMessageList(List<MessageData> list) {
        int i = 0;
        for (MessageData m : list) {
            if (m.isSent())
                Log.d("messageList#" + i++, m.getMessage() + " " + m.getSenderName() + " " + m.getCreatedAt());
            else
                Log.d("messageList#" + i++, m.getMessage() + " " + m.getChatBotName() + " " + m.getCreatedAt());

        }
    }

    @Override
    public void showMessageView(MessageDTO messageDTO) {
        MessageData received_msg_data = messageDTO.message;
        received_msg_data.setSent(false);
        received_msg_data.setCreatedAt(Utils.getCurrentTimeStamp());
        //add To Database
        Utils.addMsgToDB(dbHelper, received_msg_data);
        //add to adapter
        messageList.add(received_msg_data);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        Log.e("messageList :", "" + messageList.size());
        printMessageList(messageList);
    }

    @Override
    public void onResponseFailure(String message) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.et_send_msg:
            case R.id.ll_send_msg:
                if (adapter.getItemCount() > 0) {
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_msg:
                sendMessageandShowResponse();
                break;
        }
    }

    @Override
    public void addToMessageList(List<MessageData> messageDataList) {
        messageList.addAll(messageDataList);
        //progress.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size() - 1);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(br != null) {
            //unregisterReceiver(br);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        }
        if(networkBR!=null){
            unregisterReceiver(networkBR);
        }
    }
}
