Welcome!

Usage
-----

### Prerequisites
Gradle in PATH environment variable.

Database already created.  Example create database for local instance;

https://medium.com/coding-blocks/creating-user-database-and-adding-access-on-postgresql-8bfcd2f4a91e

### General

To specify an environment use --Penv=xyz where xyz is a properties file
in the environments folder without the .properties suffix.

### New script

To create  new migration script, at the command line (project root dir) type:

<code>gradle migrateNew -Pdescription="BBB-nnn-Short_Summary"</code>

* Replace the nnn with your ticket number.

This will create a new sql file in the scripts directory that you edit to add your sql migration script.

**You must add the rollback script under the <code>--//@UNDO</code> tag in the script file.**

**The sql comment prior to //@UNDO must NOT have a space in it.**

### Show status of migrations

To show the status of migrations run the following on the command line:

<code>gradle migrateStatus -Penv=local --info</code>

This will show migration scripts that have not been applied. Need info logging to get anything meaningful output.


### Apply pending migration scripts

To apply all pending scripts on a database, run the following at the command line:

<code>gradle migrateUp -Penv=local</code>

This will attempt to apply the migration script and will report on any errors.


### Rollback migration scripts

To rollback migration scripts run the following at the command line:

<code>gradle migrateDown -Penv=local \[-Psteps=n\]</code>

This will attempt to apply the migration script and will report on any errors.

The optional parameter <code>downSteps</code> defaults to 1 when not specified.


Additional Info
---------------

See https://github.com/marceloemanoel/gradle-migrations-plugin
See http://www.mybatis.org/migrations/

The repository base directory contains three subdirectories as follows:

./drivers

Place your JDBC driver .jar or .zip files in this directory.  Upon running a
migration, the drivers will be dynamically loaded.

./environments

In the environments folder you will find .properties files that represent
your database instances.  By default a development.properties file is
created for you to configure your development time database properties.
You can also create test.properties and production.properties files.
The environment can be specified when running a migration by using
the -Penv=<environment> option (without the path or ".properties" part).

The default environment is "development".

./scripts

This directory contains your migration SQL files.  These are the files
that contain your DDL to both upgrade and downgrade your database
structure.  By default, the directory will contain the script to
create the changelog table, plus one empty "first" migration script.
To create a new migration script, use the "new" command.  To run
all pending migrations, use the "up" command.  To undo the last
migration applied, use the "down" command etc.

For more information about commands and options, run the MyBatis
Migration script with the --help option.

Enjoy.
