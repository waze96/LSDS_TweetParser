package upf.edu.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import upf.edu.parser.SimplifiedTweet;

public class FileLanguageFilter implements LanguageFilter {

	private String inputFile;
	private String outputFile;
	
	public FileLanguageFilter(String inputFile, String outputFile) {

		this.inputFile=inputFile;
		this.outputFile=outputFile;	
	}
	
	@Override
	public void filterLanguage(String language) throws Exception {
		// Buffer that stores the content of the inputFile
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(getInputFile()));
			// Each line from the inputFile; While line is not null, we try to parse to SimplifiedTweet
			String line = reader.readLine();
			while (line != null) {
				// Return Optional.empty or Optional<SimplifiedTweet>
				Optional<SimplifiedTweet> tweet = SimplifiedTweet.fromJson(line);
				// If not Optional.empty
				if(tweet.isPresent()) {			  
					SimplifiedTweet t = tweet.get();
					// If tweet language is 'es', write the file in outputFile
					if(t.getLanguage().equals(language))
						writeTweet(t);
				}
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeTweet(SimplifiedTweet tweet) throws IOException{
		File file = new File(getOutputFile());
		FileWriter fr = new FileWriter(file, true);
		fr.write(tweet.toString());
		BufferedWriter br = new BufferedWriter(fr);
		PrintWriter pr = new PrintWriter(br);
		pr.println(tweet.toString());
		pr.close();
		br.close();
		fr.close();
	}
	
	// GETTERS
	public String getInputFile() {
		return inputFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	//SETTERS
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

}
