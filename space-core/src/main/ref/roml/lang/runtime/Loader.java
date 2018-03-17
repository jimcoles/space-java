/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang.runtime;

import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import java.io.InputStream;

import com.jkc.roml.lang.RomlProgram;

/**
 * <p>Loads the 'heap' of a ROML Program, which is a set of ModelElement
 * definitions.  After loading, the Executor executes the Program.
 *
 * <p>Analogous to a Java class XmlLoader.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Loader extends DefaultHandler
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  private InputStream _in = null;
  private RomlProgram _program = null;

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public Loader (InputStream in)
  {
    _in = in;
  }

  //----------------------------------------------------------------------------
  // Public - SAX parser methods
  //----------------------------------------------------------------------------

  public void startDocument()
      throws SAXException
  {
    super.startDocument();
  }

  public void endDocument()
      throws SAXException
  {
    super.endDocument();
  }

  public void startElement(String uri, String localName,
                           String qName, Attributes attributes)
      throws SAXException
  {
    super.startElement(uri, localName, qName, attributes);
  }

  public void endElement(String uri, String localName, String qName)
      throws SAXException
  {
    super.endElement(uri, localName, qName);
  }

  //----------------------------------------------------------------------------
  // Package-level methods
  //----------------------------------------------------------------------------
  RomlProgram load()
    throws Exception
  {
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
