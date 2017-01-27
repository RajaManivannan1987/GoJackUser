package com.example.im028.gojackuser.Singleton;

import com.example.im028.gojackuser.Utility.InterfaceClasses.CompletedInterface;

/**
 * Created by Im033 on 1/25/2017.
 */

public class ActionCompletedSingleton {
    private CompletedInterface actionCompleted;
    private static ActionCompletedSingleton completedSingleton = new ActionCompletedSingleton();

    public static ActionCompletedSingleton actionCompletedSingleton() {
        return completedSingleton;
    }

    public void setListener(CompletedInterface completed) {
        actionCompleted = completed;
    }

    public void ActionCompleted() {
        if (actionCompleted != null) {
            actionCompleted.completed();
        }
    }
}
