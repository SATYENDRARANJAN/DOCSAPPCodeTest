package docsapp.com.chatbot.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import docsapp.com.chatbot.DTO.MessageData;
import docsapp.com.chatbot.MessageConstants;

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

    public MessageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        this.mSQLdb = getWritableDatabase();
    }
    public MessageDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context mContext) {
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

        return messageDataList;

    }

    public void triggerPendingApisAndShowResponse() {
    }
}
