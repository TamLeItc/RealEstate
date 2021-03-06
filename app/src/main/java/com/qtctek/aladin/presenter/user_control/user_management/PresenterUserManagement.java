package com.qtctek.aladin.presenter.user_control.user_management;

import com.qtctek.aladin.dto.User;
import com.qtctek.aladin.model.user_control.ModelUserManagement;
import com.qtctek.aladin.view.user_control.user_management.ViewHandleUserManagement;

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

    public void handleGetUserList(int start, int quality, String status){
        this.mModelUserManagement = new ModelUserManagement(this);
        this.mModelUserManagement.requireUserList(start, quality, status);
    }

    public void handleUpdateStatusUser(int userId){
        this.mModelUserManagement.requireUpdateSatusUser(userId);
    }

    @Override
    public void onGetUserListSuccessful(String data) {
        try {
            ArrayList <User> arrListUser = handleUserList(data);
            this.mViewHandleUserManagement.onHandleUserListSuccessful(arrListUser);
        } catch (JSONException e) {
            this.mViewHandleUserManagement.onHandleUserListError(e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<User> handleUserList(String data) throws JSONException {

        ArrayList<User> arrListUser = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            User user = new User();
            user.setId(jsonObject.getInt("id"));
            user.setFullName(jsonObject.getString("name"));
            user.setEmail(jsonObject.getString("email"));
            user.setPhone(jsonObject.getString("phone"));
            user.setType(jsonObject.getString("type"));
            user.setLevel(jsonObject.getInt("level"));
            user.setStatus(jsonObject.getString("status"));

            arrListUser.add(user);
        }

        return arrListUser;

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
