package Game;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.binary.Base64;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;



public class XMLParser {
	private ArrayList<Person> ScoreData = new ArrayList<Person>(); 
	private String rawXML;
	private int dataCount=0;

	public XMLParser(String xml) {
		rawXML = xml;
		xmlBuilder();
	}
	
	// If you need using additional elements, are able to use recursive method like below annotation code.
	private void parsing(List<Element> elements){
		for (Element element : elements) {
			/*
			List<Element> e = element.getChildren();
			if (element.getName().equals("Person") && e.size() != 0) {
			*/
			
			if (element.getName().equals("Person")) {				
				ScoreData.add(new Person());
				List<Attribute> attrs = element.getAttributes();
				for (Attribute a : attrs) {
					if (a.getName().equals("name")) {
						ScoreData.get(ScoreData.size()-1).setName(a.getValue());
					} else if (a.getName().equals("score")) {
						ScoreData.get(ScoreData.size()-1).setScore(a.getValue());
					} else if (a.getName().equals("date")) {
						ScoreData.get(ScoreData.size()-1).setDate(a.getValue());
					} else if (a.getName().equals("comment")) {
						ScoreData.get(ScoreData.size()-1).setComment(a.getValue());
					}
				}
			} else if (element.getName().equals("TotalDataCount")) {				
				dataCount = Integer.parseInt(element.getValue());
			}
			
			/*
			parsing(e);
			} else {
				if (element.getName().equals("name")) {
					ScoreData.get(ScoreData.size()-1).setName(element.getText());
				} else if (element.getName().equals("score")) {
					ScoreData.get(ScoreData.size()-1).setScore(element.getText());
				} else if (element.getName().equals("date")) {
					ScoreData.get(ScoreData.size()-1).setDate(element.getText());
				}
			}
			*/
		}
	}

	
	private void xmlBuilder() {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		StringBuilder sb = new StringBuilder(rawXML);
		try {
			doc = builder.build(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));

			Element root = doc.getRootElement();
			List<Element> elements = root.getChildren();
			parsing(elements);
			
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public String toString(){
		return ScoreData.toString();
	}
	
	public ArrayList<Person> getScoreData(){
		return ScoreData;
	}
	
	public int getPageCount() {
		return dataCount;
	}
}