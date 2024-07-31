package org.example;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Serializable;
import java.io.StringReader;

public class Document {
    public static void main(String[] args) {
        try {
            // Sample XML
            String xml = "<root><element>Sample</element><element>Sample2</element></root>";

            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the sample XML
            InputSource inputSource = new InputSource(new StringReader(xml));
            org.w3c.dom.Document document = builder.parse(inputSource);

            // Print the actual implementation class of the Document
            System.out.println("Document implementation class: " + document.getClass().getName());
            System.out.println(document instanceof Serializable);

            NodeList nodeList = document.getElementsByTagName("element");
            System.out.println("Document implementation class: " + nodeList.getClass().getName());
            System.out.println(nodeList instanceof Serializable);

            Node node = nodeList.item(1);
            System.out.println("Document implementation class: " + node.getClass().getName());
            System.out.println(node instanceof Serializable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

