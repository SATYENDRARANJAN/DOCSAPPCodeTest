package docsapp.com.chatbot.View;

import docsapp.com.chatbot.DTO.MessageDTO;

public interface MessageView {
    public void showMessageView(MessageDTO messageDTO);
    public void onResponseFailure(String message);
}
