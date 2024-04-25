package com.batch.demo.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;


@RestController
@RequestMapping("/batch")
public class BatchController {
	@Autowired
	private Job processJob;

	@Autowired
	private Job personProcessJob;

	@Autowired
	private Job processJobRegion;

	@Autowired
	JobLauncher jobLauncher;

	private static final String UPLOAD_DIR = "D:\\cut-files"; // Specify your desired folder path here

	@PostMapping("/upload")
	public String uploadFileAi(@RequestParam("file") MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println(file.getOriginalFilename());

		try {
			// Create the upload directory if it doesn't exist
			Path uploadPath = Path.of(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Copy the file to the upload directory
			Path filePath = uploadPath.resolve(fileName);
			System.out.println(filePath);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			JobParameters jp = new JobParametersBuilder().addString("jobName", processJobRegion.getName())
					.addString("fileName", file.getOriginalFilename()).addLong("time", System.currentTimeMillis())
					.toJobParameters();

			// JobExecution jobExecution = jobLauncher.run(personProcessJob,jp );
			try {
				JobExecution jobExecution = jobLauncher.run(personProcessJob, jp);

			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "File uploaded successfully!";
		} catch (IOException ex) {
			return "Error uploading file: " + ex.getMessage();
		}
	}

//	@RequestMapping(value = "/callApi")
//	public void callEngine(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
//			@RequestParam(value = "employeeId") Integer employeeId) throws Exception{
//	JobParameters jobParameters = new JobParametersBuilder()
//			.addDate("startDate", DateUtils.setStartDate(new Date()) )
//			.addDate("endDate", DateUtils.setEndDate(new Date()))   			
//            .toJobParameters();
//    jobLauncher.run(processJob, jobParameters);
//	}
//	
//	@GetMapping("run")
//	 public String runJob() {
//	        JobParameters jobParameters = new JobParametersBuilder()
//	                .addLong("time", System.currentTimeMillis())
//	                .toJobParameters();
//	      //  processJob.execute(null);
//	        try {
//	            JobExecution execution = jobLauncher.run(processJob, jobParameters);
//	            System.out.println("Job Exit Status: " + execution.getStatus());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return e.getLocalizedMessage();
//	        }
//	       return"Success"; 
//	}
//	@GetMapping("run2")
//	 public String personProcessJob() {
//	        JobParameters jobParameters = new JobParametersBuilder()
//	                .addLong("time", System.currentTimeMillis())
//	                .toJobParameters();
//	      //  processJob.execute(null);
//	        try {
//	            JobExecution execution = jobLauncher.run(personProcessJob, jobParameters);
//	            System.out.println("Job Exit Status: " + execution.getStatus());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return e.getLocalizedMessage();
//	        }
//	       return"Success"; 
//	}

	@PostMapping("/runRegionJob")
	public ResponseEntity<String> runRegionJob(@RequestParam("file") MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println(file.getOriginalFilename());

		try {
			// Create the upload directory if it doesn't exist
			Path uploadPath = Path.of(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Copy the file to the upload directory
			Path filePath = uploadPath.resolve(fileName);
			System.out.println(filePath);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			JobParameters jobParameters = new JobParametersBuilder().addString("jobName", processJobRegion.getName())
					.addString("name", filePath.toString()).addLong("time", System.currentTimeMillis())
					.toJobParameters();
			return runJob(processJobRegion, jobParameters);
		} catch (IOException ex) {
			return new ResponseEntity("Error uploading file: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/runCustomerJob")
	public ResponseEntity<String> runCustomerJob(@RequestParam("file") MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println(file.getOriginalFilename());

		try {
			// Create the upload directory if it doesn't exist
			Path uploadPath = Path.of(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Copy the file to the upload directory
			Path filePath = uploadPath.resolve(fileName);
			System.out.println(filePath);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			JobParameters jobParameters = new JobParametersBuilder().addString("jobName", processJob.getName())
					.addString("name", filePath.toString()).addLong("time", System.currentTimeMillis())
					.toJobParameters();
			return runJob(processJob, jobParameters);
		} catch (IOException ex) {
			return new ResponseEntity("Error uploading file: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/runPersonJob")
	public ResponseEntity<String> runPersonJob() {
		JobParameters jobParameters = new JobParametersBuilder().addString("jobName", personProcessJob.getName())
				.addLong("time", System.currentTimeMillis()).toJobParameters();
		return runJob(personProcessJob, jobParameters);
	}

	private ResponseEntity<String> runJob(Job job, JobParameters jobParameters) {
		try {

			JobExecution jobExecution = jobLauncher.run(job, jobParameters);

			return ResponseEntity
					.ok("Batch Job " + job.getName() + " started with JobExecutionId: " + jobExecution.getId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to start Batch Job: " + e.getMessage());
		}
	}

//	
//	@PostMapping("/uploadFile")
//	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile)
//			throws IOException, SQLException {
//
//		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//		long size = multipartFile.getSize();
//		String filecode =saveFile(fileName, multipartFile);
//		System.out.println(filecode);
////		System.out.println(fileName);
////		System.out.println(filecode);
////		String rootPath = System.getProperty("user.dir");
////		Files.copy(Paths.get(rootPath + "/Files-Upload/" +  fileName),
////				Paths.get(rootPath + "/src/main/resources/db/changelog/" + filecode + "-" + fileName),
////				StandardCopyOption.REPLACE_EXISTING);
////		FileWriter fw = new FileWriter(rootPath + "/src/main/resources/db/changelog/liquibase-changelog2.yml", true);
////		BufferedWriter bw = new BufferedWriter(fw);
////		String content = "      file: db/changelog/" + filecode + "-" + fileName;
////		bw.append(content);
////		bw.newLine();
////		bw.close();
////		FileUploadResponse response = new FileUploadResponse();
////		response.setFileName(fileName);
////		response.setSize(size);
////		response.setDownloadUri("/downloadFile/" + filecode);
////		FileDownloadUtil obj = new FileDownloadUtil();
////		try {
////			onApplicationEvent();
////		} catch (LiquibaseException e) {
////			e.printStackTrace();
////			System.out.println(e.getMessage());
////		}
//		return new ResponseEntity<>("Done", HttpStatus.OK);
//	}
//	 public static String saveFile(String fileName, MultipartFile multipartFile)
//	            throws IOException {
//	        Path uploadPath = Paths.get("Files-Upload");
//	        System.out.println(uploadPath.toString());
//	          
//	        if (!Files.exists(uploadPath)) {
//	            Files.createDirectories(uploadPath);
//	        }
//	 
//	        //String fileCode = RandomStringUtils.randomAlphanumeric(8);
//	         
//	        try (InputStream inputStream = multipartFile.getInputStream()) {
//	            Path filePath = uploadPath.resolve( fileName);
//	            System.out.println(filePath.toString());
//	            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//	        } catch (IOException ioe) {       
//	            throw new IOException("Could not save file: " + fileName, ioe);
//	        }
//	         
//	        return fileName;
//	    }
//	
//	
//	 @RequestMapping(value="/import/file", method=RequestMethod.POST)
//	    public String create(@RequestParam("file") MultipartFile multipartFile) throws IOException{
//		 System.out.println("File Name :"+multipartFile.getOriginalFilename());
//		 String rootPath = System.getProperty("user.dir");
//		 System.out.println(rootPath);
//	        //Save multipartFile file in a temporary physical folder
//	        String path = new ClassPathResource(rootPath+"/src/main/resources/").getURL().getPath();//it's assumed you have a folder called tmpuploads in the resources folder
//	   	 System.out.println("path : "+path);
//	        File fileToImport = new File(path + multipartFile.getOriginalFilename());
//	        OutputStream outputStream = new FileOutputStream(fileToImport);
//	        IOUtils.copy(multipartFile.getInputStream(), outputStream);
//	        outputStream.flush();
//	        outputStream.close();       
//
////	        //Launch the Batch Job
////	        JobExecution jobExecution = jobLauncher.run(importUserJob, new JobParametersBuilder()
////	                .addString("fullPathFileName", fileToImport.getAbsolutePath())
////	                .toJobParameters());        
//
//	        return "OK";
//	    }

}
