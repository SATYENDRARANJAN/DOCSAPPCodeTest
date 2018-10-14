package docsapp.com.chatbot.DTO;

import com.google.gson.annotations.SerializedName;

public class MessageData {

    @SerializedName("chatBotName")
    String chatBotName;

    @SerializedName("chatBotID")
    int chatBotID;

    @SerializedName("message")
    String message;

    @SerializedName("emotion")
    String emotion;

    //for sender
    @SerializedName("senderID")
    int senderID;

    @SerializedName("senderName")
    String senderName;

    //timestamp
    @SerializedName("createdAt")
    long createdAt;

    @SerializedName("sent")
    boolean isSent;

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }


    public String getChatBotName() {
        return chatBotName;
    }

    public void setChatBotName(String chatBotName) {
        this.chatBotName = chatBotName;
    }

    public int getChatBotID() {
        return chatBotID;
    }

    public void setChatBotID(int chatBotID) {
        this.chatBotID = chatBotID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

}
