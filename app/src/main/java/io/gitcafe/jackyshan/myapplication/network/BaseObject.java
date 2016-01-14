package io.gitcafe.jackyshan.myapplication.network;

import io.gitcafe.jackyshan.myapplication.utils.LogUtil;

/**
 * Created by jackyshan on 15/5/8.
 */

public class BaseObject {

    protected void logMsg(String msg) {
        LogUtil.LogMsg(this.getClass(), msg);
    }

    protected void logErr(Exception ex) {
        LogUtil.LogErr(this.getClass(), ex);
    }
}