import config.Config;
import crawler.Crawl;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Author: stk
 * Date: 4/15/17
 * Time: 5:47 PM
 */
public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] prop = line.split("=");
                switch (prop[0].trim()) {
                    case "OUT_DIR":
                        Config.setOutDir(prop[1].trim());
                        break;
                    case "FINAL_NAME":
                        Config.setFinalName(prop[1].trim());
                        break;
                    case "MERGE_NAME":
                        Config.setMergeName(prop[1].trim());
                        break;
                }
            }
            System.out.println("Use custom config.");
        } catch (Exception e) {
            System.out.println("Use default config.");
        }
        new Crawl().getAll(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }
}
