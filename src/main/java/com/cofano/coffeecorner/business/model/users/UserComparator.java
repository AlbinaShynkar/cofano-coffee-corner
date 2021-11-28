package com.cofano.coffeecorner.business.model.users;

import java.util.Comparator;

/**
 * This {@link Comparator} defines how users should be ordered in a list. Using this class,
 * users are displayed in ascending order by {@link User#getName() name}.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
public class UserComparator implements Comparator<User> {

	@Override
	public int compare(User u1, User u2) {
		return u1.getName().compareTo(u2.getName());
	}

}
