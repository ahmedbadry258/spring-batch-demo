package com.batch;

import java.io.File;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;

import com.batch.demo.model.Customer;
import com.batch.demo.model.Person;
import com.batch.demo.model.Region;
import com.batch.demo.repositories.CustomerRepository;
import com.batch.demo.repositories.PersonRepository;
import com.batch.demo.repositories.RegionRepository;



@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegionRepository regionRepository;

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> reader(@Value("#{jobParameters['name']}") String fileName) {
		System.out.println("fileName : " + fileName);
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

		// reader.setResource(new ClassPathResource("customers.csv"));
		// reader.setResource(new FileSystemResource("D:\\cut-files\\"+fileName));
		reader.setResource(new FileSystemResource(fileName));
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());
		return reader;
	}

	@Bean
	public LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("id", "firstName", "lastName");
		tokenizer.setDelimiter(","); // Set the delimiter to semicolon (;)
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(
				(fieldSet) -> new Customer(fieldSet.readLong(0), fieldSet.readString(1), fieldSet.readString(2)));
		return mapper;
	}

	@Bean
	public ItemProcessor<Customer, Customer> processor() {
		return customer -> {
			System.out.println(customer.toString());
			// Perform any processing/transformation on the customer
			// For simplicity, we'll just return the same customer object
			return customer;
		};
	}

//    @Bean
//    public JdbcBatchItemWriter<Customer> writer() {
//        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        writer.setSql("INSERT INTO customers (id, first_name, last_name) VALUES (:id, :firstName, :lastName)");
//        writer.setDataSource(dataSource);
//        return writer;
//    }
	@Bean
	public RepositoryItemWriter<Customer> repositoryItemWriter() {
		RepositoryItemWriter<Customer> iwriter = new RepositoryItemWriter<>();
		iwriter.setRepository(customerRepository);
		iwriter.setMethodName("save");
		return iwriter;

	}

	@Bean
	public Step processStep(ItemReader<Customer> reader, ItemWriter<Customer> writer,
			ItemProcessor<Customer, Customer> processor) {
		return stepBuilderFactory.get("processStep").<Customer, Customer>chunk(3).reader(reader).processor(processor)
				.writer(writer).build();
	}

	@Bean
	public Job processJob(Step processStep) {
		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).flow(processStep).end().build();
	}

	// *******************************Person
	// ****************************************************
	@Bean
	public FlatFileItemReader<Person> personReader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();

		reader.setResource(new ClassPathResource("test.csv"));

		reader.setLinesToSkip(1);

		reader.setLineMapper(personLineMapper());
		return reader;
	}

	@Bean
	public LineMapper<Person> personLineMapper() {
		DefaultLineMapper<Person> mapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("source_id", "first_name", "middle_initial", "last_name", "email_address", "phone_number",
				"street", "city", "state", "zip", "birthdate", "action", "ssn");
		tokenizer.setDelimiter(","); // Set the delimiter to semicolon (;)

		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper((fieldSet) -> new Person(fieldSet.readString(0), fieldSet.readString(1),
				fieldSet.readString(2), fieldSet.readString(3), fieldSet.readString(4), fieldSet.readString(5),
				fieldSet.readString(6), fieldSet.readString(7), fieldSet.readString(8), fieldSet.readString(9),
				fieldSet.readString(10), fieldSet.readString(11), fieldSet.readString(12)));
		return mapper;
	}

	@Autowired
	private ItemReader<Person> personItemReader;
	@Autowired
	private ItemWriter<Person> personItemWriter;
	@Autowired
	private ItemProcessor<Person, Person> personItemProcessor;

	@Bean
	public Step personProcessStep() {
		return stepBuilderFactory.get("personProcessStep3").<Person, Person>chunk(10).reader(personItemReader)
				// .processor(personProcessJob())
				.processor(personItemProcessor).writer(personItemWriter).build();
	}

	@Bean
	public Job personProcessJob() {
		return jobBuilderFactory.get("personProcessJob3").incrementer(new RunIdIncrementer())
				// .flow(personProcessStep)
				.start(personProcessStep())
				// .end()
				.build();
	}

	@Bean
	public ItemProcessor<Person, Person> personProcessor() {
		return person -> {
			System.out.println(person.toString());
			// Perform any processing/transformation on the customer
			// For simplicity, we'll just return the same customer object
			return person;
		};
	}

	@Bean
	public RepositoryItemWriter<Person> personRepositoryItemWriter() {
		RepositoryItemWriter<Person> iwriter = new RepositoryItemWriter<>();
		iwriter.setRepository(personRepository);
		iwriter.setMethodName("save");
		return iwriter;

	}

	// **************************************** Region Section
	// ************************************

	@Bean
	@StepScope
	public FlatFileItemReader<Region> readerRegion(@Value("#{jobParameters['name']}") String fileName) {
		System.out.println("fileName : " + fileName);
		FlatFileItemReader<Region> reader = new FlatFileItemReader<>();
		try {
		// reader.setResource(new ClassPathResource("customers.csv"));
		// reader.setResource(new FileSystemResource("D:\\cut-files\\"+fileName));
		reader.setResource(new FileSystemResource(fileName));
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapperRegion());
		reader.afterPropertiesSet();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reader;
	}

	@Bean
	public LineMapper<Region> lineMapperRegion() {
		DefaultLineMapper<Region> mapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"REGION_ID", "REGION_NAME"});
		tokenizer.setDelimiter(","); // Set the delimiter to semicolon (;)
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper((fieldSet) -> new Region(fieldSet.readLong(0), fieldSet.readString(1), fieldSet.readString(2)));
		return mapper;
	}

	@Bean
	public ItemProcessor<Region, Region> processorRegion() {
		return region -> {
			System.out.println(region.toString());
			// Perform any processing/transformation on the customer
			// For simplicity, we'll just return the same customer object
			return region;
		};
	}

//	    @Bean
//	    public JdbcBatchItemWriter<Customer> writer() {
//	        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
//	        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//	        writer.setSql("INSERT INTO customers (id, first_name, last_name) VALUES (:id, :firstName, :lastName)");
//	        writer.setDataSource(dataSource);
//	        return writer;
//	    }
	@Bean
	public RepositoryItemWriter<Region> repositoryItemWriterRegion() {
		RepositoryItemWriter<Region> iwriter = new RepositoryItemWriter<>();
		iwriter.setRepository(regionRepository);
		iwriter.setMethodName("save");
		return iwriter;

	}

	@Bean
	public Step processStepRegion(ItemReader<Region> reader, ItemWriter<Region> writer,
			ItemProcessor<Region, Region> processor) {
		return stepBuilderFactory.get("processStepRegion").<Region, Region>chunk(5).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public Job processJobRegion(Step processStep) {
		return jobBuilderFactory.get("processJobRegion").incrementer(new RunIdIncrementer()).flow(processStep).end()
				.build();
	}

}
