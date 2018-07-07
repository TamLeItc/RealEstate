package com.qtctek.realstate.presenter.user_action.update_user;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.dto.User_Object;
import com.qtctek.realstate.model.user_action.ModelUpdateUser;
import com.qtctek.realstate.view.user_action.update_user.ViewHandleUpdateUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterUpdateUser implements PresenterImpHandleUpdateUser {

    private ViewHandleUpdateUser mViewHandleUpdateUser;

    private ModelUpdateUser mModelUpdateUser;

    public PresenterUpdateUser(ViewHandleUpdateUser viewHandleUpdateUser){
        this.mViewHandleUpdateUser = viewHandleUpdateUser;
        this.mModelUpdateUser = new ModelUpdateUser(this);
    }

    public void handleUpdateUser(User user){
        this.mModelUpdateUser.requireUpdateUser(user);
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
