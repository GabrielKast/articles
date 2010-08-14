package org.paperolle.cascade;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.paperolle.cascade.model.BookJpaPersistenceTest;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@SuiteClasses(value = {
		BookJpaPersistenceTest.class,
})
public class AppTest
{
}
