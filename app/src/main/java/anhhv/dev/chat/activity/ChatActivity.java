package anhhv.dev.chat.activity;

import static anhhv.dev.chat.firebase.FirebaseQuery.USERNAME;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import anhhv.dev.chat.R;
import anhhv.dev.chat.adapter.ChatAdapter;
import anhhv.dev.chat.firebase.FirebaseQuery;
import anhhv.dev.chat.model.Chat;

public class ChatActivity extends AppCompatActivity {

    private String group;
    private RecyclerView recyclerView;
    private EditText edtInput;
    private Button btnSend;
    private List<Chat> objectArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.lvList);
        edtInput = findViewById(R.id.edtInput);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        group = getIntent().getStringExtra("data");

        setTitle(group);

        btnSend.setOnClickListener(view -> {
            String text = edtInput.getText().toString().trim();

            if (text.isEmpty()) {
                edtInput.setError(getString(R.string.notify_empty_text));
                return;
            }

            edtInput.setText("");
            btnSend.setEnabled(false);
            FirebaseQuery.sendMessage(group, text, USERNAME, System.currentTimeMillis(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    edtInput.setText("");
                }
            });

        });

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0){
                    btnSend.setEnabled(true);
                }else btnSend.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        objectArrayList = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, objectArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        FirebaseQuery.getListMessages(group, new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                objectArrayList.add(chat);
                chatAdapter.notifyItemChanged(objectArrayList.size());
                recyclerView.smoothScrollToPosition(objectArrayList.size());
                Log.e("ABC", "ACCC");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
