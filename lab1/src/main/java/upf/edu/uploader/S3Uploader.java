package upf.edu.uploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

public class S3Uploader implements Uploader {

	private String bucketName;
	private String prefix;
	private String name;

	private AmazonS3 s3client;
	/**
	 * Prefix:  
	 * @param bucketName
	 * @param prefix       	-	this parameter sets the string to be concatenated before the file name when 
	 * 							creating the key(file path) inside of the bucket. 
	 * @param name
	 */
	public S3Uploader(String bucketName, String prefix, String name) {
		this.bucketName = bucketName;
		this.prefix = prefix;
		this.name = name;
		this.s3client = createS3Client(name);
	}
	
	private AmazonS3 createS3Client(String name) {
		ProfileCredentialsProvider creds = new ProfileCredentialsProvider(name);
		return AmazonS3ClientBuilder.standard().withCredentials(creds).build();
	}
	
	private void createBucket(String bucketName) {
		try {
			getS3client().createBucket(bucketName);
		}
		catch (AmazonS3Exception e) {
			System.err.println(e.getErrorMessage());
		}
	}

	private List<String> listBuckets() {
        List<String> nameBuckets = new ArrayList<>();
		try {
			List<Bucket> buckets = getS3client().listBuckets();
	        System.out.println("\n* Amazon S3 buckets:\n");
	        for (Bucket b : buckets) {
	            System.out.println("\t[*] " + b.getName()); 
	            nameBuckets.add(b.getName());
	        }
		}
		catch (Exception e) {
			System.out.println("\n** " + e.getMessage()); 
			System.out.println("** Abort execution!!!"); 
			System.exit(1);
		}
        return nameBuckets;
	}

	private boolean bucketExists() {
		List<String> names = listBuckets();
		return names.contains(getBucketName()); 
	}
	
	private void checks() {
		System.out.println("\n** This bucket NO EXIST!!!"); 
		System.out.println("** You want create this bucket?\n\t[1] Yes\n\t[2] No"); 
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String res = in.readLine();
			if(res.equals("1")) {
				System.out.println("\t[*] Creating bucket '" + getBucketName() + "' ..."); 
				createBucket(getBucketName());
			} else {
				System.out.println("\t[*] Abort execution!!!"); 
				System.exit(1);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void upload(List<String> files) {
		if(!bucketExists()) 
			checks();
		for(String f : files) {
			try {
				String keyName = getPrefix() + f;
				System.out.println("\n* Uploading file ..."); 
				getS3client().putObject(getBucketName(), keyName, new File(f));
				System.out.println("* Upload done!"); 
		    } catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage()); 
				System.exit(1);
		    }
		}
	}

    public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AmazonS3 getS3client() {
		return s3client;
	}

	public void setS3client(AmazonS3 s3client) {
		this.s3client = s3client;
	}
}
