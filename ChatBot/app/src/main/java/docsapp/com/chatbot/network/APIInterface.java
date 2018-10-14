package docsapp.com.chatbot.network;

import java.util.Map;

import docsapp.com.chatbot.DTO.MessageDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {

    @GET
    Call<MessageDTO> getUserDetails(@Url String url , @QueryMap(encoded = true) Map<String , String > queries);
}
