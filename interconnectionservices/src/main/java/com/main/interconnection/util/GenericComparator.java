package com.main.interconnection.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.main.interconnection.clientBo.User;

/**
 * @desc Sorting - Generic Comparator
 * 
 * @filename GenericComparator.java
 *
 */
public final class GenericComparator implements Comparator, Serializable {
	private static final long serialVersionUID = -2293914106471884607L;
	// Logger
	private static final Log LOG = LogFactory.getLog(GenericComparator.class);

	private static final int LESSER = -1;
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	private static final String METHOD_GET_PREFIX = "get";
	private static final String DATATYPE_STRING = "java.lang.String";
	private static final String DATATYPE_DATE = "java.util.Date";
	private static final String DATATYPE_INTEGER = "java.lang.Integer";
	private static final String DATATYPE_LONG = "java.lang.Long";
	private static final String DATATYPE_FLOAT = "java.lang.Float";
	private static final String DATATYPE_DOUBLE = "java.lang.Double";

	private enum CompareMode {
		EQUAL, LESS_THAN, GREATER_THAN, DEFAULT
	}

	// generic comparator attributes
	private String targetMethod;
	private boolean sortAscending;

	public GenericComparator(boolean sortAscending) {
		super();
		this.targetMethod = null;
		this.sortAscending = sortAscending;
	}

	public GenericComparator(String sortField) {
		super();
		this.targetMethod = prepareTargetMethod(sortField);
		this.sortAscending = true;
	}

	public GenericComparator(String sortField, boolean sortAscending) {
		super();
		this.targetMethod = prepareTargetMethod(sortField);
		this.sortAscending = sortAscending;
	}

	@Override
	public int compare(Object o1, Object o2) {
		int response = LESSER;
		try {
			Object v1 = (null == this.targetMethod) ? o1 : getValue(o1);
			Object v2 = (null == this.targetMethod) ? o2 : getValue(o2);
			CompareMode cm = findCompareMode(v1, v2);

			if (!cm.equals(CompareMode.DEFAULT)) {
				return compareAlternate(cm);
			}

			final String returnType = (null == this.targetMethod) ? o1
					.getClass().getName() : getMethod(o1).getReturnType()
					.getName();
			response = compareActual(v1, v2, returnType);
		} catch (NoSuchMethodException nsme) {
			LOG.error("NoSuchMethodException occurred while comparing", nsme);
		} catch (IllegalAccessException iae) {
			LOG.error("IllegalAccessException occurred while comparing", iae);
		} catch (InvocationTargetException ite) {
			LOG.error("InvocationTargetException occurred while comparing", ite);
		}
		return response;
	}

	// ---------------------------------------------------------------------------------//
	// Private methods used by {@link com.myjeeva.comparator.GenericComparator}
	// //
	// ---------------------------------------------------------------------------------//

	private int compareAlternate(CompareMode cm) {
		int compareState = LESSER;
		switch (cm) {
		case LESS_THAN:
			compareState = LESSER * determinePosition();
			break;
		case GREATER_THAN:
			compareState = GREATER * determinePosition();
			break;
		case EQUAL:
			compareState = EQUAL * determinePosition();
			break;
		}
		return compareState;
	}

	private int compareActual(Object v1, Object v2, String returnType) {
		int acutal = LESSER;
		if (returnType.equals(DATATYPE_INTEGER)) {
			acutal = (((Integer) v1).compareTo((Integer) v2) * determinePosition());
		} else if (returnType.equals(DATATYPE_LONG)) {
			acutal = (((Long) v1).compareTo((Long) v2) * determinePosition());
		} else if (returnType.equals(DATATYPE_STRING)) {
			acutal = (((String) v1).compareTo((String) v2) * determinePosition());
		} else if (returnType.equals(DATATYPE_DATE)) {
			acutal = (((Date) v1).compareTo((Date) v2) * determinePosition());
		} else if (returnType.equals(DATATYPE_FLOAT)) {
			acutal = (((Float) v1).compareTo((Float) v2) * determinePosition());
		} else if (returnType.equals(DATATYPE_DOUBLE)) {
			acutal = (((Double) v1).compareTo((Double) v2) * determinePosition());
		}
		return acutal;
	}

	private final static String prepareTargetMethod(String name) {
		StringBuffer fieldName = new StringBuffer(METHOD_GET_PREFIX);
		fieldName.append(name.substring(0, 1).toUpperCase());
		fieldName.append(name.substring(1));
		return fieldName.toString();
	}

	private final Method getMethod(Object obj) throws NoSuchMethodException {
		return obj.getClass().getMethod(targetMethod, null);
	}

	private final static Object invoke(Method method, Object obj)
			throws InvocationTargetException, IllegalAccessException {
		return method.invoke(obj, null);
	}

	private Object getValue(Object obj) throws InvocationTargetException,
			IllegalAccessException, NoSuchMethodException {
		return invoke(getMethod(obj), obj);
	}

	private CompareMode findCompareMode(Object o1, Object o2) {
		CompareMode cm = CompareMode.LESS_THAN;

		if (null != o1 & null != o2) {
			cm = CompareMode.DEFAULT;
		} else if (null == o1 & null != o2) {
			cm = CompareMode.LESS_THAN;
		} else if (null != o1 & null == o2) {
			cm = CompareMode.GREATER_THAN;
		} else if (null == o1 & null == o2) {
			cm = CompareMode.EQUAL;
		}

		return cm;
	}

	private int determinePosition() {
		return sortAscending ? GREATER : LESSER;
	}

	// Call from controller to get the list in sorted order based on passed
	// field on which sorting need to be..
	public static <T> List<? extends Object> getSortedListBySortedField(
			String sortedField, List<? extends Object> list, boolean isAscending) {
		Collections.sort(list, new GenericComparator(sortedField, isAscending));
		return list;
	}

	// main method for test bean classes
	public static void main(String[] args) {
		List<User> listUser = new ArrayList<User>();
		User request = new User();
		request.setName("Ravish");
		listUser.add(request);
		request = new User();
		request.setName("Karan");
		listUser.add(request);
		request = new User();
		request.setName("Shweta");
		listUser.add(request);
		request = new User();
		request.setName("Amit");
		listUser.add(request);
		Collections.sort(listUser, new GenericComparator("name", true));
		for (User user : listUser) {
			System.out.println("The sorted list elements ===" + user.getName());
		}
	}
}
