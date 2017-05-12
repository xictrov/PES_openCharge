package com.opencharge.opencharge.presentation.locators;

import android.content.Context;
import android.util.Log;

import com.opencharge.opencharge.domain.use_cases.AddCommentUseCase;
import com.opencharge.opencharge.domain.use_cases.PointsCreateUseCase;
import com.opencharge.opencharge.domain.use_cases.ServiceCreateUseCase;
import com.opencharge.opencharge.domain.use_cases.impl.PointsCreateUseCaseImpl;
import com.opencharge.opencharge.domain.use_cases.PointByIdUseCase;
import com.opencharge.opencharge.domain.use_cases.PointsListUseCase;
import com.opencharge.opencharge.domain.use_cases.UserLocationUseCase;
import com.opencharge.opencharge.domain.use_cases.impl.PointByIdUseCaseImpl;
import com.opencharge.opencharge.domain.use_cases.impl.PointsListUseCaseImpl;
import com.opencharge.opencharge.domain.use_cases.impl.ServiceCreateUseCaseImpl;
import com.opencharge.opencharge.domain.use_cases.impl.UserLocationUseCaseImpl;
import com.opencharge.opencharge.domain.use_cases.impl.AddCommentUseCaseImpl;

import java.util.List;

/**
 * Created by ferran on 22/3/17.
 */

public class UseCasesLocator {
    private static UseCasesLocator instance;

    private UseCasesLocator() {
    }

    public static UseCasesLocator getInstance() {
        if(instance == null) {
            instance = new UseCasesLocator();
        }
        return instance;
    }

    public PointsListUseCase getPointsListUseCase(PointsListUseCase.Callback callback) {
        return new PointsListUseCaseImpl(
                ServicesLocator.getInstance().getExecutor(),
                ServicesLocator.getInstance().getMainThread(),
                RepositoriesLocator.getInstance().getPointsRepository(),
                callback
        );
    }

    public PointByIdUseCase getPointByIdUseCase(PointByIdUseCase.Callback callback) {
        return new PointByIdUseCaseImpl(
                ServicesLocator.getInstance().getExecutor(),
                ServicesLocator.getInstance().getMainThread(),
                RepositoriesLocator.getInstance().getPointsRepository(),
                callback
        );
    }

    public UserLocationUseCase getUserLocationUseCase(Context context, UserLocationUseCase.Callback callback) {
        ServicesLocator sl = ServicesLocator.getInstance();
        return new UserLocationUseCaseImpl(
                sl.getExecutor(),
                sl.getMainThread(),
                sl.getUserLocationService(context),
                callback
        );
    }

    public PointsCreateUseCase getPointsCreateUseCase(PointsCreateUseCase.Callback callback) {
        return new PointsCreateUseCaseImpl(
                ServicesLocator.getInstance().getExecutor(),
                ServicesLocator.getInstance().getMainThread(),
                RepositoriesLocator.getInstance().getPointsRepository(),
                callback);
    }

    public AddCommentUseCase getAddCommentUseCase(AddCommentUseCase.Callback callback) {
        ServicesLocator sl = ServicesLocator.getInstance();
        return new AddCommentUseCaseImpl(
                sl.getExecutor(),
                sl.getMainThread(),
                RepositoriesLocator.getInstance().getCommentsRepository(),
                callback
        );
    }

    public ServiceCreateUseCase getServiceCreateUseCase(ServiceCreateUseCase.Callback callback) {
        ServicesLocator sl = ServicesLocator.getInstance();
        //TODO: replace this
        return new ServiceCreateUseCaseImpl(
                sl.getExecutor(),
                sl.getMainThread(),
                RepositoriesLocator.getInstance().getServiceRepository(),
                callback
        );
    }
}
