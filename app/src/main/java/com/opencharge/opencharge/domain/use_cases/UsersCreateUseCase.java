package com.opencharge.opencharge.domain.use_cases;

import android.util.Pair;

import com.opencharge.opencharge.domain.use_cases.base.UseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 24/05/2017.
 */

public interface UsersCreateUseCase extends UseCase {
    interface Callback {
        void onUserCreated(String id);
    }

    public void setUserParameters(String name, String photo, String email,String password,
                                  ArrayList<Pair<String,String>> puntsCreats,
                                  ArrayList<Pair<String,String>> puntsReservats);
}
