package upf.edu.parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Optional;

public class SimplifiedTweet {

	private static Gson parser = new Gson();

	private final long tweetId;			  // the id of the tweet ('id')
	private final String text;  		      // the content of the tweet ('text')
	private final long userId;			  // the user id ('user->id')
	private final String userName;		  // the user name ('user'->'name')
	private final String language;          // the language of a tweet ('lang')
	private final long timestampMs;		  // seconds from epoch ('timestamp_ms')  

	public static Gson getParser() {
		return parser;
	}
	
	public long getTweetId() {
		return tweetId;
	}
	
	public String getText() {
		return text;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public long getTimestampMs() {
		return timestampMs;
	}

	public SimplifiedTweet(long tweetId, String text, long userId, String userName,
	                         String language, long timestampMs) {
		
		this.tweetId=tweetId;
		this.text=text;
		this.userId=userId;
		this.userName=userName;
		this.language=language;
		this.timestampMs=timestampMs;	
	}

	/**
	 * Returns a {@link SimplifiedTweet} from a JSON String.
	 * If parsing fails, for any reason, return an {@link Optional#empty()}
	 *
	 * @param jsonStr
	 * @return an {@link Optional} of a {@link SimplifiedTweet}
	 */
	public static Optional<SimplifiedTweet> fromJson(String jsonStr)  {
		if(!jsonStr.isEmpty()) {
			JsonElement je = JsonParser.parseString(jsonStr);
			JsonObject tweet = je.getAsJsonObject();

			long tweetId, userId, timestampMs;			  
			String text, userName, language;	 

			if (tweet.has("id") && tweet.has("text") && tweet.has("lang") && tweet.has("timestamp_ms")) {  
				tweetId = tweet.get("id").getAsLong();
				timestampMs = tweet.get("timestamp_ms").getAsLong();
				text = tweet.get("text").getAsString();
				language = tweet.get("lang").getAsString();
				if (tweet.has("user")) {   
					JsonObject user = tweet.getAsJsonObject("user");
					if (user.has("id") && user.has("name")) {  
						userId = user.get("id").getAsLong(); 
						userName = user.get("name").getAsString();
						SimplifiedTweet t = new SimplifiedTweet(tweetId, text, userId, userName, language, timestampMs);
						return Optional.of(t);
					}
				}
			}
		}
		return Optional.empty();	
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
