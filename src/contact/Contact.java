package contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Contact implements Serializable {
	private int contactId;
	private String contactName;
	private String email;
	private List<String> contactNumber;
	public Contact(int contactId, String contactName, String email, List<String> contactNumber) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
		this.email = email;
		this.contactNumber = contactNumber;
	}
	public Contact() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the Contact ID : ");
		this.contactId = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter the Contact Name : ");
		this.contactName = sc.nextLine();
		System.out.print("Enter the Email Address : ");
		this.email = sc.nextLine();
		System.out.print("Enter the amount of Contact Numbers : ");
		int n = sc.nextInt();
		ArrayList<String> contactNumber = new ArrayList<String>();
		for(int i=1; i<=n; i++) {
			contactNumber.add(sc.nextLine());
		}
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public List<String> getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}
	@Override
	public String toString() {
		return "Contact [contactId=" + contactId + ", contactName=" + contactName + ", email=" + email
				+ ", contactNumber=" + contactNumber + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contactId;
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (contactId != other.contactId)
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		return true;
	}
	
}
