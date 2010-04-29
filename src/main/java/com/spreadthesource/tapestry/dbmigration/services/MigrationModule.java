//
// Copyright 2010 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.spreadthesource.tapestry.dbmigration.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;

public class MigrationModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(MigrationManager.class, MigrationManagerImpl.class);
        binder.bind(MigrationRunner.class, MigrationRunnerImpl.class);
        binder.bind(MigrationHelper.class, MigrationHelperImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(MigrationSymbolConstants.DO_UPDATE, "true");
        configuration.add(MigrationSymbolConstants.HALT_ON_ERROR, "true");
        configuration.add(MigrationSymbolConstants.VERSIONING_TABLE_NAME, "versions");
    }

    /*
     * public void contributeActivityFeedWriter( MappedConfiguration<Class, ActivityFeedWriter>
     * configuration) { configuration.add(AccountActivity.class, accountActivityFeed); }
     */
}
