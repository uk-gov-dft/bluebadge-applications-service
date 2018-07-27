# DFT BLUE BADGE BETA - APPLICATIONMANAGEMENT-SERVICE

## Getting Started in few minutes
From command line:
```
git clone git@github.com:uk-gov-dft/badgemanagement-service.git
cd applications-service
gradle wrapper
gradle build
gradle bootRun
```

## INSTALLATION
From command line:
```
brew install postgres
```

To Start postgresql:
```
pg_ctl start -D /usr/local/var/postgres
```

Create User:
```
createuser -W developer -P
createdb bb_dev  OWNER developer
psql bb_dev -U developer
```

You may need to create the database schema using the (database-schema module) 
