package edu.odu.cs.ujv.GBParser;

import static org.testng.AssertJUnit.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;



public class TestFeatureSet {
	
	/**
	@BeforeTest
	public void setup() {
		FeatureSet Fset = new FeatureSet();
	}
	*/
	
	// was unable to get working in either one but can replace later.
	/*
	@Test
	public void TestFeatureName(){
		String Test = "Test";
		FeatureSet Fset = new FeatureSet();
		Fset.setFeatureName(Test);
		assertEquals(Fset.getFeatureName(),Test);
	}
	
	@Test
	public void TestStart(){
		int Test = 5;
		FeatureSet Fset = new FeatureSet();
		Fset.setStart(Test);
		assertEquals(Fset.getStart(),Test);
	}
	
	@Test
	public void TestStop(){
		int Test = 5;
		FeatureSet Fset = new FeatureSet();
		Fset.setStop(Test);
		assertEquals(Fset.getStop(),Test);
	}
	
	@Test
	public void TestNucleotideSequence(){
		String Test = "ATCGGCAATCAGTA";
		FeatureSet Fset = new FeatureSet();
		Fset.setSequence(Test);
		assertEquals(Fset.getSequence(),Test);
	}
	
	@Test 
	public void TestAttributesKeyVal(){
		FeatureSet Fset = new FeatureSet();
		String Key = "Locust";
		String Val = "5115";
		Fset.setAttributes(Key,Val);
		assertEquals(Fset.getAttributes(Key),Val);
	}
	
	@Test
	public void TestSetMap(){
		FeatureSet Fset = new FeatureSet();
		Map<String,String> map = new HashMap<>();
		map.put("key", "value");
		map.put("eye", "phone");
		Fset.setMap(map);
		Map<String,String> Testmap = Fset.getAttributes();
		assertEquals(Testmap.get("key"),map.get("key"));
		assertEquals(Testmap.get("eye"),map.get("eye"));
	}
	
	@Test
	public void TestSetComplement(){
		FeatureSet Fset = new FeatureSet();
		boolean bool = true;
		Fset.setComplement(bool);
		assertEquals(bool,Fset.getcomplement());
	}
	*/
}
