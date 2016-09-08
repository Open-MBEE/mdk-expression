/* Copyright (c) 2011-2012, The University of Edinburgh.
 * All Rights Reserved.
 *
 * This file is part of AsciiMathParser.
 *
 * AsciiMathParser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AsciiMathParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License (at http://www.gnu.org/licences/lgpl.html)
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AsciiMathParser. If not, see <http://www.gnu.org/licenses/lgpl.html>.
 */
package gov.nasa.jpl.mbee.mdk.expression;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Some basic XML helpers.
 *
 * @author David McKain
 */
final class XmlUtilities {

    /**
     * Creates a (namespace-aware) DOM {@link DocumentBuilder}, throwing an {@link AsciiMathParserException}
     * if such a thing cannot be created/configured.
     *
     * @throws AsciiMathParserException if a namespace-aware DOM {@link DocumentBuilder}
     *   could not be created.
     */
    public static DocumentBuilder createNSAwareDocumentBuilder() {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        try {
            return documentBuilderFactory.newDocumentBuilder();
        }
        catch (final ParserConfigurationException e) {
            throw new AsciiMathParserException("Could not create Namespace-aware DocumentBuilder", e);
        }
    }

    /**
     * Creates an instance of the currently specified JAXP {@link TransformerFactory}, ensuring
     * that the result supports the {@link DOMSource#FEATURE} and {@link DOMResult#FEATURE}
     * features.
     */
    public static TransformerFactory createJaxpTransformerFactory() {
        TransformerFactory transformerFactory = null;
        try {
            transformerFactory = TransformerFactory.newInstance();
        }
        catch (final TransformerFactoryConfigurationError e) {
            throw new AsciiMathParserException(e);
        }
        /* Make sure we have DOM-based features */
        requireFeature(transformerFactory, DOMSource.FEATURE);
        requireFeature(transformerFactory, DOMResult.FEATURE);

        /* Must have been OK! */
        return transformerFactory;
    }

    public static void requireFeature(final TransformerFactory transformerFactory, final String feature) {
        if (!transformerFactory.getFeature(feature)) {
            throw new AsciiMathParserException("TransformerFactory "
                    + transformerFactory.getClass().getName()
                    + " needs to support feature "
                    + feature
                    + " in order to be used with AsciiMathParser");
        }
    }

    /**
     * Helper to turn on indentation for a {@link Transformer} that works correctly for
     * both Saxon and Xalan.
     *
     * @param transformer {@link Transformer} to configure
     * @param indent required indentation, where 0 or more provides indentation and negative
     *   numbers turns indentation off.
     */
    public static void setIndentation(final Transformer transformer, final int indent) {
        if (indent>=0) {
            final String indentString = String.valueOf(indent);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            /* Set custom properties for both Saxon and Xalan at once.
             * This appears safe to do without having to check the underlying processor.
             */
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indentString);
            transformer.setOutputProperty("{http://saxon.sf.net/}indent-spaces", indentString);
        }
        else {
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
        }
    }

    public static String serializeMathmlDocument(final Document mathml) {
        final StringWriter stringWriter = new StringWriter();
        try {
            final Transformer transformer = XmlUtilities.createJaxpTransformerFactory().newTransformer();
            XmlUtilities.setIndentation(transformer, 2);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(mathml), new StreamResult(stringWriter));
        }
        catch (final TransformerException e) {
            throw new AsciiMathParserException("Could not serialize MathML DOM", e);
        }
        return stringWriter.toString();
    }
}