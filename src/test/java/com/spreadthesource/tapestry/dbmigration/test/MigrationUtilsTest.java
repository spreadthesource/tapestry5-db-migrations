package com.spreadthesource.tapestry.dbmigration.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

@Test
public class MigrationUtilsTest
{

    @Test
    public void testPkId() {
        
        Assert.assertEquals(MigrationUtils.buildPkColumnId("BookActivity"), "book_activity_id");
        
    }
    
}
