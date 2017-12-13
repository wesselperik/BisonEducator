package com.wesselperik.educator;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wesselperik.educator.model.Goal;
import com.wesselperik.educator.model.Result;
import com.wesselperik.educator.view.adapters.GoalListAdapter;
import com.wesselperik.educator.view.adapters.ResultListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ListView resultListView;
    private ProgressBar resultProgressBar;
    private ResultListAdapter resultListAdapter;
    private ArrayList<Result> resultList;
    private ListView goalListView;
    private ProgressBar goalProgressBar;
    private GoalListAdapter goalListAdapter;
    private ArrayList<Goal> goalList;
    private long wait = 1000;
    private boolean studiesLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        class EducatorJavaScriptInterface {

            public EducatorJavaScriptInterface() { }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void getHTML(String html) {
                loadData(html);
            }
        }

        goalProgressBar = (ProgressBar) findViewById(R.id.progressBarGoals);
        goalListView = (ListView) findViewById(R.id.listViewGoals);
        goalList = new ArrayList<>();

        goalListAdapter = new GoalListAdapter(this.getApplicationContext(), R.layout.goal_item, goalList);
        goalListView.setAdapter(goalListAdapter);

        resultProgressBar = (ProgressBar) findViewById(R.id.progressBarResults);
        resultListView = (ListView) findViewById(R.id.listViewResults);
        resultList = new ArrayList<>();

        resultListAdapter = new ResultListAdapter(this.getApplicationContext(), R.layout.result_item, resultList);
        resultListView.setAdapter(resultListAdapter);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setSaveFormData(false);
        webView.addJavascriptInterface(new EducatorJavaScriptInterface(), "INTERFACE");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                if (url.contains("login")){
                    view.loadUrl(getJavascriptString(getResources().getString(R.string.username), getResources().getString(R.string.password)));
                } else if (url.contains("studyprogress")){
                    getHTML();
                } else if (url.contains("enrollment") || url.contains("catalog")){
                    // logged in, but redirected to a wrong page. Redirect to study progress overview page
                    webView.loadUrl("https://bison.saxion.nl/studyprogress");
                }
            }
        });

        webView.loadUrl("https://bison.saxion.nl/login");


    }

    private String getJavascriptString(String username, String password) {
        return "javascript:document.getElementById('username').value='" +
                username +
                "';document.getElementById('password').value='" +
                password +
                "';document.forms[0].submit();";
    }

    private void getHTML() {
        try {
            Log.d("getHTML", "getting HTML, waiting " + (wait / 1000) + " seconds");
            Thread.sleep(wait); // TODO: create a different solution for waiting on the AJAX content.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:window.INTERFACE.getHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            });
            wait += 1000;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadData(String html) {
        Log.d("DEBUG", "Getting grades");
        Document doc = Jsoup.parse(html);

        if (!studiesLoaded) {
            //Element study = doc.getElementsByClass("study").first();
            //Log.d("STUDY", study.text());

            Elements goals = doc.getElementsByClass("goal-unit-card");
            for (Element goal : goals) {
                String goalTitle = goal.getElementsByClass("page-title").first().text();
                int goalProgress = Integer.parseInt(goal.getElementsByClass("progress-bar").first().getElementsByTag("span").first().text().replaceAll("[\\D]", ""));
                String goalAchieved = goal.getElementsByClass("achieved-workload-label").first().getElementsByTag("span").first().text();

                Log.d("GOAL", goalTitle + " | progress: " + goalProgress + " | achieved: " + goalAchieved);

                Goal goalItem = new Goal(goalTitle, goalProgress, goalAchieved);
                goalList.add(goalItem);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goalListAdapter.notifyDataSetChanged();
                    goalProgressBar.setVisibility(View.GONE);
                }
            });
            studiesLoaded = true;
        }

        Element resultContent = doc.getElementsByClass("list-group list-group--grid list-group--results").first();

        if (resultContent == null) {
            getHTML();
            return;
        }

        Elements results = resultContent.getElementsByClass("list-group-item");
        for (Element result : results) {
            String examName = result.getElementsByClass("exam-unit__name").first().text();
            String grade = result.getElementsByClass("grade").first().text();
            Result resultItem = null;
            if (!grade.equals("-")) {
                String[] gradeSplit = grade.split(" ");
                resultItem = new Result(examName, Integer.parseInt(gradeSplit[0]), Integer.parseInt(gradeSplit[1]));
                Log.d("GRADE", examName + ": " + gradeSplit[0]);
            } else {
                resultItem = new Result(examName, 0, 0);
                Log.d("GRADE", examName + ": no result yet");
            }

            resultList.add(resultItem);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultListAdapter.notifyDataSetChanged();
                resultProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
