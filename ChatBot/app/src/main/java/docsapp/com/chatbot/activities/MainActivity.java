package docsapp.com.chatbot.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import docsapp.com.chatbot.Constants;
import docsapp.com.chatbot.DTO.MessageDTO;
import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.Presenter.MessagePresenter;
import docsapp.com.chatbot.R;
import docsapp.com.chatbot.Util.Utils;
import docsapp.com.chatbot.View.MessageView;
import docsapp.com.chatbot.adapter.MessageListAdapter;

public class MainActivity extends Activity implements View.OnClickListener,View.OnTouchListener , MessageView {


    private MessageListAdapter adapter;
    private RecyclerView recyclerView;
    Button btn_send;
    EditText et_msg_sent ;
    MessagePresenter presenter;
    ArrayList<MessageData> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_messages);

        //Create a LinearLayout object
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        //set it on recyclerVeiw
        recyclerView.setLayoutManager(linearLayoutManager);
        //Create a new  Adapter
        messageList = new ArrayList<>();
        adapter = new MessageListAdapter(this,messageList );//this.initRecyclerViewItemDTOList());
        recyclerView.setAdapter(adapter);

        btn_send = findViewById(R.id.bt_send_msg);
        et_msg_sent = findViewById(R.id.et_send_msg);
        btn_send.setOnClickListener((View.OnClickListener) this);
        et_msg_sent.setOnTouchListener((View.OnTouchListener) this);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                recyclerView.scrollToPosition(messageList.size()-1);

            }
        });

        presenter = new MessagePresenter(this);

    }

    private List<MessageDTO> initRecyclerViewItemDtoList(int layout)
    {
        /*List<MessageDTO> ret = new ArrayList<MessageDTO>();

            ret.add(itemDto);

       */return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send_msg:
                sendMessageandShowResponse();
                break ;

        }
    }

    private void sendMessageandShowResponse() {
        String send_msg = et_msg_sent.getText().toString();
        if(!TextUtils.isEmpty(send_msg)) {
            MessageData sent_msg_data = new MessageData();
            sent_msg_data.setSenderName(Constants.VALUE_EXT_ID);
            sent_msg_data.setCreatedAt(Utils.getCurrentTimeStamp());
            sent_msg_data.setMessage(send_msg);
            sent_msg_data.setSent(true);
            //add the sent message to arrayList

            messageList.add(sent_msg_data);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            et_msg_sent.setText("");
            // printMessageList(messageList);
            Log.e("messageList :", "" + messageList.size());
            presenter.getMessageResponse(this, send_msg);
        }

    }

    private void printMessageList(List<MessageData> list) {
        int i=0;
        for(MessageData m : list){
            if(m.isSent())
               Log.d("messageList#" + i++,m.getMessage()+" "+m.getSenderName()+" "+m.getCreatedAt());
            else
                Log.d("messageList#" + i++,m.getMessage()+" "+m.getChatBotName() + " " +m.getCreatedAt());

        }
    }

    @Override
    public void showMessageView(MessageDTO messageDTO) {
        MessageData received_msg_data = messageDTO.message;
        received_msg_data.setSent(false);
        received_msg_data.setCreatedAt(Utils.getCurrentTimeStamp());
        messageList.add(received_msg_data);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        Log.e("messageList :",""+messageList.size());
        printMessageList(messageList);
    }

    @Override
    public void onResponseFailure(String message) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.et_send_msg:
            case R.id.ll_send_msg:
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
                break;
        }
        return false;
    }

}
