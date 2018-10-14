package docsapp.com.chatbot.network;

import android.content.Context;

import docsapp.com.chatbot.DTO.MessageDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract  class APICallback<T> implements Callback<T> {

    private Context mContext ;
    private APICallback<T> mCallBack;

    public APICallback( Context context){
        this.mContext = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if(response.isSuccessful()){
            MessageDTO messageDTO = (MessageDTO) response.body();
            if(messageDTO!=null && messageDTO.success==1){
                onResponseSuccess(messageDTO);
            }else{
                onResponseError(messageDTO);
            }
        }else{
            onResponseFailure(response.message());
        }
    }

    public abstract  void onResponseSuccess(MessageDTO messageDTO);
    public abstract  void onResponseError(MessageDTO messageDTO);
    public abstract  void onResponseFailure(String message);

    @Override
    public void onFailure(Call call, Throwable t) {
        onResponseFailure("OOPS ! There is a problem !");
    }
}
