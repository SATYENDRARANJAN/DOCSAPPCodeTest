package docsapp.com.chatbot.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import docsapp.com.chatbot.Constants;
import docsapp.com.chatbot.DTO.MessageDTO;
import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.R;

public class MessageListAdapter extends RecyclerView.Adapter{

    public Context mContext;
    public List<MessageData> mMessageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT_OFFLINE = 3;

    public MessageListAdapter(Context context, List<MessageData> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view ;
        if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_msg_received,viewGroup,false);
            ReceivedMessageHolder holder= new ReceivedMessageHolder(view);
            return holder;
        }else if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_msg_sent,viewGroup,false);
            SentMessageHolder holder = new SentMessageHolder(view);
            return  holder;
        }/*else if(viewType == VIEW_TYPE_MESSAGE_SENT_OFFLINE){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_msg_sent,viewGroup,false);
            SentMessageHolder holder = new SentMessageHolder(view);
            return  holder;
        }*/
        return null;
    }

    @Override
    public int getItemViewType(int position) {
      MessageData message = (MessageData) mMessageList.get(position);

      if (message.isSent()/*UserManager.getCurrentUser()*/){
          Log.e("Satya","2");
            /*if(!message.isUploaded())
                return VIEW_TYPE_MESSAGE_SENT_OFFLINE;*/
          return VIEW_TYPE_MESSAGE_SENT;
      }else{
          return VIEW_TYPE_MESSAGE_RECEIVED;
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MessageData message = mMessageList.get(position);
        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_RECEIVED:

                ReceivedMessageHolder  rmh = (ReceivedMessageHolder) viewHolder;
                rmh.nameText.setText(message.getChatBotName());
                rmh.messageText.setText( message.getMessage()+ message.getEmotion());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(message.getCreatedAt());
                rmh.timeText.setText(sdf.format(date));
                break;
            case VIEW_TYPE_MESSAGE_SENT:

                SentMessageHolder  smh = (SentMessageHolder)viewHolder;
                smh.messageText.setText( message.getMessage());
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = new Date(message.getCreatedAt());
                smh.timeText.setText(sdf.format(date));
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText,timeText, nameText;


        public ReceivedMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_receiver_msg);
            timeText = itemView.findViewById(R.id.tv_received_time);
            nameText = itemView.findViewById(R.id.tv_receiver_name);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText,timeText;
        public SentMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_sent_msg);
            timeText = itemView.findViewById(R.id.tv_received_time);
        }
    }
}
