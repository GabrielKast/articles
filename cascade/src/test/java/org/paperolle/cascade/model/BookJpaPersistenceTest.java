package org.paperolle.cascade.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.paperolle.cascade.JpaPersistenceTest;

public class BookJpaPersistenceTest extends JpaPersistenceTest
{

	@Test
	public void shouldPersistAuthor()
	{
		Author marcel = new Author("Marcel", "Proust");

		try
		{
			em.getTransaction().begin();

			em.persist(marcel);
			assertTrue(em.contains(marcel));

			em.remove(marcel);
			assertFalse(em.contains(marcel));

			em.getTransaction().commit();

		}
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			ex.printStackTrace();
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
			em.getTransaction().begin();

			em.persist(marcel);
			assertTrue(em.contains(marcel));
			Long marcelId = marcel.getId();
			assertNotNull(marcelId);

			Author persistentMarcel = em.find(Author.class, marcelId);
			assertTrue(persistentMarcel.getBooks().size() == 1);

			Book b2 = new Book("Les plaisirs et les jours");
			marcel.addBook(b2);
			assertTrue(persistentMarcel.getBooks().size() == 2);

			Author persistentMarcel2 = em.find(Author.class, marcelId);
			assertTrue(persistentMarcel2.getBooks().size() == 2);

			em.getTransaction().commit();

		}
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			ex.printStackTrace();
			fail("Exception during testPersistAuthor");
		}
	}
}
