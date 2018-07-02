package com.qtctek.realstate.presenter.user_action.update_user;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.model.user_action.ModelInformationUser;
import com.qtctek.realstate.model.user_action.ModelUpdateUser;
import com.qtctek.realstate.view.user_action.update_user.ViewHandleUpdateUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterUpdateUser implements PresenterImpHandleUpdateUser {

    private ViewHandleUpdateUser mViewHandleUpdateUser;

    private ModelUpdateUser mModelUpdateUser;
    private ModelInformationUser mModelInformationUser;

    public PresenterUpdateUser(ViewHandleUpdateUser viewHandleUpdateUser){
        this.mViewHandleUpdateUser = viewHandleUpdateUser;
        this.mModelUpdateUser = new ModelUpdateUser(this);
        this.mModelInformationUser = new ModelInformationUser(this);
    }

    public void handleUpdateUser(User user){
        this.mModelUpdateUser.requireUpdateUser(user);
    }

    public void handleGetInformationUser(String email){
        this.mModelInformationUser.requireGetInformationUser(email);
    }

    @Override
    public void onUpdateUserSuccessful() {
        this.mViewHandleUpdateUser.onUpdateUserSuccessful();
    }

    @Override
    public void onUpdateUserError(String error) {
        this.mViewHandleUpdateUser.onUpdateUserError(error);
    }

    @Override
    public void onGetInformationUserSuccessful(String data){
        try {

            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            User user = new User();
            user.setIdUser(jsonObject.getInt("id"));
            user.setName(jsonObject.getString("name"));
            user.setEmail(jsonObject.getString("email"));
            user.setPhoneNumber(jsonObject.getString("phone_number"));

            this.mViewHandleUpdateUser.onHandleInformationUserSuccessful(user);

        } catch (JSONException e) {
            this.mViewHandleUpdateUser.onHandleInformationUserError(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onGetInformationUserError(String error) {
        this.mViewHandleUpdateUser.onHandleInformationUserError(error);
    }

}
