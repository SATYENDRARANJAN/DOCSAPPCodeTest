package docsapp.com.chatbot.Util;

import java.util.Date;

public class Utils {

    public static long getCurrentTimeStamp(){
        Date date = new Date();
        return date.getTime();
    }
}
