import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool.*;


public class CountRecs {
    public static void main(String[] args) throws Exception { 
        if (args.length != 2) {
        System.err.println("Usage: CountRecs <input path> <output path>");
        System.exit(-1);
        }
        Job job = new Job(); 
        job.setJarByClass(CountRecs.class); 
        job.setJobName("Count Records");
        FileInputFormat.addInputPath(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(CountRecsMapper.class);
        job.setCombinerClass(CountRecsReducer.class);
        job.setReducerClass(CountRecsReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(1); // 1 Reduce task
        System.exit(job.waitForCompletion(true) ? 0 : 1); 
    }

}