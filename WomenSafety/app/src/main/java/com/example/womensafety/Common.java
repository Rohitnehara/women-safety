package com.example.womensafety;

import com.example.womensafety.model.Results;
import com.example.womensafety.Remote.IGoogleAPIService;
import com.example.womensafety.Remote.RetrofitClient;
import com.example.womensafety.Remote.RetrofitScalarsClient;

public class Common {


    public static Results currentResult;
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

    public static IGoogleAPIService getGoogleAPIServiceScalars()
    {
        return RetrofitScalarsClient.getScalarClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
