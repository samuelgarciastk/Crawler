package parse;

import config.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: stk
 * Date: 4/14/17
 * Time: 9:51 PM
 */
public class Duplication {
    public static void duplication() {
        String inputFile = Config.getOutDir() + Config.getMergeName();
        String outputFile = Config.getOutDir() + Config.getFinalName();
        Set<String> titles = new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            FileWriter fw = new FileWriter(outputFile);
            String line;
            int duplication = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() > 7 && line.substring(0, 8).equals("[Title]:")) {
                    String title = line.substring(8);
                    if (titles.contains(title)) {
                        duplication++;
                        System.out.println(title);
                        while (true) {
                            String tmp = br.readLine();
                            if (tmp.length() > 39 && tmp.substring(0, 40).equals("----------------------------------------"))
                                break;
                        }
                        continue;
                    }
                    titles.add(title);
                }
                fw.write(line);
                fw.write("\n");
            }
            fw.flush();
            fw.close();
            br.close();
            System.out.println("Duplication: " + duplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Duplication.duplication();
    }
}
