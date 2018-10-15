package docsapp.com.chatbot.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.MessageConstants;
import docsapp.com.chatbot.MessageJobService;

public class MessageDbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "MessageDB";

    // Message table name
    public static final String TABLE_MESSAGE = "MessageTable";
    //User Table
    public static final String TABLE_USER = "UserTable";
    private final Context mContext;

    private SQLiteDatabase mSQLdb;

    static MessageDbHelper sInstance ;
    public static MessageDbHelper getInstance(Context context) {
        if(sInstance ==null){
            sInstance = new MessageDbHelper(context);
        }
        return sInstance;
    }

    private MessageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        this.mSQLdb = getWritableDatabase();
    }

    private MessageDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context mContext) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + MessageConstants.KEY_MESSAGE_ID + " LONG PRIMARY KEY,"
                + MessageConstants.KEY_CHATBOT_NAME + " TEXT,"
                + MessageConstants.KEY_CHATBOT_ID + " INTEGER,"
                + MessageConstants.KEY_MESSAGE + " TEXT,"
                + MessageConstants.KEY_EMOTICON + " TEXT,"
                + MessageConstants.KEY_SENDER_ID + " INTEGER,"
                + MessageConstants.KEY_SENDER_NAME + " TEXT,"
                + MessageConstants.KEY_CREATED_AT + " LONG,"
                + MessageConstants.KEY_IS_UPLOADED + " INTEGER,"
                + MessageConstants.KEY_IS_SENT + " INTEGER"
                + ")";
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        this.onCreate(db);
    }


    public void addMessage(MessageData messageData) {
        mSQLdb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageConstants.KEY_CHATBOT_NAME, messageData.getChatBotName());
        values.put(MessageConstants.KEY_CHATBOT_ID, ""+messageData.getChatBotID());
        values.put(MessageConstants.KEY_MESSAGE, messageData.getMessage());
        values.put(MessageConstants.KEY_EMOTICON, messageData.getEmotion());
        values.put(MessageConstants.KEY_IS_SENT, messageData.isSent());
        values.put(MessageConstants.KEY_SENDER_ID, ""+messageData.getSenderID());
        values.put(MessageConstants.KEY_SENDER_NAME, messageData.getSenderName());
        values.put(MessageConstants.KEY_CREATED_AT,messageData.getCreatedAt());
        values.put(MessageConstants.KEY_IS_UPLOADED,messageData.isUploaded());
        mSQLdb.insert(TABLE_MESSAGE, null, values);
        mSQLdb.close();
    }

    public List<MessageData> getMessagesList(){
        mSQLdb = getReadableDatabase();
        Cursor cursor = mSQLdb.query(TABLE_MESSAGE, new String[]{
                        MessageConstants.KEY_MESSAGE_ID,
                        MessageConstants.KEY_CHATBOT_NAME,
                        MessageConstants.KEY_CHATBOT_ID,
                        MessageConstants.KEY_MESSAGE,
                        MessageConstants.KEY_EMOTICON,
                        MessageConstants.KEY_SENDER_ID,
                        MessageConstants.KEY_SENDER_NAME,
                        MessageConstants.KEY_IS_SENT,
                        MessageConstants.KEY_CREATED_AT,
                        MessageConstants.KEY_IS_UPLOADED
                        },
                null, null, null, null, MessageConstants.KEY_CREATED_AT + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<MessageData> messageDataList = new ArrayList<MessageData>();

        if (cursor.moveToFirst()) {
            do {
                MessageData messageData = new MessageData();
                if(cursor.getInt(7)==1){//sent message
                    messageData.setSent(true);
                    messageData.setSenderName(cursor.getString(6));
                }else if (cursor.getInt(7)==0){//received message
                    messageData.setSent(false);
                    messageData.setChatBotName(cursor.getString(1));
                    messageData.setChatBotID(cursor.getInt(2));
                }
                messageData.setMessage(cursor.getString(3));
                messageData.setEmotion(cursor.getString(4));
                messageData.setCreatedAt(cursor.getLong(8));

                messageDataList.add(messageData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mSQLdb.close();
        printMessageTable();
        return messageDataList;
    }

    public List<MessageData> getMessageDataListNotUploaded() {
        mSQLdb = getReadableDatabase();
        Cursor cursor = mSQLdb.query(TABLE_MESSAGE, new String[]{
                        MessageConstants.KEY_MESSAGE_ID,
                        MessageConstants.KEY_CHATBOT_NAME,
                        MessageConstants.KEY_CHATBOT_ID,
                        MessageConstants.KEY_MESSAGE,
                        MessageConstants.KEY_EMOTICON,
                        MessageConstants.KEY_SENDER_ID,
                        MessageConstants.KEY_SENDER_NAME,
                        MessageConstants.KEY_IS_SENT,
                        MessageConstants.KEY_CREATED_AT,
                        MessageConstants.KEY_IS_UPLOADED
                },

                MessageConstants.KEY_IS_UPLOADED + "=?", new String[]{String.valueOf(0)}, null, null, MessageConstants.KEY_CREATED_AT + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<MessageData> messageDataList = new ArrayList<MessageData>();
        MessageData messageData ;
        if (cursor.moveToFirst()) {
            do {
                messageData = new MessageData();
                if(cursor.getInt(7)==1 && cursor.getInt(9)==0){//sent message
                    messageData.setSent(true);
                    messageData.setSenderName(cursor.getString(6));
                    messageData.setMessage(cursor.getString(3));
                    messageData.setEmotion(cursor.getString(4));
                    messageData.setCreatedAt(cursor.getLong(8));

                    messageDataList.add(messageData);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        mSQLdb.close();

        return messageDataList;
    }

    public void updateIsUploadedStatusOfOfflineMessage(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageConstants.KEY_IS_UPLOADED, 1);
        db.update(TABLE_MESSAGE, contentValues, MessageConstants.KEY_IS_UPLOADED+ "=? AND " +
                        MessageConstants.KEY_IS_SENT +"=?"
                        , new String[]{String.valueOf(0),String.valueOf(1)});
        db.close();

    }

    public void  printMessageTable(){
        mSQLdb = getReadableDatabase();
        Cursor cursor = mSQLdb.query(TABLE_MESSAGE, new String[]{
                        MessageConstants.KEY_MESSAGE_ID,
                        MessageConstants.KEY_CHATBOT_NAME,
                        MessageConstants.KEY_CHATBOT_ID,
                        MessageConstants.KEY_MESSAGE,
                        MessageConstants.KEY_EMOTICON,
                        MessageConstants.KEY_SENDER_ID,
                        MessageConstants.KEY_SENDER_NAME,
                        MessageConstants.KEY_IS_SENT,
                        MessageConstants.KEY_CREATED_AT,
                        MessageConstants.KEY_IS_UPLOADED
                },
                null, null, null, null, MessageConstants.KEY_CREATED_AT + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int i=0;
        if (cursor.moveToFirst()) {
            do {
               Log.d("Row#"+i++,cursor.getLong(0)+ "|" +
                cursor.getString(1)+ " | " +
                cursor.getInt(2)+ " | " +
                cursor.getString(3)+ " | " +
                cursor.getString(4)+ " | " +
                cursor.getInt(5)+ " | " +
                cursor.getString(6)+ " | " +
                cursor.getInt(7)+ " | " +
                cursor.getLong(8)+ " | " +
                cursor.getInt(9));

            } while (cursor.moveToNext());
        }
        cursor.close();
        mSQLdb.close();

    }

}
