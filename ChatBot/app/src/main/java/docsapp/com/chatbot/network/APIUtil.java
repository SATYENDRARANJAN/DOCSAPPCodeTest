package docsapp.com.chatbot.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtil {
    private static APIUtil sInstance = null;
    private static Retrofit mRetrofit;

    private APIUtil (){
         mRetrofit = new Retrofit.Builder()
                .baseUrl("https://www.personalityforge.com/api/chat/")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static APIUtil getsInstance() {
        if(sInstance == null){
            sInstance = new APIUtil();
        }
        return sInstance;
    }

    public APIInterface  getApiInterface(){
        return  mRetrofit.create(APIInterface.class);
    }
}
