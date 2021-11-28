package com.cofano.coffeecorner.business.model.page;

import org.apache.commons.text.CaseUtils;

import java.util.HashMap;

/**
 * This class is the prototype for all pages of Cofano Coffee Corner web-application.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 * @version 1
 */
@SuppressWarnings("serial")
public class Page extends HashMap<String, Object> {
	
	public Page() { super(); };

	@Override
	public Object put(String key, Object value) {
		return super.put(CaseUtils.toCamelCase(key, false, '_'), value);
	}
	
}
