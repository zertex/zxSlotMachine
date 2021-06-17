package com.xapple.zxslotmachine.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Egorka on 10.10.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@interface CommandDescription {

    public abstract String value();
}
