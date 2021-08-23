package com.cognizant.cde;

import com.cognizant.cde.services.FileUtilityService;
import com.cognizant.cde.services.IdentifyRabbitQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RabbitMQMigratorApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(RabbitMQMigratorApplication.class);
	private final IdentifyRabbitQueueService identifyRabbitQueueService = new IdentifyRabbitQueueService(new FileUtilityService());

	public static void main(String[] args) {

		SpringApplication.run(RabbitMQMigratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("### Command Line Arguments : " + Arrays.toString(args));

		if (args.length != 2) {
			System.out.println("Usage: RabbitMQMigratorApplication <<SourcePath>> <<DestinationPath>>");
			return;
		}

		String projectRootPath = args[0];
		String projectDestinationPath = args[1];

		identifyRabbitQueueService.processMQMigrate(projectRootPath, projectDestinationPath);

	}
}
