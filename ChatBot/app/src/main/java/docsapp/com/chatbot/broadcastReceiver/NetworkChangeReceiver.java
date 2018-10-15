package docsapp.com.chatbot.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import docsapp.com.chatbot.Service.SendPendingMessagesService;
import docsapp.com.chatbot.Util.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //A service would be started to find all the api request entries in db which are not uploaded and upload them and show their responses .

        String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (NetworkUtils.isNetworkConnected(context)) {
                Intent i = new Intent(context, SendPendingMessagesService.class);
                i.setAction("com.docsapp.UPLOAD");
                context.startService(i);
            }

        }
    }
}
