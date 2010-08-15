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
@Table(name = "book_hibernate")
public class BookHibernate
{
	@Id
	@GeneratedValue
	private Integer id;

	private String title;

	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "aut_id")
	private AuthorHibernate author;

	public BookHibernate()
	{
	}

	public BookHibernate(String title)
	{
		super();
		this.title = title;
	}

	public AuthorHibernate getAuthor()
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

	protected void setAuthor(AuthorHibernate author)
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
		BookHibernate other = (BookHibernate) obj;
		return Objects.equal(this.author, other.getAuthor()) && Objects.equal(this.title, other.getTitle());
	}

}
