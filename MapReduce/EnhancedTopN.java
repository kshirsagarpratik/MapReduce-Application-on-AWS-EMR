import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import java.util.*;
import java.io.IOException;

public class EnhancedTopN {
    
    public static class MiscUtils {
        
        public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
            List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());
            
            Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
                
                @Override
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            
            //LinkedHashMap will keep the keys in the order they are inserted
            //which is currently sorted on natural ordering
            Map<K, V> sortedMap = new LinkedHashMap<K, V>();
            
            for (Map.Entry<K, V> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            
            return sortedMap;
        }
        
        
    }
    
    
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: Test Coverage <in> <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf);
        job.setJobName("Test Coverage");
        job.setJarByClass(EnhancedTopN.class);
        job.setMapperClass(TopNMapper.class);
        job.setReducerClass(TopNReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    
    public static class TopNMapper extends Mapper<Object, Text, Text, IntWritable> {
        
        private Map<String, Integer> countMap = new HashMap<>();
        
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String cleanLine = value.toString().toLowerCase().replaceAll("[_|$#<>\\^=\\[\\]\\*/\\\\,;,.\\-:()?!\"']", " ");
            StringTokenizer itr = new StringTokenizer(cleanLine);
            while (itr.hasMoreTokens()) {
                
                String word = itr.nextToken().trim();
                if (countMap.containsKey(word)) {
                    countMap.put(word, countMap.get(word)+1);
                }
                else {
                    countMap.put(word, 1);
                }
            }
        }
        
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            
            for (String key: countMap.keySet()) {
                context.write(new Text(key), new IntWritable(countMap.get(key)));
            }
        }
    }
    
    
    public static class TopNReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        
        private Map<Text, IntWritable> countMap = new HashMap<>();
        
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            
            // computes the number of occurrences of a single word
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            
            // puts the number of occurrences of this word into the map.
            // We need to create another Text object because the Text instance
            // we receive is the same for all the words
            countMap.put(new Text(key), new IntWritable(sum));
        }
        
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            
            Map<Text, IntWritable> sortedMap = MiscUtils.sortByValues(countMap);
            
            int counter = 0;
            for (Text key: sortedMap.keySet()) {
                if (counter ++ == 100) {
                    break;
                }
                context.write(key, sortedMap.get(key));
            }
        }
    }
    
    
}
