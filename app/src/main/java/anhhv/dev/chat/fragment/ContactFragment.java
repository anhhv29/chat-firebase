package anhhv.dev.chat.fragment;

import static anhhv.dev.chat.firebase.FirebaseQuery.USERNAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anhhv.dev.chat.R;
import anhhv.dev.chat.activity.ChatActivity;
import anhhv.dev.chat.adapter.UserAdapter;
import anhhv.dev.chat.firebase.FirebaseQuery;
import anhhv.dev.chat.model.User;
import anhhv.dev.chat.utils.ItemClickSupport;

public class ContactFragment extends Fragment {

    private RecyclerView lvList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvList = view.findViewById(R.id.lvList);
        FirebaseQuery.getListUser(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, User>> objectsGTypeInd =
                        new GenericTypeIndicator<HashMap<String, User>>() {
                        };
                Map<String, User> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                final List<User> objectArrayList = new ArrayList<>(objectHashMap.values());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                UserAdapter userAdapter = new UserAdapter(getActivity(), objectArrayList);

                lvList.setAdapter(userAdapter);
                lvList.setLayoutManager(linearLayoutManager);
                lvList.setHasFixedSize(true);

                ItemClickSupport.addTo(lvList).setOnItemClickListener((recyclerView, position, v) -> {
                    String groupID = USERNAME + "|" + objectArrayList.get(position).username;
                    FirebaseQuery.checkExistGroup(groupID, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("data", dataSnapshot1.getKey());
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
