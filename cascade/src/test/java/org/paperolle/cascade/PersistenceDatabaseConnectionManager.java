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
import org.junit.AfterClass;
import org.junit.Before;

public class PersistenceDatabaseConnectionManager
{
	private static Logger logger = Logger.getLogger(PersistenceDatabaseConnectionManager.class
			.getName());
	private EntityManagerFactory emFactory;
	protected EntityManager em;
	private static PersistenceDatabaseConnectionManager instance = new PersistenceDatabaseConnectionManager();

	public static PersistenceDatabaseConnectionManager getInstance()
	{
		return instance;
	}

	private PersistenceDatabaseConnectionManager()
	{

	}

	@Before
	public void setUp() throws Exception
	{
		try
		{
			logger.info("Starting in-memory database for unit tests");
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

	@AfterClass
	public void tearDown() throws Exception
	{
		logger.info("Shuting down Hibernate JPA layer.");
		if (em != null)
		{
			em.close();
		}
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
		VFMemoryStorageFactory.purgeDatabase(new File("unit-testing-jpa")
				.getCanonicalPath());
	}
}
