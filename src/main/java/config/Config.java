package config;

/**
 * Author: stk
 * Date: 4/17/17
 * Time: 6:55 PM
 */
public class Config {
    private static String OUT_DIR = "/home/stk/Documents/result/";
    private static String FINAL_NAME = "final";
    private static String MERGE_NAME = "list";

    public static String getOutDir() {
        return OUT_DIR;
    }

    public static void setOutDir(String outDir) {
        OUT_DIR = outDir;
    }

    public static String getFinalName() {
        return FINAL_NAME;
    }

    public static void setFinalName(String finalName) {
        FINAL_NAME = finalName;
    }

    public static String getMergeName() {
        return MERGE_NAME;
    }

    public static void setMergeName(String mergeName) {
        MERGE_NAME = mergeName;
    }
}
