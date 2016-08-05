/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.RomlProgram;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * <p>Loads the 'heap' of a ROML Program, which is a set of ModelElement
 * definitions.  After loading, the Executor executes the Program.
 * <p>
 * <p>Analogous to a Java class Loader.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Loader extends DefaultHandler {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------

    private InputStream _in = null;
    private RomlProgram _program = null;

    //----------------------------------------------------------------------------
    // Constructor(s) (private, package, protected, public)
    //----------------------------------------------------------------------------

    /**
     * Constructor...
     */
    public Loader(InputStream in) {
        _in = in;
    }

    //----------------------------------------------------------------------------
    // Public - SAX parser methods
    //----------------------------------------------------------------------------

    public void startDocument()
            throws SAXException {
        super.startDocument();
    }

    public void endDocument()
            throws SAXException {
        super.endDocument();
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
    }

    //----------------------------------------------------------------------------
    // Package-level methods
    //----------------------------------------------------------------------------
    RomlProgram load()
            throws Exception {
//    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // use a sax parser to build our RomlProgram...

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        _program = new RomlProgram();

        parser.parse(_in, this);


        return _program;
    }

    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}
