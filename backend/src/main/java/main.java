package main;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class Main {

    @PostConstruct
    public void initFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("backend/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://customer-app-62f73.firebaseio.com/") // ←ご自身のURLに変更
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase 初期化成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public String addCustomer(@RequestParam String name, @RequestParam String email) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("customers")
                    .push();

            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);
            ref.setValueAsync(data);

            System.out.println("登録成功: " + data);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
