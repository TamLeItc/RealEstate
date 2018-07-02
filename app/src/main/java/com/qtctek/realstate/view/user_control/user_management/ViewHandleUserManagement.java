package com.qtctek.realstate.view.user_control.user_management;

import com.qtctek.realstate.dto.User;

import java.util.ArrayList;

public interface ViewHandleUserManagement {

    void onHandleUserListSuccessful(int qualityUser, ArrayList<User> arrayListUser);

    void onHandleUserListError(String error);

    void onHandleUpdateStatusUserSuccessful();

    void onHandleUpdateStatusUserError(String error);

}
