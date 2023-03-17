## Issue: When saving a subclass object with a composite primary key using IdClass for the second time, an INSERT statement instead of an UPDATE statement is used if the base class has a composite primary key with IdClass.
- The issue appears to be related to the @IdClass annotation. When using @EmbeddedId or @Embeddable for composite primary keys, the error does not occur.
- Upon further investigation, it was discovered that the problem only occurs when the two merge calls happen in separate transactions.

### Purpose and Goal for each @Test case
- Purpose: To test the ability to save a modified instance of a subclass for the second time under different transaction environments, including none, inner transaction, @Transactional annotation, and TransactionTemplate.
- Goal: To investigate whether the failure is specific to cases where the base class employs IdClass for a composite primary key, and whether saving a modified instance of the subclass for the second time under one of the transaction environments triggers this failure.

### HibernateIdClassTests testing an annotated @idClass using an EntityManager

#### Test Case 1: Without a Transaction
- The purpose of this test case is to investigate whether saving a modified instance of the subclass for the second time triggers a failure when the base class employs IdClass for a composite primary key.
- The test creates a new instance of VipCustomerWithIdClass, sets its fields, and calls the merge method of the EntityManager to persist it.
- The mergeOperation method modifies the instance and calls the merge method again to update it, but this time the merge method throws a DataIntegrityViolationException with the message "could not execute statement; SQL [n/a]; constraint [customer_with_id_class_pkey]" due to the use of a composite primary key.
- The failure is specific to cases where the base class employs IdClass for a composite primary key.

#### Test Case 2: With an inner/nested Transaction
- The purpose of this test case is to investigate whether saving a modified instance of the subclass for the second time triggers a failure when the merge method is called without an active transaction in progress.
- The error message "No EntityManager with actual transaction available for current thread - cannot reliably process 'merge' call" is thrown because the merge method should always be executed within the scope of an active transaction.
- To fix this error, the merge method should be executed within the scope of an active transaction, which can be achieved by adding a @Transactional annotation to the doStuff() method or by wrapping the merge operation in a txTemplate.execute() method call.
- The failure is not specific to cases where the base class employs IdClass for a composite primary key.

#### Test Case 3: With a hand rolled Transaction

#### Test Case 4: With a TransactionTemplate

#### Test Case 5: With a Transactional annotation

### SpringDataIdClassTests testing an annotated @idClass using a JpaRepository

#### Test Case 6: With an inner/nested Transaction
#### Test Case 7: With a TransactionTemplate
#### Test Case 8: With a Transactional annotation