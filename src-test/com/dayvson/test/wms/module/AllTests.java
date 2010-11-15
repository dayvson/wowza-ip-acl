package com.dayvson.test.wms.module;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * 
 * @author dayvson
 * @email dayvson@gmail.com
 * 
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(WowzaIpAclTest.class);
		//$JUnit-END$
		return suite;
	}

}
