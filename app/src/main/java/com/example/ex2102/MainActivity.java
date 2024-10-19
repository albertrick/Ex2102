package com.example.ex2102;

import static com.example.ex2102.FBRef.refAuth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText eTEmail, eTPass;
    private TextView tVMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTEmail = findViewById(R.id.eTEmail);
        eTPass = findViewById(R.id.eTPass);
        tVMsg = findViewById(R.id.tVMsg);
    }

    public void createUser(View view) {
        String email = eTEmail.getText().toString();
        String pass = eTPass.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) {
            tVMsg.setText("Please fill all fields");
        } else {
            refAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("MainActivity", "createUserWithEmailAndPassword:success");
                                FirebaseUser user = refAuth.getCurrentUser();
                                tVMsg.setText("User created successfully\nUid: "+user.getUid());
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    tVMsg.setText("Invalid email address.");
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    tVMsg.setText("Password too weak.");
                                } catch (FirebaseAuthUserCollisionException e) {
                                    tVMsg.setText("User already exists.");
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    tVMsg.setText("General authentication failure.");
                                } catch (FirebaseNetworkException e) {
                                    tVMsg.setText("Network error. Please check your connection and try again.");
                                } catch (Exception e) {
                                    tVMsg.setText("An error occurred. Please try again later.");
                                }
                            }
                        }
                    });
        }
    }
}