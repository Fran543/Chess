/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.engine.model.board.Board.Builder;
import hr.algebra.engine.model.pieces.Piece;
import hr.algebra.utilities.BuilderSaxParser.Game;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author fran
 */
public class BuilderXmlParser {
    
    private static final String FILE_NAME = "moves.xml";

    
    public static void creteXmlDocument(List<Builder> bulderList) {
        try {
            StringWriter stringWriter = new StringWriter();
            
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);
            
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("Builders");
            for(Builder builder : bulderList) {

                xMLStreamWriter.writeStartElement(Builder.class.getSimpleName());
                    xMLStreamWriter.writeAttribute("nextMoveMaker", builder.getNextMoveMaker().name());
                    xMLStreamWriter.writeStartElement("boardConfig");
                    for (Piece piece : builder.boardConfig.values()) {
                        xMLStreamWriter.writeStartElement(Piece.class.getSimpleName());
                        xMLStreamWriter.writeAttribute("PieceType", piece.getPieceType().name());
                        xMLStreamWriter.writeAttribute("piecePosition", piece.getPiecePosition().toString());
                        xMLStreamWriter.writeAttribute("pieceColor", piece.getPieceColor().name());
                        xMLStreamWriter.writeAttribute("isFirstMove", piece.isIsFirstMove()? "True" : "False");
                        xMLStreamWriter.writeEndElement();
                    }
                    xMLStreamWriter.writeEndElement();
                    
                    if (builder.getEnPassantPawn() != null) {                    
                        xMLStreamWriter.writeStartElement("enPassantPawn");
                            xMLStreamWriter.writeAttribute("PieceType", builder.getEnPassantPawn().getPieceType().name());
                            xMLStreamWriter.writeAttribute("piecePosition", builder.getEnPassantPawn().getPiecePosition().toString());
                            xMLStreamWriter.writeAttribute("pieceColor", builder.getEnPassantPawn().getPieceColor().name());
                            xMLStreamWriter.writeAttribute("isFirstMove", builder.getEnPassantPawn().isIsFirstMove()? "True" : "False");
                        xMLStreamWriter.writeEndElement();
                    }
                
                xMLStreamWriter.writeEndElement();

            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            
            String xmlString = stringWriter.getBuffer().toString();
            
            stringWriter.close();
            
            Document xmlDocument = convertStringToXMLDocument( xmlString );

            saveXmlDocToFile(xmlDocument);
        
        } catch (XMLStreamException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static Document convertStringToXMLDocument(String xmlString) 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
             
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    public static void saveXmlDocToFile(Document xmlDocument) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDocument);
            
            FileWriter writer = new FileWriter(new File(FILE_NAME));
            StreamResult result = new StreamResult(writer);
            
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Game readXmlDocFromFile() {
        BuilderSaxParser builderSaxParser = new BuilderSaxParser();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance ();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(FILE_NAME, builderSaxParser);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuilderXmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return builderSaxParser.getGame();
    }
    
}
