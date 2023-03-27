package org.an.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ElementFinderTest {
    private static final String FILE_NAME = "test-xml.xml";
    //private static final int ELEMENT_COUNT = 120_000_000;
    private static final int ELEMENT_COUNT = 10;

    private static final String FINDING_ELEMENT_NAME = "elementToFind";
    private static final String FINDING_ATTR_NAME = "attrToFind";

    private static final String START_MARK = "startMark";
    private static final String MIDDLE_MARK = "middleMark";
    private static final String END_MARK = "endMark";

    @Test
    public void dataGen() throws IOException, XMLStreamException {
        String fileName = getFileName();
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName))) {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(os);
            try {
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();
                xmlEventWriter.add(eventFactory.createStartDocument());
                xmlEventWriter.add(eventFactory
                        .createStartElement("", null, "root"));
                for (int i = 0; i < ELEMENT_COUNT; i++) {
                    String value;
                    String elementText = "Position:" + i;
                    if (i == 0) {
                        value = START_MARK;
                    } else if (i == ELEMENT_COUNT - 1) {
                        value = END_MARK;
                    } else if (i == ELEMENT_COUNT / 2) {
                        value = MIDDLE_MARK;
                    } else {
                        value = UUID.randomUUID().toString();
                    }
                    List<Attribute> attributes = new ArrayList<>() {{
                        add(eventFactory.createAttribute(FINDING_ATTR_NAME, value));
                    }};
                    StartElement element = eventFactory
                            .createStartElement("", null, FINDING_ELEMENT_NAME,
                                    attributes.iterator(), null);
                    xmlEventWriter.add(element);
                    xmlEventWriter.add(eventFactory.createCharacters(elementText));
                    xmlEventWriter.add(eventFactory
                            .createEndElement("", null, FINDING_ELEMENT_NAME));
                }
                xmlEventWriter.add(eventFactory.createEndElement("", null, "root"));
                xmlEventWriter.add(eventFactory.createEndDocument());
            } finally {
                xmlEventWriter.close();
            }
        }
    }

    @Test(dependsOnMethods = {"dataGen"})
    public void testFindElement1() throws XMLStreamException, IOException {
        String result = testFindElement(END_MARK);
        Assert.assertEquals(result, "Postion:0");
    }

    @Test(dependsOnMethods = {"dataGen"})
    public void testFindElement2() throws XMLStreamException, IOException {
        String result = testFindElement(MIDDLE_MARK);
        Assert.assertEquals(result, "Postion:" + (ELEMENT_COUNT / 2));
    }

    @Test(dependsOnMethods = {"dataGen"})
    public void testFindElement3() throws XMLStreamException, IOException {
        String result = testFindElement(END_MARK);
        Assert.assertEquals(result, "Postion:" + (ELEMENT_COUNT - 1));
    }

    private String testFindElement(String valueToFind) throws XMLStreamException, IOException {
        ElementFinder elementFinder = new ElementFinder(getFileName());
        long t0 = System.currentTimeMillis();
        String result = elementFinder.findElement(FINDING_ELEMENT_NAME, FINDING_ATTR_NAME, valueToFind);
        System.out.println("Find time (ms): " + (System.currentTimeMillis() - t0));
        return result;
    }

    private String getFileName() {
        return Path.of(FILE_NAME).toAbsolutePath().toString();
    }
}