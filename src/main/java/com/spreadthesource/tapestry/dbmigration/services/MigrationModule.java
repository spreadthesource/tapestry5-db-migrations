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

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.command.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.command.CreateTable;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.command.JoinTable;
import com.spreadthesource.tapestry.dbmigration.command.Sql;
import com.spreadthesource.tapestry.dbmigration.command.UpdateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.ConstraintImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.DropContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.JoinTableContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.SqlQueryImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.TableImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.UpdateTableImpl;

public class MigrationModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(MigrationManager.class, MigrationManagerImpl.class);
        binder.bind(MigrationRunner.class, MigrationRunnerImpl.class);
        binder.bind(MigrationHelper.class, MigrationHelperImpl.class).scope(ScopeConstants.PERTHREAD);
        binder.bind(DbSource.class, DbSourceImpl.class);
        binder.bind(PrimaryKeyStrategy.class, DefaultPrimaryKeyStrategy.class);
    }

    @Match("MigrationManager")
    public static void adviseClientInfrastructure(MethodAdviceReceiver receiver,
            final MigrationRunner runner) throws SecurityException, NoSuchMethodException
    {

        MethodAdvice advice = new MethodAdvice()
        {
            public void advise(Invocation invocation)
            {
                runner.inStatement(invocation);
            }
        };

        receiver.adviseAllMethods(advice);
    }

    public void contributeMigrationHelper(MappedConfiguration<Class, Class> configuration)
    {
        configuration.add(CreateTable.class, TableImpl.class);
        configuration.add(CreateConstraint.class, ConstraintImpl.class);
        configuration.add(UpdateTable.class, UpdateTableImpl.class);
        configuration.add(Drop.class, DropContextImpl.class);
        configuration.add(JoinTable.class, JoinTableContextImpl.class);
        configuration.add(Sql.class, SqlQueryImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(MigrationSymbolConstants.DO_UPDATE, "true");
        configuration.add(MigrationSymbolConstants.HALT_ON_ERROR, "true");
        configuration.add(MigrationSymbolConstants.VERSIONING_TABLE_NAME, "versions");
        configuration.add(MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION, "true");
    }
}
