### API Acceptance tests

#### Start the applicationmanagement service

First you need to start the applicationmanagement service by executing following commands

```
cd applications-service
git pull
git checkout whateverbranch
gradle bootRun
```

#### How to run api acceptance tests

You may need to install maven plugin in intellij.

Then go to api-acceptance-test project and right click on pom.xml → Add as Maven project

```
mvn clean verify -f api-acceptance-tests/pom.xml -Pacceptance-test
```

### Relevant Articles: 
- [Test a REST API with Java](http://www.baeldung.com/2011/10/13/integration-testing-a-rest-api/)
- [Introduction to WireMock](http://www.baeldung.com/introduction-to-wiremock)
- [REST API Testing with Cucumber](http://www.baeldung.com/cucumber-rest-api-testing)
- [Testing a REST API with JBehave](http://www.baeldung.com/jbehave-rest-testing)
- [REST API Testing with Karate](http://www.baeldung.com/karate-rest-api-testing)

