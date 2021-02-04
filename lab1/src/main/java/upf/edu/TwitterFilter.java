package upf.edu;

import upf.edu.filter.FileLanguageFilter;
import upf.edu.uploader.S3Uploader;

import java.util.Arrays;
import java.util.List;

public class TwitterFilter {
    public static void main( String[] args ) throws Exception {
    	long startTime = System.nanoTime();
        List<String> argsList = Arrays.asList(args);
        String language = argsList.get(0);
        String outputFile = argsList.get(1);
        String bucket = argsList.get(2);
        System.out.println("\n\n* Input info:\n");
        System.out.println("\t[*] Language: " + language + "\n\t[*] Output file: " + outputFile + "\n\t[*] Destination bucket: " + bucket);
        System.out.println("\n* Processing Files:\n");
        for(String inputFile: argsList.subList(3, argsList.size())) {
            System.out.println("\t[*] Processing: " + inputFile);
            final FileLanguageFilter filter = new FileLanguageFilter(inputFile, outputFile);
            filter.filterLanguage(language);
        }
		long endProcessingTime = System.nanoTime();
		
        final S3Uploader uploader = new S3Uploader(bucket, "prefix", "default");
        uploader.upload(Arrays.asList(outputFile));
        
		long endTime   = System.nanoTime();
		
        double processingTime = (double) (endProcessingTime - startTime) / 1_000_000_000;
        double uploadingTime = (double) (endTime - endProcessingTime) / 1_000_000_000;
        double totalTime = (double) (endTime - startTime) / 1_000_000_000;
		
        // 1 second = 1_000_000_000 nano seconds
        System.out.println("\n* Benchmark:");
        System.out.println("\t[*] Processing Time: " + processingTime + " seconds");
        System.out.println("\t[*] Uploading Time: " + uploadingTime + " seconds");
        System.out.println("\t[*] Execution Time: " + totalTime + " seconds");
    }
}
