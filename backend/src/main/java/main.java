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
import java.util.HashMap;
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
        System.out.println("Firebase 初期化成功");
    }

    @PostMapping("/add")
    public String addCustomer(@RequestParam String name, @RequestParam String email) {
        System.out.println("データ受信: name=" + name + ", email=" + email);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("customers")
                .push();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);

        ref.setValueAsync(data);

        System.out.println("Firebase 登録送信");

        return "OK";
    }
}
