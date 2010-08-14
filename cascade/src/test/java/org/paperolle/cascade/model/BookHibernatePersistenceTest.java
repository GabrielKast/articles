package org.paperolle.cascade.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hibernate.Transaction;
import org.junit.Test;
import org.paperolle.cascade.HibernatePersistenceTest;

public class BookHibernatePersistenceTest extends HibernatePersistenceTest
{

	@Test
	public void shouldPersistAuthor()
	{
		Author marcel = new Author("Marcel", "Proust");

		try
		{
			Transaction transaction = session.beginTransaction();

			session.saveOrUpdate(marcel);
			assertNotNull(marcel.getId());
			assertTrue(session.contains(marcel));

			session.delete(marcel);
			assertFalse(session.contains(marcel));

			transaction.commit();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			session.getTransaction().rollback();
			fail("Exception during testPersistAuthor");
		}
	}

	@Test
	public void shouldPersistAuthorAndBook()
	{
		Author marcel = new Author("Marcel", "Proust");
		Book book = new Book("A la recherche du temps perdu");
		marcel.addBook(book);

		try
		{
			Transaction transaction = session.beginTransaction();

			session.persist(marcel);
			assertTrue(session.contains(marcel));
			Long marcelId = marcel.getId();
			assertNotNull(marcelId);

			Author persistentMarcel = (Author) session.get(Author.class, marcelId);
			assertTrue(persistentMarcel.getBooks().size() == 1);

			Book b2 = new Book("Les plaisirs et les jours");
			marcel.addBook(b2);
			assertTrue(persistentMarcel.getBooks().size() == 2);

			Author persistentMarcel2 = (Author) session.get(Author.class, marcelId);
			assertTrue(persistentMarcel2.getBooks().size() == 2);

			transaction.commit();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			session.getTransaction().rollback();
			fail("Exception during testPersistAuthor");
		}
	}
}
