package docsapp.com.chatbot.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import docsapp.com.chatbot.DBHandler.MessageDbHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendPendingMessagesService extends IntentService {

    private static final String ACTION_UPLOAD = "com.docsapp.UPLOAD";

    public SendPendingMessagesService() {
        super("SendPendingMessagesService");
    }

    private static MessageDbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new MessageDbHelper(this);
    }

    public static void startActionUpload() {
            dbHelper.triggerPendingApisAndShowResponse();
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

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
