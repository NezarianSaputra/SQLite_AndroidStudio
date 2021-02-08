package com.example.sqlite_nezarian.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqlite_nezarian.DatabaseHelper;
import com.example.sqlite_nezarian.PersonBean;
import com.example.sqlite_nezarian.R;
import com.example.sqlite_nezarian.RecyclerviewAdapter;

import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener, RecyclerviewAdapter.OnUserClickListener{

    RecyclerView recyclerView;
    EditText edtName, edtAge;
    Button btnSubmit;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<PersonBean> listPersonInfo;

    public MainFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        recyclerView = view.findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        edtName = view.findViewById(R.id.edtName);
        edtAge = view.findViewById(R.id.edtAge);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        setupRecycleView();
        // TODO: Use the ViewModel
    }

    public void setupRecycleView() {
        DatabaseHelper db = new DatabaseHelper(context);
        listPersonInfo = db.selectUserData();

        RecyclerviewAdapter adapter = new RecyclerviewAdapter(context, listPersonInfo, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v){
        if (v.getId() == R.id.btnSubmit) {
            DatabaseHelper db = new DatabaseHelper(context);
            PersonBean currentPerson = new PersonBean();
            String btnStatus = btnSubmit.getText().toString();
            if (btnStatus.equals("Submit")){
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.insert(currentPerson);
            }
            if (btnStatus.equals("Update")) {
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.update(currentPerson);
            }
            setupRecycleView();
            edtName.setText("");
            edtAge.setText("");
            edtName.setFocusable(true);
            btnSubmit.setText("Submit");
        }
    }

    public void onUserClick(PersonBean currentPerson, String action) {
        if (action.equals("Edit")) {
            edtName.setText(currentPerson.getName());
            edtName.setFocusable(true);
            edtAge.setText(currentPerson.getAge()+"");
            btnSubmit.setText("Update");
        }
        if (action.equals("Delete")) {
            DatabaseHelper db = new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecycleView();
        }
    }

}