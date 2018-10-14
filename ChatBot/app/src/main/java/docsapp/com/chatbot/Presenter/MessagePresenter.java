package docsapp.com.chatbot.Presenter;

import android.content.Context;
import android.text.TextUtils;

import docsapp.com.chatbot.Constants;
import docsapp.com.chatbot.DTO.MessageDTO;
import docsapp.com.chatbot.View.MessageView;
import docsapp.com.chatbot.activities.MainActivity;
import docsapp.com.chatbot.manager.CommunicationManager;
import docsapp.com.chatbot.network.APICallback;

public class MessagePresenter {


    public MessageView messageView ;

    public MessagePresenter(MessageView view ) {
        this.messageView =view;
    }

    public void getMessageResponse(Context context, String sentMessage){
        CommunicationManager.getMessageResponse(context, sentMessage, Constants.VALUE_API_KEY, Integer.parseInt(Constants.VALUE_CHATBOTID),
                Constants.VALUE_EXT_ID, new APICallback<MessageDTO>(context) {
                    @Override
                    public void onResponseSuccess(MessageDTO messageDTO) {
                        if(messageDTO==null || messageDTO.message==null){
                            return;
                        }
                        MessageView view= messageView;
                        if(view==null)
                            return;
                        view.showMessageView(messageDTO);
                    }

                    @Override
                    public void onResponseError(MessageDTO messageDTO) {
                        MessageView view = messageView;
                        if(view == null)
                            return;
                        if(TextUtils.isEmpty(messageDTO.errorMessage )){
                            onResponseFailure(messageDTO.errorMessage);
                        }
                    }

                    @Override
                    public void onResponseFailure(String message) {
                        MessageView view = messageView ;
                        if(view==null)
                            return;
                        view.onResponseFailure(message);
                    }
                });

    }
}
