package org.an.test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ElementFinder {
    private final String fileName;

    public ElementFinder(String fileName) {
        this.fileName = fileName;
    }

    public String findElement(String parentName, String name, String value) throws IOException, XMLStreamException {
        if(parentName == null || name == null || value == null) {
            return "";
        }
        try(InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(is);
            boolean found = false;
            while(xmlEventReader.hasNext()){
                XMLEvent event = xmlEventReader.nextEvent();
                if(found) {
                    if(event.isCharacters()) {
                        Characters characters = event.asCharacters();
                        return characters.getData();
                    }
                } else {
                    if (event.isStartElement()) {
                        StartElement e = event.asStartElement();
                        if (e.getName().getLocalPart().equals(parentName)) {
                            Attribute attribute = e.getAttributeByName(QName.valueOf(name));
                            if (attribute != null) {
                                if (value.equals(attribute.getValue())) {
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
}
