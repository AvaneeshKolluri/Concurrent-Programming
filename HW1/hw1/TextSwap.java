//Avaneesh Kolluri and Akhilesh Reddy
//I pledge my honor that I have abided by the Stevens Honor System.


import java.io.*;
import java.util.*;

public class TextSwap {

    private static String readFile(String filename) throws Exception {
        String line;
        StringBuilder buffer = new StringBuilder();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        br.close();
        return buffer.toString();
    }

    private static Interval[] getIntervals(int numChunks, int chunkSize) {
        // TODO: Implement me!
    	//assuming the end is inclusive
    	Interval[] chunk_interval = new Interval[numChunks];
    	int start = 0;
    			
    	for(int i = 0; i < numChunks; i++) {
    		int end = start + chunkSize - 1;
    		chunk_interval[i] = new Interval(start,end);
    		start = end + 1;
    	}
    	
        return chunk_interval;
    }

    private static List<Character> getLabels(int numChunks) {
        Scanner scanner = new Scanner(System.in);
        List<Character> labels = new ArrayList<Character>();
        int endChar = numChunks == 0 ? 'a' : 'a' + numChunks - 1;
        System.out.printf("Input %d character(s) (\'%c\' - \'%c\') for the pattern.\n", numChunks, 'a', endChar);
        for (int i = 0; i < numChunks; i++) {
            labels.add(scanner.next().charAt(0));
        }
        scanner.close();
        // System.out.println(labels);
        return labels;
    }

    private static char[] runSwapper(String content, int chunkSize, int numChunks) {
        List<Character> labels = getLabels(numChunks);
        Interval[] intervals = getIntervals(numChunks, chunkSize);
        // TODO: Order the intervals properly, then run the Swapper instances.
        
        char[] buff = new char[chunkSize * numChunks];
        for (int i = 0; i < labels.size(); i++) {
        	Swapper swap_t = new Swapper(intervals[labels.get(i) -'a'],content,buff, chunkSize *i);
        	Thread t = new Thread(swap_t);
        	t.start(); // The result is that two threads are running concurrently: the current thread 
        				//(which returns from the call to the start method) 
        				//and the other thread (which executes its run method). 
        	
        }
        
        return buff;
    }

    private static void writeToFile(String contents, int chunkSize, int numChunks) throws Exception {
        char[] buff = runSwapper(contents, chunkSize, contents.length() / chunkSize);
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print(buff);
        writer.close();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TextSwap <chunk size> <filename>");
            return;
        }
        String contents = "";
        int chunkSize = Integer.parseInt(args[0]);
        try {
            contents = readFile(args[1]);
            if((contents.length() % chunkSize != 0)){
                System.out.println("File size must be a multiple of the chunk size");
                return;
            }

            writeToFile(contents, chunkSize, contents.length() / chunkSize);
        } catch (Exception e) {
            System.out.println("Error with IO.");
            return;
        }
        
        //do we have to add if the number of chunks is more than 26, then halt with error message??
        // also do we have to check if file size must be a multiple of chunk size
        // negative chunk size should not be valid
    }
}