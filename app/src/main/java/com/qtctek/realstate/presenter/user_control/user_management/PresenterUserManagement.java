package com.qtctek.realstate.presenter.user_control.user_management;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.model.user_control.ModelUserManagement;
import com.qtctek.realstate.view.user_control.user_management.ViewHandleUserManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterUserManagement implements PresenterImpHandleUserManagement{

    private ViewHandleUserManagement mViewHandleUserManagement;
    private ModelUserManagement mModelUserManagement;

    private int mQualityUser;

    public PresenterUserManagement(ViewHandleUserManagement viewHandleUserManagement){
        this.mViewHandleUserManagement = viewHandleUserManagement;
        this.mModelUserManagement = new ModelUserManagement(this);
    }

    public void handleGetUserList(int start, int quality){
        this.mModelUserManagement = new ModelUserManagement(this);
        this.mModelUserManagement.requireUserList(start, quality);
    }

    public void handleUpdateStatusUser(int userId){
        this.mModelUserManagement.requireUpdateSatusUser(userId);
    }

    @Override
    public void onGetUserListSuccessful(String data) {
        try {
            ArrayList <User> arrListUser = handleUserList(data);
            this.mViewHandleUserManagement.onHandleUserListSuccessful(this.mQualityUser, arrListUser );
        } catch (JSONException e) {
            this.mViewHandleUserManagement.onHandleUserListError(e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<User> handleUserList(String data) throws JSONException {
        ArrayList<User> users = new ArrayList<>();

        JSONObject jsonObjectData = new JSONObject(data);
        this.mQualityUser = Integer.parseInt(jsonObjectData.getString("quality_user"));

        JSONArray jsonArray = jsonObjectData.getJSONArray("user_list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            User user = new User();
            user.setIdUser(jsonObject.getInt("id"));
            user.setName(jsonObject.getString("name"));
            user.setEmail(jsonObject.getString("email"));
            user.setPhoneNumber(jsonObject.getString("phone_number"));
            user.setRole(jsonObject.getString("role"));
            user.setQualityPostSale(jsonObject.getInt("quality_post_sale"));
            user.setStatus(jsonObject.getString("status"));

            users.add(user);

        }

        return users;

    }

    @Override
    public void onGetUserListError(String error) {
        this.mViewHandleUserManagement.onHandleUserListError(error);
    }

    @Override
    public void onUpdateStatusUserSuccessful() {
        mViewHandleUserManagement.onHandleUpdateStatusUserSuccessful();
    }

    @Override
    public void onUpdateStatusUserError(String error) {
        mViewHandleUserManagement.onHandleUpdateStatusUserError(error);
    }
}
