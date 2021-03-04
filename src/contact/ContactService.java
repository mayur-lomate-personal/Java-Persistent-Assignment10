package contact;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ContactService {
	
	Connection con;
	
	public ContactService() {
		// TODO Auto-generated constructor stub
		try {
			this.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "contactdb", "0101");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			System.out.println("Connection Failed");
			System.exit(0);
		}
	}
	
	void addContact(Contact contact, List<Contact> contacts) {
		contacts.add(contact);
	}
	
	
	void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException {
		
		for(int i=0; i<contacts.size(); i++) {
			if(contacts.get(i).getContactId() == contact.getContactId()) {
				contacts.remove(i);
				return;
			}
		}
		throw new ContactNotFoundException();
	}
	
	Contact searchContactByName(String name, List<Contact> contacts) throws ContactNotFoundException {
		for(int i=0; i<contacts.size(); i++) {
			if(contacts.get(i).getContactName().toLowerCase().equals(name.toLowerCase())) {
				return contacts.get(i);
			}
		}
		throw new ContactNotFoundException();
	}
	
	List<Contact> SearchContactByNumber(String number, List<Contact> contacts) throws ContactNotFoundException {
		ArrayList<Contact> matched = new ArrayList<Contact>();
		for(Contact contact : contacts) {
			for(String num : contact.getContactNumber()) {
				if(num.contains(number)) {
					matched.add(contact);
					break;
				}
			}
			
		}
		if(matched.size() == 0) {
			throw new ContactNotFoundException();
		}
		return matched;
	}
	
	void addContactNumber(int contactId, String contactNo, List<Contact> contacts) {
		for(Contact contact : contacts) {
			if(contact.getContactId()==contactId) {
				contact.getContactNumber().add(contactNo);
				return;
			}
		}
	}
	
	void sortContactsByName(List<Contact> contacts) {
		Collections.sort(contacts, new SortByNameComparator());
	}
	
	void readContactsFromFile(List<Contact> contacts, String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(Paths.get(fileName).toFile());
		while(sc.hasNext()) {
			String contactInfo = sc.nextLine();
			String contactAttributes[] = contactInfo.split(",");
			ArrayList<String> numbers = new ArrayList<String>();
			for(int i=3; i<contactAttributes.length; i++) {
				numbers.add(contactAttributes[i]);
			}
			contacts.add(new Contact(
					Integer.parseInt(contactAttributes[0]),
					contactAttributes[1],
					contactAttributes[2],
					numbers
					));
		}
	}
	
	void serializeContactDetails(List<Contact> contacts, String fileName) throws IOException {
		FileOutputStream file = new FileOutputStream(fileName); 
        ObjectOutputStream out = new ObjectOutputStream(file); 

       
        	out.writeObject(contacts);
        
          
        out.close(); 
        file.close();
	}
	
	List<Contact> deserializeContact(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream file = new FileInputStream(fileName); 
        ObjectInputStream in = new ObjectInputStream(file); 
        
        ArrayList<Contact> contacts;
          
        // Method for deserialization of object 
        contacts = (ArrayList<Contact>) in.readObject();
          
        in.close(); 
        file.close();
      
        return contacts;
	}
	
	Set<Contact> populateContactFromDb() throws SQLException {
		HashSet<Contact> contacts = new HashSet();
		ResultSet rs = con.createStatement().executeQuery("select * from contact_tbl");
		while(rs.next()) {
			ArrayList<String> number = new ArrayList<String>();
			String numbers = rs.getString(4);
			if(!rs.wasNull()) {
				Collections.addAll(number, numbers.split(","));
			}
			contacts.add(new Contact(
					rs.getInt(1),
					rs.getString(2),
					rs.getString(3),
					number
					));
		}
		return contacts;
	}
	
	Boolean addContacts(List<Contact> existingContact, Set<Contact> newContacts) {
		if(newContacts.size() == 0) {
			return false;
		}
		Set<Contact> temp = new HashSet(newContacts);
		for(Contact contact : existingContact) {
			temp.add(contact);
		}
		if(temp.size() == newContacts.size() || temp.size() == existingContact.size()) {
			System.gc();
			return false;
		}
		existingContact = new ArrayList();
		existingContact.addAll(temp);
		System.gc();
		return true;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		// TODO Auto-generated method stub
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		ContactService obj = new ContactService();
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		obj.readContactsFromFile(contacts, "readFromFile.txt");
		System.out.println(contacts);
		obj.addContact(new Contact(), contacts);
		System.out.println(contacts);
		try {
			obj.removeContact(new Contact(), contacts);
		} catch (ContactNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		System.out.println(contacts);
		try {
			System.out.println(obj.searchContactByName("Rohan", contacts));
		} catch (ContactNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		try {
			System.out.println(obj.SearchContactByNumber("23", contacts));
		} catch (ContactNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		obj.addContactNumber(1, "96858", contacts);
		System.out.println(contacts);
		obj.sortContactsByName(contacts);
		System.out.println(contacts);
		obj.serializeContactDetails(contacts, "serializedContact.dat");
		System.out.println(obj.deserializeContact("serializedContact.dat"));
		System.out.println(obj.addContacts(contacts, obj.populateContactFromDb()));
		System.out.println(contacts);
	}

}
