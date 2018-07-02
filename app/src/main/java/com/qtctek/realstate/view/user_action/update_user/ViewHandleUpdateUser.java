package com.qtctek.realstate.view.user_action.update_user;

import com.qtctek.realstate.dto.User;

public interface ViewHandleUpdateUser {

    void onUpdateUserSuccessful();

    void onUpdateUserError(String error);

    void onHandleInformationUserSuccessful(User user);

    void onHandleInformationUserError(String error);

}
