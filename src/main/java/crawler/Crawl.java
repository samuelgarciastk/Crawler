package crawler;

import config.Config;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: stk
 * Date: 4/15/17
 * Time: 3:11 PM
 */
public class Crawl {
    private static final String HOST = "http://stackoverflow.com";
    private static Logger logger = Logger.getLogger(Crawl.class);

    public void getAll(String key, int begin, int end) {
        for (int i = begin; i <= end; i++) {
            getQuestion(key, i);
        }
    }

    private void getQuestion(String key, int page) {
        String url = HOST + "/search?tab=newest&pagesize=50&q=" + key + "&page=" + page;
        String html = HttpUtils.sendGet(url);
        Document document = Jsoup.parse(html);
        Elements links = document.select(".question-summary > .summary > .result-link > span > a[href]");
        StringBuilder sb = new StringBuilder();
        for (Element link : links) {
            String subUrl = HOST + link.attr("href");
            sb.append(getPost(HttpUtils.sendGet(subUrl), subUrl));
        }
        try {
            FileWriter fw = new FileWriter(Config.getOutDir() + "page" + page);
            fw.write(sb.toString());
            fw.flush();
            fw.close();
            System.out.println("Finish page " + page);
        } catch (IOException e) {
            logger.error("File writer error.", e);
            e.printStackTrace();
        }
    }

    private void getQuestionMultiThread(String key, int page) {
        String url = HOST + "/search?tab=newest&pagesize=50&q=" + key + "&page=" + page;
        String html = HttpUtils.sendGet(url);
        Document document = Jsoup.parse(html);
        Elements links = document.select(".question-summary > .summary > .result-link > span > a[href]");
        List<String> urls = new ArrayList<>();
        for (Element link : links) urls.add(HOST + link.attr("href"));
        String[] results = HttpUtils.threadGet(urls);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String result : results) sb.append(getPost(result, urls.get(i++)));
        try {
            FileWriter fw = new FileWriter(Config.getOutDir() + "page" + page);
            fw.write(sb.toString());
            fw.flush();
            fw.close();
            System.out.println("Finish page " + page);
        } catch (IOException e) {
            logger.error("File writer error.", e);
            e.printStackTrace();
        }
    }

    private String getPost(String html, String url) {
        System.out.println("Parsing " + url);
        if (html == null) {
            return "Cannot access!\n----------------------------------------\n";
        }
        StringBuilder sb = new StringBuilder();
        Document document = Jsoup.parse(html);
        Element title = document.select("#question-header > h1 > .question-hyperlink").first();
        sb.append("[Title]:").append(title.text()).append("\n").append("[URL]:").append(url).append("\n");
        Element question = document.select("#question .postcell > div > .post-text").first();
        sb.append("[Question]:").append(question.html()).append("\n");
        Element questionVote = document.select("#question .votecell > .vote > .vote-count-post").first();
        sb.append("[Question Vote]:").append(questionVote.text()).append("\n");
        Elements tags = document.select(".post-taglist > a");
        sb.append("[Question Tags]:");
        for (Element tag : tags) {
            sb.append("#").append(tag.text()).append(" ");
        }
        sb.append("\n");
        Elements questionComments = document.select("#question .comment-copy");
        if (questionComments != null) {
            int i = 1;
            for (Element questionComment : questionComments) {
                sb.append("    [Question Comment ").append(i++).append("]:").append(questionComment.html()).append("\n");
            }
        }
        Element time = document.select(".postcell .user-info > .user-action-time .relativetime").first();
        sb.append("[Post time]:").append(time.text()).append("\n");
        Elements answers = document.select(".answer");
        int i = 1;
        for (Element answer : answers) {
            Element answerText = answer.select(".answercell > .post-text").first();
            sb.append("[Answer ").append(i).append("]:").append(answerText.html()).append("\n");
            Element answerVote = answer.select(".votecell > .vote > .vote-count-post").first();
            sb.append("[Answer ").append(i).append(" Vote]:").append(answerVote.text()).append("\n");
            Elements comments = answers.select(".comment-copy");
            if (comments == null) break;
            int j = 1;
            for (Element comment : comments) {
                sb.append("    [Comment ").append(i).append("-").append(j++).append("]:").append(comment.html()).append("\n");
            }
            i++;
        }
        sb.append("----------------------------------------\n");
        return sb.toString();
    }
}
