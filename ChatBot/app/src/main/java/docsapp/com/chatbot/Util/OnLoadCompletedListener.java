package docsapp.com.chatbot.Util;

import java.util.List;

import docsapp.com.chatbot.DTO.MessageData;

public interface OnLoadCompletedListener {
    public void addToMessageList(List<MessageData> messageDataList);
}
