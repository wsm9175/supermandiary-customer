package com.lodong.spring.supermandiarycustomer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class SupermandiaryCustomerApplication {
	@Value("${app.firebase-configuration-file}")
	private static String firebaseConfigPath = "firebase/superman-diary-firebase-adminsdk-yejxp-f18ef56540.json";

	public static void main(String[] args) {
		SpringApplication.run(SupermandiaryCustomerApplication.class, args);

		try {
			ClassPathResource resource = new ClassPathResource(firebaseConfigPath);
			InputStream serviceAccount = resource.getInputStream();
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();
			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
