package com.example.craterradar;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Currency;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HomeUserSide extends Fragment implements OnMapReadyCallback{
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int LOCATION_REQUEST_CODE = 101 ;
    Context context;
    LatLng Current_latLng,Destination_latLng;
    SearchView Destination_sv;
    ProgressBar progressBar;
    Button ShowRouteInfo;
    private MapView mapView;
    private GoogleMap googleMap;
    private String Distance,Duration;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public HomeUserSide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_user_side, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.direction_progress);
        Destination_sv = view.findViewById(R.id.sv_location);
        mapView = view.findViewById(R.id.home_map);
        ShowRouteInfo = view.findViewById(R.id.show_detail_btn);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync( this);
        context = getActivity().getApplicationContext();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("SearchedRoutes");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fetchCurrentLocation();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Destination_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                googleMap.clear();
                List<Address> search_dest_list = null;
                if (!query.isEmpty() || query.contentEquals("")) {
                    Geocoder geocoder = new Geocoder(context);
                    try {
                        search_dest_list = geocoder.getFromLocationName(query, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(context,"Location Not Found.Please enter again!",Toast.LENGTH_LONG).show();
                    }
                    if (search_dest_list.size() != 0) {
                        String searched_history = search_dest_list.toString();
                        Log.i("Searched History", searched_history);

                        progressBar.setVisibility(View.GONE);
                        Address dest_address = search_dest_list.get(0);
                        Destination_latLng = new LatLng(dest_address.getLatitude(), dest_address.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(Destination_latLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Destination_latLng, 15));
                        String Searched_Route_ID = UUID.randomUUID().toString();
                        databaseReference.child(Searched_Route_ID).child("Origin_Lat").setValue(Current_latLng.latitude);
                        databaseReference.child(Searched_Route_ID).child("Origin_Long").setValue(Current_latLng.longitude);
                        databaseReference.child(Searched_Route_ID).child("Destination_Lat").setValue(Destination_latLng.latitude);
                        databaseReference.child(Searched_Route_ID).child("Destination_Long").setValue(Destination_latLng.longitude);

                        //Test
                        getplaceName(Current_latLng.latitude,Current_latLng.longitude);
                        getplaceName(Destination_latLng.latitude,Destination_latLng.longitude);

                        Destination_sv.clearFocus();
                        drawPolyLine();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Location Not Found.Please enter again!", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapView.getMapAsync(this);
        ShowRouteInfo.setVisibility(View.GONE);
    }
    /** Test Start**/
    public void getplaceName(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.e(" Address",  add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /** Test End **/
    private void drawPolyLine() {
        progressBar.setVisibility(View.VISIBLE);
        String url = getRequesturl(Current_latLng,Destination_latLng);
        TaskRequestDirection taskRequestDirection = new TaskRequestDirection();
        taskRequestDirection.execute(url);
    }


    private void fetchCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    //Toast.makeText(context,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    Current_latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    MarkerOptions CurrentLocationMarker = new MarkerOptions().position(Current_latLng);
                    googleMap.addMarker(CurrentLocationMarker);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(Current_latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Current_latLng,15));
                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    /** Set My Location button Position  Change Start**/
                    View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    rlp.setMargins(0, 0, 30, 280);
                    /** Set My Location button Position  Change End**/
                    googleMap.setMyLocationEnabled(true);

                }else{
                    Toast.makeText(context,"No Location recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchCurrentLocation();
                } else {
                    Toast.makeText(context,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private String getRequesturl(LatLng c_latLng, LatLng latLng)
    {
        String str_org = "origin="+c_latLng.latitude+","+c_latLng.longitude;
        Log.i("Origin:",str_org);

        String str_dest = "destination="+latLng.latitude+","+latLng.longitude;
        Log.i("Destination:",str_dest);

        String sensor = "sensor=false";

        String mode = "mode=driving";

        String param = str_org+"&"+str_dest+"&"+sensor+"&"+mode;
        Log.i("Param:",param);

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param +"&key=" +"AIzaSyCeDCX845f9rdcke1j3lmXwKpyaAbK5Dus";
        Log.i("FINAL URL:",url);
        return url;
    }

    public String requestDirection(String requrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(requrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream!= null)
            {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }


    public class TaskRequestDirection extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParse taskParse = new TaskParse();
            taskParse.execute(s);
        }
    }

    public class TaskParse extends AsyncTask< String,Void,List<List<HashMap<String,String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionParser directionParse = new DirectionParser();
                routes = directionParse.parse(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            progressBar.setVisibility(View.GONE);
            ArrayList dots = null;

            //Get Time and Distance From Origin to Destination
            getDestinationInfo(Destination_latLng);


            PolylineOptions polylineOptions = null;
            for (List<HashMap<String, String>> paths : lists) {
                dots = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : paths) {
                    double lat = Double.parseDouble(point.get("lat").trim());
                    double lon = Double.parseDouble(point.get("lng").trim());

                    dots.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(dots);
                polylineOptions.width(15);
                polylineOptions.color(R.color.AppPrimary);
                polylineOptions.geodesic(true);
            }
            googleMap.addPolyline(polylineOptions);
        }

        private void getDestinationInfo(LatLng latLngDestination) {
            progressBar.setVisibility(View.VISIBLE);
            String serverKey = getResources().getString(R.string.map_key); // Api Key For Google Direction API \\
            final LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            final LatLng destination = latLngDestination;
            //-------------Using AK Exorcist Google Direction Library---------------\\
            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            progressBar.setVisibility(View.GONE);
                            String status = direction.getStatus();
                            if (status.equals(RequestResult.OK)) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                Distance = distanceInfo.getText();
                                Duration = durationInfo.getText();

                                /** ------------Displaying Distance and Time----------------- **/
                                Toast.makeText(context,"Distance:"+Distance+"\n"+"Duration:"+Duration,Toast.LENGTH_LONG).show();
                               if(!Duration.isEmpty() && !Distance.isEmpty())
                                {
                                    ShowRouteInfo.setVisibility(View.VISIBLE);
                                    ShowRouteInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RouteInfoSheet routeInfoSheet = new RouteInfoSheet(Distance, Duration);
                                            routeInfoSheet.show(getActivity().getSupportFragmentManager(), "route_info");
                                            routeInfoSheet.getEnterTransition();
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(context,"Sorry you didn't search any route.",Toast.LENGTH_LONG).show();
                                }


                                /** --------------Drawing Path----------------- **/
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(),
                                        directionPositionList, 5, getResources().getColor(R.color.colorPrimary));
                                googleMap.addPolyline(polylineOptions);
                                /** -------------------------------------------- **/

                                /** -----------Zooming the map according to marker bounds------------- **/
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(destination);
                                LatLngBounds bounds = builder.build();

                                int width = getResources().getDisplayMetrics().widthPixels;
                                int height = getResources().getDisplayMetrics().heightPixels;
                                int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                googleMap.animateCamera(cu);
                                /** ------------------------------------------------------------------ **/

                            } else if (status.equals(RequestResult.NOT_FOUND)) {
                                Toast.makeText(context, "No routes exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                        }
                    });
            //-------------------------------------------------------------------------------\\

        }
    }

}
