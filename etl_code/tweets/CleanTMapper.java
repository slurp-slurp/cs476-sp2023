/* 
===================================================================================================================
This cleaning code for original_tTweets does the following. It fulfills the cleaning requirements in both hw7 and hw8.
    1. drops unneeded columns
    2. drops badly formed rows
    3. text formatting - normalizes all tweets to lowercase
    4. new boolean column based on if tweet contains target text "anymore"
===================================================================================================================
*/
import java.io.IOException;
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CleanTMapper extends Mapper<Object, Text, Text, Text> {
    private final static IntWritable one = new IntWritable(1);
    private final static IntWritable zero = new IntWritable(0);
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parsed = line.split("\t");
        int len = parsed.length;
        if(len > 0){
            String userID = parsed[0];
            if(isNumeric(userID)){ //drops badly formed rows
                if(len == 4){
                    //String userID = parsed[0];
                    String tweetID = parsed[1];
                    String tweet = parsed[2];
                    String createdAt = parsed[3];
                    String res = userID + "\t" + tweetID + "\t" + tweet; //drops unneeded columns
    
                    String lowerRes = res.toLowerCase(); //normalization - all to lower case
    
                    Boolean hasAnymore = false;
                    if(tweet.contains("anymore")){ //binary column
                        hasAnymore = true;
                    }
    
                    String resWithBool = lowerRes + "\t" + hasAnymore;
    
                    context.write(new Text(resWithBool), new Text());
                }
            }
        }
    } 


    public static boolean isNumeric(String str){      
		try 
		{ 
			Integer.parseInt(str); 
            return true;
		}  
		catch (NumberFormatException e)  
		{ 
			return false;
		} 
    }
}

