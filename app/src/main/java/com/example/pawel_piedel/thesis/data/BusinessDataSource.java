package com.example.pawel_piedel.thesis.data;

import com.example.pawel_piedel.thesis.data.model.BusinessCategory;

/**
 * Created by Pawel_Piedel on 28.10.2017.
 */

public interface BusinessDataSource {
    void getBusinesses(BusinessCategory category);

    void getBusiness(String id);
}
