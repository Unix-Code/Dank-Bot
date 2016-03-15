
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.util.ArrayList;

public class DankBot {

    public static void main(String... args) throws TwitterException {
        int tweetIndex = 0;
        while (true) {
            boolean unableToTweet = false;

            ArrayList<Status> allTweets = new ArrayList<>();

            Twitter twitter = TwitterFactory.getSingleton();

            //create a new search
            Query query = new Query("dark");
            query.setLang("en");
            query.setCount(50);

            //get the results from that search
            if (tweetIndex > 50 /*# of tweets per 1 query*/) {
                allTweets.clear();
            }
            
            if (allTweets.size() == 0) {
                QueryResult result = twitter.search(query);

                for(Status tweet : result.getTweets()) {
                    allTweets.add(tweet);
                }
            }
            
            //get the first tweet from those results

            for (Status tweet : allTweets) {
                if (tweet.getText().toLowerCase().contains("dark") && tweet.getURLEntities().length == 0 && tweet.getMediaEntities().length == 0) {
                    break;
                }
                tweetIndex ++;
            }
            System.out.println("TweetIndex: " + tweetIndex + " of allTweets Size: " + allTweets.size()); // Diagnostic
            
            if (tweetIndex >= allTweets.size()) continue;
            
            String tweetText = allTweets.get(tweetIndex).getText();

            if (tweetText.toLowerCase().contains("dark")) {
                tweetText = tweetText.substring(0, tweetText.toLowerCase().indexOf("dark")) + "dank" + tweetText.substring(tweetText.toLowerCase().indexOf("dark") + 4);

                if (tweetText.length() < 140) {
                    StatusUpdate statusUpdate = new StatusUpdate(/*"@" + tweetResult.getUser().getScreenName() + " " + */tweetText);
                    try {
                        Status status = twitter.updateStatus(statusUpdate);
                        System.out.println(tweetText);
                        System.out.println("Done.");
                    } catch (TwitterException e) {
                        System.out.println("Could not tweet, for some reason, it is above 140 characters...");
                        e.printStackTrace();
                        unableToTweet = true;
                    }                    
                }
                else {
                    unableToTweet = true;
                }
            }
            else {
                System.out.println("For some reason, this tweet did not query correctly. Dark was not found.");
                unableToTweet = true;
            }

            if (!unableToTweet) {
                try {
                    Thread.sleep(1000 * 60 * 10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
/*
 * public static void main(String[] args) {
Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
public void run() {
System.out.println("Program Closed");
}

}, "Shutdown-thread"));
 */