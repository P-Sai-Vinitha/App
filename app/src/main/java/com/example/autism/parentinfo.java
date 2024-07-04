package com.example.autism;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class parentinfo extends AppCompatActivity {

    private List<Parent> parentList;
    private ListView listView;
    private ParentAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentinfo);

        listView = findViewById(R.id.myListView);
        searchView = findViewById(R.id.searchView);
        parentList = new ArrayList<>();
        adapter = new ParentAdapter(this, parentList);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ParentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Parent selectedPatient = parentList.get(position);
                Intent intent = new Intent(parentinfo.this, parentdetails.class);
                intent.putExtra("username", selectedPatient.getId());
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });

        // Make a request to your PHP file
        String url = ip.ipn + "parentlistview.php";
        makeRequest(url);

        // Set up SearchView
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
                // Filter the parent list based on the entered text
                filterParentList(newText);
                return true;
            }
        });
    }

    private void filterParentList(String searchText) {
        Log.d("SearchText", searchText);

        List<Parent> filteredList = new ArrayList<>();

        // Trim leading and trailing whitespaces
        searchText = searchText.trim().toLowerCase();

        for (Parent parent : parentList) {
            Log.d("ParentName", parent.getName());
            if (parent.getName().toLowerCase().contains(searchText)) {
                filteredList.add(parent);
            }
        }

        // Update the adapter with the filtered list
        adapter.setFilteredList(filteredList);
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
                        Toast.makeText(parentinfo.this, "Error fetching data", Toast.LENGTH_SHORT).show();
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
                parentList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject parentObject = jsonArray.getJSONObject(i);

                    // Check if "id" and "parentname" keys exist
                    if (parentObject.has("id") && parentObject.has("parentname") && parentObject.has("profileimage")) {
                        // Extract values
                        String id = parentObject.getString("id");
                        String parentName = parentObject.getString("parentname");
                        String profileImageBase64 = parentObject.getString("profileimage");

                        // Add this parent to your parentList
                        parentList.add(new Parent(id, parentName, profileImageBase64));
                    } else {
                        // Handle missing key(s)
                        // You may choose to skip this entry or handle it in another way
                        // For example, you can log a message or show a toast
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
