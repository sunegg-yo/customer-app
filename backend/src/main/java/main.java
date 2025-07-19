package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.Map;

@SpringBootApplication
@RestController
public class Main {

    @PostConstruct
    public void initFirebase() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://customer-app-62f73.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }

    @PostMapping("/add")
    public String addCustomer(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("customers")
                .push();
        ref.setValueAsync(payload);

        return "OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
