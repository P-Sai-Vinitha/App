package com.example.autism;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.autism.ChildAdapter;
import com.example.autism.R;
import com.example.autism.child;
import com.example.autism.graphdoctor;
import com.example.autism.ip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class childprogress extends AppCompatActivity {

    private List<child> childList;
    private List<child> filteredChildList; // New list to store filtered results
    private ListView listView;
    private ChildAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childprogress);

        listView = findViewById(R.id.myListView);
        searchView = findViewById(R.id.searchView);

        childList = new ArrayList<>();
        filteredChildList = new ArrayList<>(); // Initialize the filtered list

        adapter = new ChildAdapter(this, childList);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                child selectedChild = childList.get(position);
                Intent intent = new Intent(childprogress.this, graphdoctor.class);
                intent.putExtra("emailKey", selectedChild.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });

        makeRequest(ip.ipn + "childdetails.php");

        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the child list based on the entered text
                filterChildList(newText);
                return true;
            }
        });
    }

    private void filterChildList(String searchText) {
        Log.d("SearchText", searchText);


        filteredChildList.clear();

        // Trim leading and trailing whitespaces
        searchText = searchText.trim().toLowerCase();

        // Iterate through the original childList and add matching items to the filtered list
        for (child child : childList) {
            Log.d("ChildName", child.getName());
            if (child.getName().toLowerCase().contains(searchText)) {
                filteredChildList.add(child);
            }
        }


        // Update the adapter with the filtered list
        adapter.setFilteredList(filteredChildList);
    }



    private void makeRequest(String url) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.d("Volley Response", response);
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(childprogress.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                // Clear existing data
                childList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject childObject = jsonArray.getJSONObject(i);

                    if (childObject.has("id") && childObject.has("childname")) {
                        String id = childObject.getString("id");
                        String childName = childObject.getString("childname");

                        Log.d("ChildInfo", "ID: " + id + ", Name: " + childName);
                        childList.add(new child(id, childName));
                    } else {
                        Log.e("parseResponse", "Missing key(s) for object at index " + i);
                    }
                }


                // Notify your adapter that the data set has changed
                adapter.notifyDataSetChanged();
            } else {
                // Handle empty response
                Toast.makeText(this, "Empty response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
