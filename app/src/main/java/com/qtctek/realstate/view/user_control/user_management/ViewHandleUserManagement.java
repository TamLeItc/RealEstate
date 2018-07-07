package com.qtctek.realstate.view.user_control.user_management;

import com.qtctek.realstate.dto.User;
import com.qtctek.realstate.dto.User_Object;

import java.util.ArrayList;

public interface ViewHandleUserManagement {

    void onHandleUserListSuccessful(ArrayList<User> arrayListUser);

    void onHandleUserListError(String error);

    void onHandleUpdateStatusUserSuccessful();

    void onHandleUpdateStatusUserError(String error);

}
