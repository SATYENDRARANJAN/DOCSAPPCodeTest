package docsapp.com.chatbot.manager;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import docsapp.com.chatbot.Constants;
import docsapp.com.chatbot.DTO.MessageDTO;
import docsapp.com.chatbot.R;
import docsapp.com.chatbot.network.APICallback;
import docsapp.com.chatbot.network.APIUtil;
import retrofit2.Call;

public class CommunicationManager {

    public static void getMessageResponse(Context context, String message , String  apiKey , int chatBotID , String externalID ,
                                          APICallback<MessageDTO> callback){
        Map <String , String >  queryMap = new HashMap<>();
        if(queryMap!=null){
            //Values in actual to be fetched from the UserManager instance .Hardcoded for testing purposes
            queryMap.put(Constants.KEY_API_KEY,Constants.VALUE_API_KEY);
            queryMap.put(Constants.KEY_CHATBOTID,Constants.VALUE_CHATBOTID);
            queryMap.put(Constants.KEY_MESSAGE ,message);
            queryMap.put(Constants.KEY_EXT_ID,Constants.VALUE_EXT_ID);
        }
        String url = context.getString(R.string.getMessageApi);
        Call<MessageDTO> callObj = APIUtil.getsInstance().getApiInterface().getUserDetails(url, queryMap);
        callObj.enqueue(callback);
    }

}
