import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WorkerThread implements Runnable {

    private String code;

    public WorkerThread(String code) {
        this.code = code;
    }

    @Override
    public void run() {
        String directoryName = "";
        String game = main.getHTML(main.sgfBase + code);
//        if (code.contains("1517405975"))
//        System.out.println(code + " -> " + game);
        if (game.length() < 300) {
            return;
        }
        if (!game.contains("SZ[19]")) { // exclude non-19x19 games
            return;
        } else {
            game = game.substring(44, game.length() - 2); //assumes the format doesnt change
            directoryName = game.substring(game.indexOf("  WR") + 5, game.indexOf(" ", game.indexOf("  WR") + 2) - 1);
            new File(directoryName).mkdir();
        }

        File file = new File(directoryName + "/" + code + ".sgf");
        if (file.exists())
            return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(game);
            bw.close();
            main.numberGames++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}