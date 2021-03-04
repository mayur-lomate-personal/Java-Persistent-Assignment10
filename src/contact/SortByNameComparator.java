package contact;

import java.util.Comparator;

public class SortByNameComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return ((Contact)o1).getContactName().compareTo(((Contact)o2).getContactName());
	}

}
