package docsapp.com.chatbot.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import docsapp.com.chatbot.DBHandler.MessageDbHelper;
import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.Presenter.MessagePresenter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendPendingMessagesService extends IntentService  {

    MessagePresenter presenter;

    private static final String ACTION_UPLOAD = "com.docsapp.UPLOAD";

    public SendPendingMessagesService() {
        super("SendPendingMessagesService");
    }

    private static MessageDbHelper dbHelper;
    static List<MessageData> list;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = MessageDbHelper.getInstance(this);
    }

    public  void startActionUpload() {
         list =dbHelper.getMessageDataListNotUploaded();
         sendMessageToActivity();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {
                startActionUpload();
            }
        }
    }

    //SERVICE ACTIVITY COMMUNICATION
    private void sendMessageToActivity() {
        Intent intent = new Intent(ACTION_UPLOAD);
        intent.putParcelableArrayListExtra("msg_list",  (ArrayList<? extends Parcelable>)list);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
