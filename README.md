# Tapestry 5 Database Migration Utility

## About Database Migrations

Database migrations let you create and alter your model in an organized and structured way. 

This module is largely inspired by Ruby on Rails [migrations](http://guides.rubyonrails.org/migrations.html).

## A sample migration

This is an insight of what a migration class can look like.

	@Version(20100510)
	public class MyModel implements Migration
	{
	
	    @Inject
    	private MigrationHelper helper;
	
	    public void up()
	    {
	        // Table Authority
	        helper.add(new CreateTable()
	        {
	            public void run(Table table)
	            {
	                table.setName("Authority");
	                table.addString("label").setUnique(true);
	                table.addText("description");
	                table.addText("id");
	            }
	        });
	        
	        // Table Authority
	        helper.add(new CreateTable()
	        {
	            public void run(Table table)
	            {
	                table.setName("User");
	                table.addString("username").setUnique(true).setNotNull(true);
	                table.addString("password").setNotNull(true);
	            }
	        });
		        
	    }
	
	    public void down()
	    {
	        helper.add(new Drop()
	        {
	            public void run(DropContext ctx)
	            {
	                ctx.dropTable("User");
	                ctx.dropTable("Authority");
	            }
	        });
	    }
	}

## Configure

### Database connection

By default tapestry5-db-migration will use the hibernate.cfg.xml file present in your application classpath.
You can disable this by setting MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION to 'false' and define
you own Hibernate configurers by contributing to the DbSource service.

	public void contributeDbSource(OrderedConfiguration<HibernateConfigurer> configurers)
    {
        configurers.add("CustomConfiguration", new HibernateConfigurer()
        {
        	//...
        }
	}

### Migration packages

To configure you only have to define the list of packages that contains your migration classes, this will
be done in your AppModule class. The service in charge of the migrations is MigrationManager :

	public void contributeMigrationManager(Configuration<String> configuration)
    {
        configuration.add("com.spreadthesource.tapestry.dbmigration.test.migrations");
    }

Now you have configured db-migration, we can move to the most exciting part : how to describe your schema ?

## How to

### Create a Migration class

A migration class is a simple POJO that implements the Migration interface.
You have the ability to inject anything you need to operate the migration.
To manipulate easily db-migration you only need to inject the MigrationHelper service.

	@Version(20100510)
	public class MyModel implements Migration
	{
	
	    @Inject
    	private MigrationHelper helper;
    	
    	// ....
    	
    }

### Create a table

Simply add a CreateTable command via the add() method of the helper service

	// Table Authority
	helper.add(new CreateTable()
	{
    	public void run(Table table)
        {
        	table.setName("Authority");
            table.addString("label").setUnique(true);
            table.addText("description");
		}
	});

Not that when you add a column you can chain method to define its properties, see 'label' for exemple.
As you can see here, no primary key is defined, doing this a default primary key strategy will be used
to create the primary column. This strategy can be overriden to respect the need of your application.
(cf. PrimaryKeyStrategy service)

Also, if you have to define a specific primary key for a given tableyou can call 'setPrimary(true)' on 
the column definition, this way the strategy will be ignored.

	helper.add(new CreateTable("acl_object_identity")
	{
		@Override
		public void run(Table table)
		{
			table.addLong("id").setPrimary(true).setUnique(true).setIdentityGenerator("identity");
			// ...
		}
	}

### Create a constraint

Primary constraints are directly handled during table creation. Unique and foreign key constraints are handled
via the CreateConstraint command

Above you can see an piece of spring-security acl schema described with tapestry-db-migrations that create a multiple
column unicity and different foreign keys between tables. Note that you have to set the name of the Table on which constraints
are applied, here 'acl_entry'.

	helper.add(new CreateConstraint()
	{
		public void run(Constraint ctx)
		{
			ctx.setName("acl_entry");
            ctx.setUnique("unique_uk_4", "acl_object_identity", "ace_order");

			ctx.setForeignKey("foreign_fk_4", "acl_object_identity", new String[]
            	{ "acl_object_identity" }, new String[]
				{ "id" });

			ctx.setForeignKey("foreign_fk_5", "acl_sid", new String[]
                { "sid" }, new String[]
                { "id" });
		}
	});
				  

### Update a table

To update a table is almost the same than create, the only difference is that generated sql queries
will alter the existing table. Above we simple add a civility column to the User table.

	helper.add(new UpdateTable()
	{
		public void run(Table ctx)
		{
			ctx.setName("User");
			ctx.addString("civility");
		}
	});

### Create join table

We have seen how to create foreign keys for 1-n and n-1 relations, to handle n-n you will have 
to create a join table. tapestr5-db-migration provides easy ways to create this kind of table
using the default strategy but you will still be able to create more complexe using SQL queries.

Above you can see an exemple of different call where we let tapestry5-migration decide the name
of the join table or setting our custom name.

	helper.add(new JoinTable()
	{
		public void run(JoinTableContext ctx)
		{
			ctx.join("Book", "User");
			ctx.join("BookAuthor", "Book", "User");
		}
	});
	
### Drop constraints and table

MigrationManager allows you to easily reset your database by defining 'down()' method in every
migration classes. You can use the Drop command to achieve this. It will particularly useful
for unit testing.

To drop a fk constraint simply call the dropForeignKey() and specify the table name and constraint name.

	helper.add(new Drop()
	{
		public void run(DropContext ctx)
		{
			// drop constraints
			ctx.dropForeignKey("acl_object_identity", "foreign_fk_1");
			// ...
				
			// Drop tables
			ctx.dropTable("acl_object_identity");
			// ...
		}
	});

### Execute custom SQL queries

A special command allows you to execute custom SQL queries for all database or a dedicated one (using
the dialect associated to define the targeted Db)

	helper.add(new Sql()
	{
		public void run(SqlQuery ctx)
		{
			ctx.addSql("create Table CustomSqlQuery(id int)");
			// For hsqldb only
			ctx.addSql("alter table CustomSqlQuery...", HSQLDialect.class);
		}
	});

## Extend the list of Command

New command can be added to the default one if you want to create you owns for widely used operations.

You will have to create a new Interface for you command, for exemple here command that will do specific
operation on a constraint.

	public interface MyCustomCommand extends MigrationCommand<Constraint> {
		// Nothing needed here
	}

Then add it to the list of existing one
	
	public void contributeMigrationHelper(MappedConfiguration<Class, Class> configuration)
    {
        configuration.add(MyCustomCommand.class, ConstraintImpl.class);
	}

Note that ConstraintImpl is default implementation for operation on constraints.

If you want to redefine your own implementation, you simply have to extend AbstractMigrationContext and implement
Constraint interface. Of course you will have to define all the missing method.

	public class MyConstraintImpl extends AbstractMigrationContext implements
    	    Constraint {
	
		// Implements methods here

	}

## Maven dependency

To use this plugin, add the following dependency in your `pom.xml`.

	<dependencies>
		...
		<dependency>
			<groupId>com.spreadthesource</groupId>
			<artifactId>tapestry5-db-migration</artifactId>
			<version>0.1.1-SNAPSHOT</version>
		</dependency>
		...
	</dependencies>
	
	<repositories>
		...
		<repository>
			<id>devlab722-repo</id>
			<url>http://nexus.devlab722.net/nexus/content/repositories/releases
			</url>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>

		<repository>
			<id>devlab722-snapshot-repo</id>
			<url>http://nexus.devlab722.net/nexus/content/repositories/snapshots
			</url>
			<releases>
				<enabled>false</enabled>
			</releases>
		</repository>
		...
	</repositories>

## More Informations & contacts

* Blog: http://spreadthesource.com
* Twitter: http://twitter.com/spreadthesource

## License

This project is distributed under Apache 2 License. See LICENSE.txt for more information.

