package org.paperolle.cascade;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.logging.Logger;

import org.apache.derby.impl.io.VFMemoryStorageFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

public class HibernatePersistenceTest
{
	private static Logger logger = Logger.getLogger(HibernatePersistenceTest.class
			.getName());
	private static SessionFactory sessionFactory;
	protected Session session;

	@Before
	public void setUpEntityManager() throws Exception
	{
		// Instanciate the sessionFactory
		instanciateSessionFactory();
		try
		{
			session = sessionFactory.openSession();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail("Exception during Hibernate EntityManager instanciation.");
		}
	}

	private void instanciateSessionFactory()
	{
		if (sessionFactory == null)
		{
			try
			{
				logger.info("Starting in-memory database for unit tests");

				Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
				DriverManager.getConnection(
						"jdbc:derby:memory:unit-testing-hibernate;create=true").close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				fail("Exception during database startup.");
			}
			try
			{
				logger.info("Building Hibernate EntityManager for unit tests");
				// This step will read hibernate.cfg.xml and prepare hibernate
				// for use
				sessionFactory = new AnnotationConfiguration()
						.configure()
						.buildSessionFactory();

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				fail("Exception during Hibernate EntityManager instanciation.");
			}
		}
	}

	@After
	public void closeEntityManager()
	{
		if (session != null)
		{
			session.close();
		}

	}

	@AfterClass
	public static void clean() throws Exception
	{
		logger.info("Shuting down Hibernate Hibernate layer.");
		if (sessionFactory != null)
		{
			sessionFactory.close();
		}
		logger.info("Stopping in-memory database.");
		try
		{
			DriverManager.getConnection(
					"jdbc:derby:memory:unit-testing-hibernate;shutdown=true").close();
		}
		catch (SQLNonTransientConnectionException ex)
		{
			if (ex.getErrorCode() != 45000) { throw ex; }
			// Shutdown success
		}

		VFMemoryStorageFactory.purgeDatabase(new File("unit-testing-hibernate")
				.getCanonicalPath());
	}
}
