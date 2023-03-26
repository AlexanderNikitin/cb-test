package org.an.test;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class ElementFinder {
    private final String fileName;

    public ElementFinder(String fileName) {
        this.fileName = fileName;
    }

    public String findElement(String parentName, String name, String value) throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(new File(fileName), new DefaultHandler2());
        return null;
    }
}
