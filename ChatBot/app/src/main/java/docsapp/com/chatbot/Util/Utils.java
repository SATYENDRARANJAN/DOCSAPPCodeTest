package docsapp.com.chatbot.Util;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import docsapp.com.chatbot.DBHandler.MessageDbHelper;
import docsapp.com.chatbot.DTO.MessageData;

public class Utils {

    public static long getCurrentTimeStamp(){
        Date date = new Date();
        return date.getTime();
    }

    public static void addMsgToDB(final MessageDbHelper dbHelper, final MessageData msg_data) {
        new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
            }
            protected Void doInBackground(Void... params) {
                // fetch data in background
               dbHelper.addMessage(msg_data);
                return null;
            }
            protected void onPostExecute() {
            }
        }.execute();
    }


    static List<MessageData> msgList;
    public static void getMsgFromDB(Context context,final MessageDbHelper dbHelper ) {
        final OnLoadCompletedListener listener = (OnLoadCompletedListener) context;
        new AsyncTask<Void, Void, List<MessageData>>() {
            protected void onProgressUpdate(){

            }

            protected void onPreExecute() {

            }
            protected List<MessageData> doInBackground(Void... params) {
                // fetch data in background
                msgList = dbHelper.getMessagesList();
                return msgList;
            }
            protected void onPostExecute(List<MessageData> msgList) {
                listener.addToMessageList(msgList);
            }
        }.execute();
    }




}
