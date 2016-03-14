
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DankBot {

    //if something goes wrong, we might see a TwitterException
    public static void main(String... args) throws TwitterException {
        while (true) {
            boolean unableToTweet = false;
            //access the twitter API using your twitter4j.properties file
            /// Twitter twitter = TwitterFactory.getSingleton();
            //send a tweet
            // Status status = twitter.updateStatus("Hello, World!");
            //print a message so we know when it finishes
            // System.out.println("Done.");
            Twitter twitter = TwitterFactory.getSingleton();

            //create a new search
            Query query = new Query("\"dark\"");
            query.lang("en");
            query = query.count(50);
            // System.out.println(query.getCount() + query.getQuery()); // Diagnostic
            //get the results from that search
            QueryResult result = twitter.search(query);

            //get the first tweet from those results
            Status tweetResult = result.getTweets().get(0);
            for (int i = 0; i < 50; i++) {
                Status tempTweetResult = result.getTweets().get(i);

                if (tempTweetResult.getText().toLowerCase().contains("dark") && tempTweetResult.getURLEntities().length == 0 && tempTweetResult.getMediaEntities().length == 0) {
                    tweetResult = tempTweetResult;
                    break;
                }
                
            }

            String tweetText = tweetResult.getText();

            if (tweetText.toLowerCase().contains("dark")) {
                tweetText = tweetText.substring(0, tweetText.toLowerCase().indexOf("dark")) + "dank" + tweetText.substring(tweetText.toLowerCase().indexOf("dark") + 4);

                //reply to that tweet
                if (tweetText.length() < 140) {
                    StatusUpdate statusUpdate = new StatusUpdate(/*"@" + tweetResult.getUser().getScreenName() + " " + */tweetText);
                    // statusUpdate.inReplyToStatusId(tweetResult.getId());
                    try {
                        Status status = twitter.updateStatus(statusUpdate);
                    } catch (TwitterException e) {
                        System.out.println("Could not tweet, for some reason, it is above 140 characters...");
                        unableToTweet = true;
                    }
                    System.out.println(tweetText);
                }
                else {
                    unableToTweet = true;
                }
            }
            else {
                System.out.println("For some reason, this tweet did not query correctly. Dank was not found.");
                unableToTweet = true;
            }

            System.out.println("Done.");

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
