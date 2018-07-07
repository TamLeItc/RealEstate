package com.qtctek.realstate.view.user_control.saved_post;

import java.util.HashMap;

public interface ViewGetDataLocal {

    void onHandleDataProductIdsSuccessful(HashMap<String, String> list);

    void onHandleDataProductIdsError(String error);

}
