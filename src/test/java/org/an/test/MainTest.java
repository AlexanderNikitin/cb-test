package org.an.test;

import org.testng.annotations.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


public class MainTest {
    private static final String FILE_NAME = "test-xml.xml";
    //private static final int ELEMENT_COUNT = 120_000_000;
    private static final int ELEMENT_COUNT = 1;

    private static final String FINDING_ELEMENT_NAME = "elementToFind";
    private static final String FINDING_ATTR_NAME = "attrToFind";

    private static final String START_MARK = "startMark";
    private static final String MIDDLE_MARK = "middleMark";
    private static final String END_MARK = "endMark";

    @Test
    public void dataGen() throws ParserConfigurationException, IOException, XMLStreamException {
        String fileName = getFileName();
        try (OutputStream os = new FileOutputStream(fileName)) {
            XMLEventWriter xmlEventWriter = XMLOutputFactory.newFactory().createXMLEventWriter(os);
            try {
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();
                xmlEventWriter.add(eventFactory.createStartDocument());
                xmlEventWriter.add(eventFactory
                        .createStartElement("", null, "root"));
                for (int i = 0; i < ELEMENT_COUNT; i++) {
                    StartElement element = eventFactory
                            .createStartElement("", null, "element");
                    xmlEventWriter.add(element);
                    xmlEventWriter.add(eventFactory
                            .createEndElement("", null, "element"));
                }
                xmlEventWriter.add(eventFactory.createEndElement("", null, "root"));
                xmlEventWriter.add(eventFactory.createEndDocument());
            } finally {
                xmlEventWriter.close();
            }
        }
    }

    private String getFileName() throws IOException {
        return Files.createTempDirectory("").resolve(FILE_NAME).toAbsolutePath().toString();
    }
}