package docsapp.com.chatbot.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MessageData implements Parcelable {

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

    @SerializedName("isUploaded")
    boolean isUploaded;

    @SerializedName("sent")
    boolean isSent;

    public MessageData(Parcel in) {
        chatBotName = in.readString();
        chatBotID = in.readInt();
        message = in.readString();
        emotion = in.readString();
        senderID = in.readInt();
        senderName = in.readString();
        createdAt = in.readLong();
        isUploaded = in.readByte() != 0;
        isSent = in.readByte() != 0;
    }

    public static final Creator<MessageData> CREATOR = new Creator<MessageData>() {
        @Override
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        @Override
        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };

    public MessageData() {

    }

    public boolean isSent() {
        return isSent;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatBotName);
        dest.writeInt(chatBotID);
        dest.writeString(message);
        dest.writeString(emotion);
        dest.writeInt(senderID);
        dest.writeString(senderName);
        dest.writeLong(createdAt);
        dest.writeByte((byte) (isUploaded ? 1 : 0));
        dest.writeByte((byte) (isSent ? 1 : 0));
    }
}
