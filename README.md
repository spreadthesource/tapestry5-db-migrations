# Tapestry 5 Database Migration Utility

## About Database Migrations

Database migrations let you create and alter your model in an organized and structured way. 

This module is largely inspired by Ruby on Rails [migrations](http://guides.rubyonrails.org/migrations.html).

## A sample migration

This is an insight of what a couple of migrations can looks like.

	@Version(20100510)
	public class MyModel implements Migration
	{
	    public void up()
	    {
	        helper.createTable("users");
	        helper.addPrimaryKey("users", "id", Types.INTEGER);
	        helper.addColumn("users", "name", Types.VARCHAR);
	        helper.addColumn("users", "password", Types.VARCHAR);
	    }
	
	    public void down()
	    {
	        helper.dropTable("users");
	    }
	}
	
	@Version(20100511)
	public class UsersDescription implements Migration
	{
	    public void up()
	    {
	        helper.addColumn("users", "description", Types.CLOB);
	    }
	
	    public void down()
	    {
	        helper.removeColumn("users", "description");
	    }
	}
	


## How to

TODO : 

## Maven dependency

To use this plugin, add the following dependency in your `pom.xml`.

	<dependencies>
		...
		<dependency>
			<groupId>com.spreadthesource</groupId>
			<artifactId>tapestry5-db-migration</artifactId>
			<version>1.0-SNAPSHOT</version>
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

