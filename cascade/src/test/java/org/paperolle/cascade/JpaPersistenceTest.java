package org.paperolle.cascade;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.derby.impl.io.VFMemoryStorageFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

public class JpaPersistenceTest
{
	private static Logger logger = Logger.getLogger(JpaPersistenceTest.class
			.getName());
	private static EntityManagerFactory emFactory;
	protected EntityManager em;

	@Before
	public void setUpEntityManager() throws Exception
	{
		// Instanciate the EntityManagerFactory
		instanciateEmFactory();
		try
		{
			em = emFactory.createEntityManager();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail("Exception during JPA EntityManager instanciation.");
		}
	}

	private void instanciateEmFactory()
	{
		if (emFactory == null)
		{
			try
			{
				logger.info("Starting in-memory database for unit tests");

				VFMemoryStorageFactory.purgeDatabase(new File("unit-testing-jpa")
						.getCanonicalPath());

				Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
				DriverManager.getConnection(
						"jdbc:derby:memory:unit-testing-jpa;create=true").close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				fail("Exception during database startup.");
			}
			try
			{
				logger.info("Building JPA EntityManager for unit tests");
				emFactory = Persistence.createEntityManagerFactory("testPU");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				fail("Exception during JPA EntityManager instanciation.");
			}
		}
	}

	@After
	public void closeEntityManager()
	{
		if (em != null)
		{
			em.close();
		}

	}

	@AfterClass
	public static void clean() throws Exception
	{
		logger.info("Shuting down Hibernate JPA layer.");
		if (emFactory != null)
		{
			emFactory.close();
		}
		logger.info("Stopping in-memory database.");
		try
		{
			DriverManager.getConnection(
					"jdbc:derby:memory:unit-testing-jpa;shutdown=true").close();
		}
		catch (SQLNonTransientConnectionException ex)
		{
			if (ex.getErrorCode() != 45000) { throw ex; }
			// Shutdown success
		}
	}
}
