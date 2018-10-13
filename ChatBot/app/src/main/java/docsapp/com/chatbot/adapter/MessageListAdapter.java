package docsapp.com.chatbot.adapter;

import android.content.Context;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import docsapp.com.chatbot.R;
import docsapp.com.chatbot.dao.Message;

public class MessageListAdapter extends RecyclerView.Adapter{

    public Context mContext;
    public List<Message> mMessageList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageListAdapter(Context context, List<Message> messageList) {
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
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
      Message message = (Message) mMessageList.get(position);

      if ((message.getSender().getId()) == 123 /*UserManager.getCurrentUser()*/){
          return VIEW_TYPE_MESSAGE_SENT;
      }else{
          return VIEW_TYPE_MESSAGE_RECEIVED;
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Message message = mMessageList.get(position);
        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ReceivedMessageHolder  rmh = (ReceivedMessageHolder) viewHolder;
                rmh.nameText.setText(message.getSenderName());
                rmh.messageText.setText( message.getMsg());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(message.getCreatedAt());
                rmh.timeText.setText(sdf.format(date));
                break;
            case VIEW_TYPE_MESSAGE_SENT:
                SentMessageHolder  smh = (SentMessageHolder)viewHolder;
                smh.messageText.setText( message.getMsg());
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
        ImageView profileImage;


        public ReceivedMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_receiver_msg);
            timeText = itemView.findViewById(R.id.tv_received_time);
            nameText = itemView.findViewById(R.id.tv_receiver_name);
            profileImage = itemView.findViewById(R.id.img_receiver);
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
