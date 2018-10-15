package docsapp.com.chatbot;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import docsapp.com.chatbot.DBHandler.MessageDbHelper;
import docsapp.com.chatbot.receiver.ConnectivityReceiver;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MessageJobService extends JobService implements
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MessageJobService.class.getSimpleName();
    boolean isWorking = false;
    boolean jobCancelled = false;
    MessageDbHelper dbHelper ;

    private ConnectivityReceiver mConnectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        mConnectivityReceiver = new ConnectivityReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        isWorking = true;

        dbHelper= MessageDbHelper.getInstance(this);
        //when conditions met , task of fetching from db and broadcast to activity is send

        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        unregisterReceiver(mConnectivityReceiver);
        return false;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}
