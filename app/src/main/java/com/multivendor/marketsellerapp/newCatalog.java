package com.multivendor.marketsellerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketsellerapp.Adapters.nbyshopAdapter;
import com.multivendor.marketsellerapp.Models.newProductModel;
import com.multivendor.marketsellerapp.Repos.catbdRepo;
import com.multivendor.marketsellerapp.ViewModels.homeViewModel;
import com.multivendor.marketsellerapp.databinding.FragmentNewCatalogBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newCatalog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newCatalog extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentNewCatalogBinding shbinding;
    private com.multivendor.marketsellerapp.Adapters.nbyshopAdapter nbyshopAdapter;
    private homeViewModel categfragViewModel;
    private String selcatname;
    private String mParam1;
    private String mParam2;
    private String lat;
    private String longit;
    private String userid;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    public newCatalog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newCatalog.
     */
    // TODO: Rename and change types and number of parameters
    public static newCatalog newInstance(String param1, String param2) {
        newCatalog fragment = new newCatalog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        categfragViewModel=new ViewModelProvider(getActivity()).get(homeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shbinding=FragmentNewCatalogBinding.inflate(inflater,container,false);
        getlatlong();

        viewfunctions();

        return shbinding.getRoot();
    }
    @SuppressLint("MissingPermission")
    private void getlatlong() {

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
            fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            }, Looper.getMainLooper());
            LocationManager manager = (LocationManager)getContext(). getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        lat = String.valueOf(location.getLatitude());
                        longit = String.valueOf(location.getLongitude());
                        Geocoder geocoder = new Geocoder(getContext()
                                , Locale.getDefault());

                        List<Address> addresses = null;
                        String cityname = null;
                        categfragViewModel.getlocation(userid,lat,longit,"Ludhiana");
                        categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                            @Override
                            public void onChanged(newProductModel.homeprodResult homeprodResult) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(homeprodResult.getAll_products()!=null) {
                                            Log.d("fucku",String.valueOf(homeprodResult.getAll_products().size()));
                                                if (homeprodResult.getAll_products().size() > 0) {

                                                    shbinding.searchres.setVisibility(View.VISIBLE);
                                                    nbyshopAdapter = new nbyshopAdapter(getContext(),homeprodResult.getAll_products());
                                                    GridLayoutManager glm = new GridLayoutManager(getContext(),2);
                                                    glm.setOrientation(RecyclerView.VERTICAL);
                                                    shbinding.searchres.setLayoutManager(glm);
                                                    shbinding.searchres.setAdapter(nbyshopAdapter);


                                            }
                                        }
                                    }
                                }, 200);
                            }
                        });

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            cityname=addresses.get(0).getLocality().toString();
                            categfragViewModel.getlocation(userid,lat,longit,"Ludhiana");
                            categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                                @Override
                                public void onChanged(newProductModel.homeprodResult homeprodResult) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(homeprodResult.getAll_products()!=null) {
                                                if (homeprodResult.getAll_products().size() > 0) {

                                                    shbinding.searchres.setVisibility(View.VISIBLE);
                                                    nbyshopAdapter = new nbyshopAdapter(getContext(),homeprodResult.getAll_products());
                                                    GridLayoutManager glm = new GridLayoutManager(getContext(),2);
                                                    glm.setOrientation(RecyclerView.VERTICAL);
                                                    shbinding.searchres.setLayoutManager(glm);
                                                    shbinding.searchres.setAdapter(nbyshopAdapter);
                                                    loadsearchres();
                                                }
                                            }
                                        }
                                    }, 200);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    } else {

                        LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
                                lat = String.valueOf(location1.getLatitude());
                                longit = String.valueOf(location1.getLongitude());
                                Geocoder geocoder = new Geocoder(getContext()
                                        , Locale.getDefault());
                                String cityname = null;
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    cityname=addresses.get(0).getLocality().toString();
                                    categfragViewModel.getlocation(userid,lat,longit,"Ludhiana");
                                    categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                                        @Override
                                        public void onChanged(newProductModel.homeprodResult homeprodResult) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(homeprodResult.getAll_products()!=null) {
                                                        if (homeprodResult.getAll_products().size() > 0) {
                                                            shbinding.searchres.setVisibility(View.VISIBLE);
                                                            nbyshopAdapter = new nbyshopAdapter(getContext(),homeprodResult.getAll_products());
                                                            GridLayoutManager glm = new GridLayoutManager(getContext(),2);
                                                            glm.setOrientation(RecyclerView.VERTICAL);
                                                            shbinding.searchres.setLayoutManager(glm);
                                                            shbinding.searchres.setAdapter(nbyshopAdapter);
                                                            loadsearchres();
                                                        }
                                                    }
                                                }
                                            }, 200);
                                        }
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    private void loadsearchres() {


        shbinding.catalsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                shbinding.searchres.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shbinding.searchres.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().isEmpty()) {
                    shbinding.searchres.setVisibility(View.VISIBLE);
                    searchfun(s.toString());
                }
            }
        });

    }

    private void searchfun(String query) {

        List<newProductModel.ListProductresp> searchedList = new ArrayList<>();
        //searchedList.clear();
        if(categfragViewModel.getnbyshopModel().getValue().getAll_products()!=null) {
            for (newProductModel.ListProductresp model : categfragViewModel.getnbyshopModel().getValue().getAll_products()) {

                if (model.getProduct_name().toString().toLowerCase().contains(query.toLowerCase()) ||
                        model.getProduct_category().toString().toLowerCase().contains(query.toLowerCase())) {

                    searchedList.add(model);
                }

            }
            nbyshopAdapter.searchList(searchedList);
        }
    }

    private void viewfunctions() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getViewModelStore().clear();
    }
}