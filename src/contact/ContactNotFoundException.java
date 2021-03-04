package contact;

public class ContactNotFoundException extends Exception {
	public ContactNotFoundException() {
		super("Contact Not Found");
	}
}
