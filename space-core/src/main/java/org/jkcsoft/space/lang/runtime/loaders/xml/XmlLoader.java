/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.xml;

import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.Directory;
import org.jkcsoft.space.lang.ast.SourceInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * <p>Loads the 'heap' of a Space Program, which is a set of ModelElement
 * definitions.  After loading, the Executor executes the Program.
 * <p>
 * <p>Analogous to a Java class XmlLoader.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class XmlLoader extends DefaultHandler {

    private InputStream _in = null;
    private Directory _program = null;
    private AstFactory astFactory = new AstFactory();

    public XmlLoader(InputStream in) {
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
    public Directory load()
            throws Exception {
//    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // use a sax parser to build our Directory...

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        _program = astFactory.newProgram(null, astFactory.newNamePart(SourceInfo.API, ""));

        parser.parse(_in, this);

        return _program;
    }

}
