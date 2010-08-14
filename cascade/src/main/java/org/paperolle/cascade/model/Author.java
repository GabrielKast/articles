package org.paperolle.cascade.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.google.common.base.Objects;

@Entity
@Table(name = "author")
public class Author
{

	public Author()
	{
	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "first_name", nullable = true, length = 50)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 80)
	private String lastName;

	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Book> books = new HashSet<Book>();

	public Author(String firstName, String lastName)
	{
		super();
		checkNotNull(lastName, "firstName cannot be null");
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Set<Book> getBooks()
	{
		return Collections.unmodifiableSet(books);
	}

	public void setBooks(Set<Book> books)
	{
		this.books = books;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.firstName, this.lastName);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equal(firstName, other.getFirstName())
				&& Objects.equal(lastName, other.getLastName());
	}

	public void addBook(Book book)
	{
		if (!books.contains(book))
		{
			books.add(book);
			book.setAuthor(this);
		}
	}

	public void removeBook(Book book)
	{
		if (books.contains(book))
		{
			books.remove(book);
			book.setAuthor(null);
		}
	}

}
