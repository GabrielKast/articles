package org.paperolle.cascade.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.google.common.base.Objects;

@Entity
@Table(name = "Book")
public class Book
{
	@Id
	@GeneratedValue
	private Integer id;

	private String title;

	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "aut_id")
	private Author author;

	public Book()
	{
	}

	public Book(String title)
	{
		super();
		this.title = title;
	}

	public Author getAuthor()
	{
		return author;
	}

	public Integer getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	protected void setAuthor(Author author)
	{
		this.author = author;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(author, title);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Book other = (Book) obj;
		return Objects.equal(this.author, other.getAuthor()) && Objects.equal(this.title, other.getTitle());
	}

}
