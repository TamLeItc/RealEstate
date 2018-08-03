package com.qtctek.aladin.presenter.user_action.update_user;

import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.model.user_action.ModelUpdateUser;
import com.qtctek.aladin.view.user_action.update_user.ViewHandleUpdateUser;

public class PresenterUpdateUser implements PresenterImpHandleUpdateUser {

    private ViewHandleUpdateUser mViewHandleUpdateUser;

    private ModelUpdateUser mModelUpdateUser;

    public PresenterUpdateUser(ViewHandleUpdateUser viewHandleUpdateUser){
        this.mViewHandleUpdateUser = viewHandleUpdateUser;
        this.mModelUpdateUser = new ModelUpdateUser(this);
    }

    public void handleUpdateUser(User user, String oldPassword){
        this.mModelUpdateUser.requireUpdateUser(user, oldPassword);
    }

    @Override
    public void onUpdateUserSuccessful() {
        this.mViewHandleUpdateUser.onUpdateUserSuccessful();
    }

    @Override
    public void onUpdateUserError(String error) {
        this.mViewHandleUpdateUser.onUpdateUserError(error);
    }

}
