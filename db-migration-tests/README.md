## Spring-Boot-3 slice-testing our Persistence Layer

### db-migration-tests has the following goals:
- Create a database schema that describes how to construct our database, then:
  - Validate Entity Mappings.
  - Verify Constraints.
- Reset the database to a known state after each test.
- Insert sufficient and appropriate data.
- Test against a real database:
  - Test Custom Queries: Inferred, JPQL, and Native SQL Queries.
  - Test db migrations.


	  
	  
