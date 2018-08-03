package com.qtctek.aladin.view.user_control.user_management;

import com.qtctek.aladin.dto.User;

import java.util.ArrayList;

public interface ViewHandleUserManagement {

    void onHandleUserListSuccessful(ArrayList<User> arrayListUser);

    void onHandleUserListError(String error);

    void onHandleUpdateStatusUserSuccessful();

    void onHandleUpdateStatusUserError(String error);

}
