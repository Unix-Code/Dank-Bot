
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.util.ArrayList;

public class DankBot {

    private final Twitter twitter;
    private ArrayList<Status> tweets;
    private int tweetIndex;

    public DankBot() {
        tweetIndex = 0;
        tweets = new ArrayList<>();
        twitter = TwitterFactory.getSingleton();
    }

    public static void main(String... args) throws TwitterException {
        DankBot dankBot = new DankBot();
        dankBot.sendTweet();
        if (dankBot.isTweetCountMilestone()) dankBot.sendMilestoneTweet();
    }

    public void sendTweet() {
        boolean tweetSent = false;

        while (!tweetSent) {
            if (this.tweetIndex >= this.tweets.size()) {
                this.tweets = this.query();
                this.tweetIndex = 0;
            }
            String tweetText = this.formTweet(this.tweetIndex);
            StatusUpdate statusUpdate = new StatusUpdate(tweetText);

            try {
                Status status = twitter.updateStatus(statusUpdate);
                System.out.println(tweetText + "\nSent.");
                tweetSent = true;

            } catch (TwitterException e) {
                System.out.println("Could not tweet, for some reason.");
            }
            this.tweetIndex++;
        }
    }

    private ArrayList<Status> query() {
        ArrayList<Status> someTweets = new ArrayList<>();

        Query query = new Query(" dark ");
        query.setLang("en");
        query.setCount(50);

        QueryResult result = null;
        if (someTweets.isEmpty()) {
            try {
                result = twitter.search(query);
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }

            for (Status tweet : result.getTweets()) {
                if (tweet.getText().toLowerCase().contains("dark") 
                        && tweet.getURLEntities().length == 0 
                        && tweet.getMediaEntities().length == 0 
                        && tweet.getUserMentionEntities().length == 0
                        && tweet.getURLEntities().length == 0) {
                    someTweets.add(tweet);
                }

                // System.out.println(tweet.getText() + "\n");
            }
        }

        return someTweets;
    }

    private String formTweet(int tweetIndex) {
        Status tweet = this.tweets.get(tweetIndex);
        String tweetText = tweet.getText();

        tweetText = tweetText.substring(0, tweetText.toLowerCase().indexOf("dark")) + "dank" + tweetText.substring(tweetText.toLowerCase().indexOf("dark") + 4);

        return tweetText;
    }
    
    public boolean isTweetCountMilestone() {
        return getStatusCount()%1000 == 0;
    }
    
    private int getStatusCount() {
        int statusCount = -1;
        try {
            statusCount = twitter.verifyCredentials().getStatusesCount();
        } catch (TwitterException e) {
            System.out.println("Could not get Tweet Count.");
        }
        return statusCount;
    }
    
    public void sendMilestoneTweet() {
        try {
            Status tweet = twitter.updateStatus("Beep Boop Bop! I reached my " + getStatusCount() + " tweet. Beep! Thank you for sticking around for this milestone.");
            
        } catch(TwitterException e) {
            System.out.println("Could not send milestone tweet for some reason.");
        }
    } 
}
