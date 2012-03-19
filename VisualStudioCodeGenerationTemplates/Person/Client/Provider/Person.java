package dev.ronlemire.personprovider;

import java.io.Serializable;

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Id;
	private String FirstName;
	private String LastName;
	private String Email;

	public Person (
			String Id,
			String FirstName,
			String LastName,
			String Email
			) {
		this.Id = Id;
		this.FirstName = FirstName;
		this.LastName = LastName;
		this.Email = Email;
	}

	public String getId() { return this.Id; }
	public String getFirstName() { return this.FirstName; }
	public String getLastName() { return this.LastName; }
	public String getEmail() { return this.Email; }
}
