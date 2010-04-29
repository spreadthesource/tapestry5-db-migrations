package com.spreadthesource.tapestry.dbmigration.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Version
{
    /**
     * Version number. To prevent any further problem, you should use this pattern: YYYYMMDDHHMM.
     */
    int value();
}
