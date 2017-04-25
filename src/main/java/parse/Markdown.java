package parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Author: stk
 * Date: 4/18/17
 * Time: 7:00 PM
 */
public class Markdown {
    public static void main(String[] args) {
        Markdown.txt2markdown();
    }

    public static void txt2markdown() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/stk/Documents/Result_split/devops"));
            FileWriter fw = new FileWriter("/home/stk/Documents/Result_split/devops_space");
            String line;
            while ((line = br.readLine()) != null) {
                fw.write(line);
                fw.write("\n");
                fw.write("\n");
            }
            fw.flush();
            fw.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
