package com.xapple.zxslotmachine.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Egorka on 19.10.2015.
 */
public class UpdateCheck {

    public static String version = "1.03";

    public static Boolean needUpdate() {
        Boolean res = false;
        try {
            String webPage = "http://zertex.ru/minecraft/updates/check.php?mod=slotmachine";
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            if (!result.equalsIgnoreCase(version) && result.length() <= 6) {
                res = true;
            }
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return res;
    }

}
