/* 
===================================================================================================================
This cleaning code for original_user_location does the following. It fulfills the cleaning requirements in both hw7 and hw8.
    1. drops badly formed rows
    2. text formatting - removes state from all rows to normalize the location format
    - no extra columns to drop since there are only two
===================================================================================================================
*/
import java.io.IOException;
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CleanUMapper extends Mapper<Object, Text, Text, Text> {
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
                if(len == 2){
                    String res = line.split(",")[0]; //text formatting
                    context.write(new Text(res), new Text());
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

