package edu.mohibmir.covider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.mohibmir.covider.redis.RClass.Class;
import edu.mohibmir.covider.redis.RClass.User;
import edu.mohibmir.covider.redis.RedisDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link third_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class third_fragment extends Fragment {

    FragmentActivity listener;
    ArrayList<String> arrayList;


    public third_fragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_third_fragment, container, false);


        ImageView leftIcon = view.findViewById(R.id.left_icon);
        ImageView righticon = view.findViewById(R.id.right_icon);
        TextView title = view.findViewById(R.id.toolbar_title);

        righticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("STATE", "TOOLBAR CLICKED");
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.navbarmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()){
                        case R.id.navbarmenu_buildings:
                            Log.d("STATE", "NAV BAR BUILDINGS LIST CLICKED");
                            Intent in = new Intent(getActivity(), BuildingList.class);
                            startActivity(in);
                            return true;
                        case R.id.navbarmenu_notifications:
                            Log.d("STATE", "NAV BAR BUILDINGS NOTIFICATIONS CLICKED");
                            Intent myIntent = new Intent(getActivity(), NotificationActivity.class);
                            startActivity(myIntent);


                            return true;
                        case R.id.navbarmenu_settings:
                            Log.d("STATE", "NAV BAR MENU SETTINGS CLICKED");
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();

            }
        });


        ListView listView = (ListView) view.findViewById(R.id.listview);

        arrayList = new ArrayList<>();
        arrayList.add("");

        User user = new User(RedisDatabase.userId);

        if (user.getIsInstructor() == false)
        {
            populateStudent(user);
        }
        if (user.getIsInstructor() == true)
        {
            populateInstructor(user);
        }

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);


        return view;
    }

    public void populateStudent(User user) {

        arrayList.add(user.getFirstName() + " " + user.getLastName()  + "      " + user.getCovidStatus());
        arrayList.add("ID: " + user.getUserId());

        String string = user.getClass1();
        String inperson;
        for (int i = 1; i<=5; i++)
        {
            Class class1 = new Class(string);
            if (class1.getClassName() != "") {
                if (class1.getInPerson() == true) {
                    inperson = "In-person";
                } else {
                    inperson = "Online";
                }
                arrayList.add(class1.getClassName() + "          " + inperson);
                if (i == 2) string = user.getClass2();
                if (i == 3) string = user.getClass3();
                if (i == 4) string = user.getClass4();
                if (i == 5) string = user.getClass5();
            }
        }

    }
    public void populateInstructor(User user) {

        arrayList.add(user.getFirstName() + " " + user.getLastName()  + "      " + user.getCovidStatus());
        arrayList.add("ID: " + user.getUserId());


        String string = user.getClass1();
        Class class1 = new Class(string);
        List<String> studentList = class1.getStudents();
        String inperson;
        if (class1.getInPerson() == true) inperson = " In-Person";
        else inperson = " Online";

        arrayList.add(class1.getClassName() + inperson);

        for (int i = 0; i < studentList.size(); i ++) {
            User student = new User(studentList.get(i));
            if (student.getIsInstructor() == false) {
                arrayList.add(student.getFirstName() + " " + student.getLastName() + " " + student.getCovidStatus());
            }
        }





    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        listView=(ListView) listView.findViewById(R.id.listview);
//        listView.setAdapter(arrayAdapter);

    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment third_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static third_fragment newInstance(String param1, String param2) {
        third_fragment fragment = new third_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




}