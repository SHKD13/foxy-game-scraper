import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {
    public static String getHTML(String urlToRead) {
//        for (int attempts = 0; attempts < 20; attempts++) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                // give up
            }
            return result.toString();
        } catch (Exception e) {
            // give up
        }
//        }

        System.out.println("Could not return any HTML: " + urlToRead);
        return "";
    }

    public static final String sgfBase = "http://happyapp.huanle.qq.com/cgi-bin/CommonMobileCGI/TXWQFetchChess?chessid=";
    public static final String base = "http://happyapp.huanle.qq.com/cgi-bin/CommonMobileCGI/TXWQFetchChessList?type=3&lastCode=";

    public static long numberGames = 0;

    public static void main(String[] ar) throws Exception {

        String code = "";
        ExecutorService executor = Executors.newFixedThreadPool(800);

//        if (ar.length < 1) {
//            System.out.println("supply a code to search backwards from");
//            System.exit(-1);
//        }

        if (ar.length != 0) {
        code = ar[0];
        }
//        System.out.println(getHTML(sgfBase+code));
//        if (code != null)
//            System.exit(0);

//        long timestamp = Long.parseLong(ar[0].substring(0, 10));
        // a 0
//        String[] type = {"1000"};
//        String[] _019 = {"1"};
        // then 3 digits. not random, they depend on _019, but for now let's assume random.
//        String[] digits = { "6", "7", "8", "9"};
//        1517405975010001679
        while (true) {
//            for (String s : type)
//                for (String o19 : _019)
//                    for (String d0 : digits)
//                        for (String d1 : digits)
//                            for (String d2 : digits) {
//                System.out.println(timestamp + "0" + s + o19 + d0 + d1 + d2);
//                                Runnable worker = new WorkerThread(timestamp + "0" + s + o19 + d0 + d1 + d2);
//                                executor.execute(worker);
//                            }
//            timestamp--; // idk what happens when the timestamp loses a digit...
//            System.out.println(numberGames);
//            break;
            try {
                String result = getHTML(base + code);
                if (result.length() < 500)
                    continue;
                String lastId = "";
                int index = 0;
                while ((index = result.indexOf("chessid", index)) != -1) {
                    lastId = result.substring(index + 10, result.indexOf(',', index) - 1);

                    Runnable worker = new WorkerThread(lastId);
                    executor.execute(worker);

                    index += 10;
                }
                code = lastId;

//                if (numberGames%1000==0)
                System.out.println(numberGames + ", " + code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
