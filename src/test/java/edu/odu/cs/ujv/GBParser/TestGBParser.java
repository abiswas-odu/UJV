package edu.odu.cs.ujv.GBParser;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
/*
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.LocationTools;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import edu.odu.cs.ujv.GBParser.GenBankParser;
*/

public class TestGBParser {
	private File genomeFile;

	@BeforeTest
	public void setup() {
		if(this.genomeFile == null)
			this.genomeFile = new File("src/main/resources/NC_010612.gbk");
		//System.out.println(genomeFile.exists());
	}


	@Test
	public void testGetFeatureSet(){

	}

	/*
	
	@Test
	public void TestgetFeatures(){
		BufferedReader br = null;
		List<String> TheList = new ArrayList<>();
		
		 try {
	      br = new BufferedReader(new FileReader("src\\main\\resources\\NC_010612.gbk"));
	    }
	    catch (FileNotFoundException ex) {
	      ex.printStackTrace();
	      System.exit(-1);
	    }
		TheList = GenBankParser.getFeatures(br);
		assertTrue(TheList.contains("gene"));
		assertTrue(TheList.contains("source"));
		assertTrue(TheList.contains("CDS"));
		assertTrue(TheList.contains("tRNA"));
		assertTrue(TheList.contains("mobile_element"));
		assertTrue(TheList.contains("STS"));
		assertTrue(TheList.contains("tmRNA"));
		assertTrue(TheList.contains("ncRNA"));
		assertTrue(TheList.contains("misc_feature"));
		assertTrue(TheList.contains("rRNA"));
		assertEquals(TheList.size(),10);
	}

	@Test
	public void TestgetFullSequence() {
		String seq = "ttgaccaatgaccccggttccggtttcgccgcagtgtggaatgccgtcgt";
		String nucleo = null;
	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("src\\main\\resources\\NC_010612.gbk"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//nucleo = GenBankParser.getFullSequence(br);
		assertEquals(nucleo.substring(0, 50),seq);
	}
	
	@Test
	public void TestsetStartStop(){
	//Location loc = LocationTools.makeLocation(1, 50);
	FeatureSet Fset = new FeatureSet();
	//GenBankParser.setStartStop(loc, Fset);
	assertEquals(Fset.getStart(),1);
	assertEquals(Fset.getStop(),50);
		
	}
	
	@Test
	public void TestsetNucleo(){
		FeatureSet Fset = new FeatureSet();
		String seq = "actgggcaat";
		//GenBankParser.setNucleo(seq, Fset);
		assertEquals(seq,Fset.getSequence());
		}
	
	@Test
	public void TestsetAnnotations(){
		FeatureSet Fset = new FeatureSet();
		Map<String,String> map = new HashMap<>();
		map.put("key", "value");
		map.put("eye", "phone");
		//GenBankParser.setAnnotations(map, Fset);
		Map<String,String> Testmap = new HashMap<>();
		Testmap = Fset.getAttributes();
		assertEquals("value",Testmap.get("key"));
		assertEquals("phone",Testmap.get("eye"));
	}
	
	@Test
	public void TestsetName(){
		FeatureSet Fset = new FeatureSet();
		String TestString = "Test";
		//GenBankParser.setName(TestString,Fset);
		assertEquals("Test",Fset.getFeatureName());
		
	}
	
	@Test
	public void TestsetComplement(){
		FeatureSet Fset = new FeatureSet();
		Boolean val = true;
		//GenBankParser.setComplement(val,Fset);
		assertTrue(Fset.getcomplement());
		
	}
	*/
	
	
}
