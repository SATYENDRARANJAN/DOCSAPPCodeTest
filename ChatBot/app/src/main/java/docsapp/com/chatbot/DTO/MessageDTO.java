package docsapp.com.chatbot.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageDTO {

    @SerializedName("success")
    public int success ;

    @SerializedName("errorMessage")
    public String errorMessage;

    @SerializedName("message")
    public MessageData message;

    @SerializedName("data")
    public List<OtherData> data;

/*
    {
        "success":1, "errorMessage":"",
            "message":
        {
            "chatBotName":"Cyber Ty",
                "chatBotID":63906,
                "message" :
            "Maybe you should practice your shit talking with a baby bot before you step up to me...",
                    "emotion":"sad-9"
        },
        "data":[]}*/
}
