package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore objectFirebaseFireStore;
    private static String STUDENT = "student";
    private String mylist;
    private static final String Collection = "Student";
    private Dialog objectDialog;
    private EditText Et1, Et2, Et3;
    private TextView TextV;
    private DocumentReference objectDocumentReference;
    private CollectionReference ObjectCollectioReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            objectFirebaseFireStore = FirebaseFirestore.getInstance();
            objectDialog = new Dialog(this);
            objectDialog.setContentView(R.layout.please_wait);
            objectDialog.setCancelable(false);
            Et1 = findViewById(R.id.Et1);
            Et2 = findViewById(R.id.Et2);
            Et3 = findViewById(R.id.Et3);
            TextV = findViewById(R.id.TextV);
        } catch (Exception e) {
            Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddValues(View v) {
        try {
            if (!Et1.getText().toString().isEmpty() && !Et2.getText().toString().isEmpty()
                    && !Et3.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("studentName", Et2.getText().toString());
                objectMap.put("gender", Et3.getText().toString());
                objectFirebaseFireStore.collection(STUDENT)
                        .document(Et1.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Et1.setText("");
                                Et2.setText("");
                                Et3.setText("");
                                Et1.requestFocus();
                                Toast.makeText(MainActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "fails to add data", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "Add values in FireStore", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetValues(View v) {
        objectDialog.show();
               // objectDocumentReference = objectFirebaseFireStore.collection(STUDENT).document(Et1.getText().toString());
              objectFirebaseFireStore.collection(Collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()) {

                          for (QueryDocumentSnapshot doc : task.getResult()) {
                              mylist += "\n Student Name " + doc.getId();
                          }
                          TextV.setText(mylist);
                          Toast.makeText(MainActivity.this, "get Successfully", Toast.LENGTH_SHORT).show();
                      } else {
                          objectDialog.dismiss();
                          Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                      }
                  }
              });
    }


                  public void Delete(View v) {
                      try {
                          objectDialog.show();
                          objectFirebaseFireStore.collection(Collection).get()
                                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                          if (task.isSuccessful()) {
                                              for (QueryDocumentSnapshot doc : task.getResult()) {
                                                  objectFirebaseFireStore.collection(Collection).document(doc.getId()).delete();
                                              }
                                              Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                                          } else {
                                              objectDialog.dismiss();

                                          }
                                      }
                                  });
                      } catch (Exception e) {
                          objectDialog.dismiss();
                          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                      }
                  }
              }
