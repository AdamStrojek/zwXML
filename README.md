zwXML
=====

Library to create and parse XML files for Java. It was designed to be as easy as possible.

zwXMLStreamWriter
-----

Example usage:

    zwXMLStreamWriter xml = new zwXMLStreamWriter(System.out);
    
    xml.startDocument();
    xml.startElement("example");
    xml.startElement("subexample").writeAttribute("number", 123).endElement();
    xml.endElement();
    xml.endDocument();
    
    System.out.flush();
