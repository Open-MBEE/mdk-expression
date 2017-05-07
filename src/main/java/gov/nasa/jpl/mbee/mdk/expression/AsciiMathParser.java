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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Simple Java wrapper round AsciiMathParser.js that runs it using the Rhino JavaScript engine.
 * <p>
 * An instance of this class is thread-safe and reusable.
 *
 * @author David McKain
 */
public final class AsciiMathParser {

    /**
     * Option to add <tt>display="block"</tt> to the resulting <tt>math</tt> element
     */
    public static final String OPTION_DISPLAY_MODE = "displayMode";

    /**
     * Option to add an annotation to the resulting MathML showing the ASCIIMath input
     */
    public static final String OPTION_ADD_SOURCE_ANNOTATION = "addSourceAnnotation";

    /**
     * Name of the AsciiMathParser.js file.
     */
    public static final String ASCIIMATH_PARSER_JS_NAME = "AsciiMathParser.js";

    /**
     * Location of the bundled AsciiMathParser.js within the ClassPath. (It is included in the JAR file)
     */
    public static final String ASCIIMATH_PARSER_JS_LOCATION = "AsciiMathParser.js";

    /**
     * Shared scope used by a single instance of this Class
     */
    private final ScriptableObject sharedScope;

    public AsciiMathParser() {
        this(ASCIIMATH_PARSER_JS_LOCATION);
    }

    public AsciiMathParser(final String classPathLocation) {
        final InputStream parserJavaScriptStream = getClass().getClassLoader().getResourceAsStream(classPathLocation);
        if (parserJavaScriptStream == null) {
            throw new AsciiMathParserException("AsciiMathParser.js was not found in the ClassPath at " + classPathLocation);
        }
        try {
            /* Read in JavaScript from the ClassPath */
            final Reader parserJSFileReader = new InputStreamReader(parserJavaScriptStream, "UTF-8");

            /* Evaluate the parser script and store away the results */
            final Context context = Context.enter();
            this.sharedScope = context.initStandardObjects();
            context.evaluateReader(sharedScope, parserJSFileReader, "AsciiMathParser.js", 1, null);
            Context.exit();
        } catch (final Exception e) {
            throw new AsciiMathParserException("Error parsing AsciiMathParser.js", e);
        } finally {
            try {
                parserJavaScriptStream.close();
            } catch (final Exception e) {
                throw new AsciiMathParserException("Error closing AsciiMathParser.js stream", e);
            }
        }
    }

    /**
     * Parses the given ASCIIMath input, returning a DOM {@link Document} Object containing the
     * resulting MathML <tt>math</tt> element.
     *
     * @param asciiMathInput ASCIIMath input, which must not be null
     * @return resulting MathML {@link Document} object
     * @throws IllegalArgumentException if asciiMathInput is null
     * @throws AsciiMathParserException if an unexpected Exception happened
     * @see #parseAsciiMath(String, AsciiMathParserOptions)
     */
    public Document parseAsciiMath(final String asciiMathInput) {
        return parseAsciiMath(asciiMathInput, null);
    }

    /**
     * Parses the given ASCIIMath input, returning a DOM {@link Document} Object containing the
     * resulting MathML <tt>math</tt> element, using the given {@link AsciiMathParserOptions} to
     * tweak the results.
     *
     * @param asciiMathInput ASCIIMath input, which must not be null
     * @param options        optional {@link AsciiMathParserOptions}
     * @return resulting MathML {@link Document} object
     * @throws IllegalArgumentException if asciiMathInput is null
     * @throws AsciiMathParserException if an unexpected Exception happened
     * @see #parseAsciiMath(String)
     */
    public Document parseAsciiMath(final String asciiMathInput, final AsciiMathParserOptions options) {
        if (asciiMathInput == null) {
            throw new IllegalArgumentException("AsciiMathInput must not be null");
        }
        /* Create DOM Document for the parser to use */
        final Document document = XmlUtilities.createNSAwareDocumentBuilder().newDocument();

        /* Call up the JavaScript parsing code */
        final Context context = Context.enter();
        try {
            final Scriptable newScope = context.newObject(sharedScope);

            final Scriptable parser = context.newObject(newScope, "AsciiMathParser", new Object[]{document});
            final Scriptable optionsJS = context.newObject(newScope);
            if (options != null) {
                if (options.isDisplayMode()) {
                    ScriptableObject.putProperty(optionsJS, OPTION_DISPLAY_MODE, Boolean.TRUE);
                }
                if (options.isAddSourceAnnotation()) {
                    ScriptableObject.putProperty(optionsJS, OPTION_ADD_SOURCE_ANNOTATION, Boolean.TRUE);
                }
            }
            final Object result = ScriptableObject.callMethod(parser, "parseAsciiMathInput", new Object[]{asciiMathInput, optionsJS});
            final Element mathElement = (Element) Context.jsToJava(result, Element.class);
            document.appendChild(mathElement);
            return document;
        } catch (final Exception e) {
            throw new AsciiMathParserException("Error running AsciiMathParser.js on input", e);
        } finally {
            Context.exit();
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Trivial main method that simply runs {@link AsciiMathParser} on the command line arguments
     * (concatenated together with spaces), printing out the resulting MathML document.
     */
    public static void main(final String[] args) {
        final StringBuilder inputBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            inputBuilder.append(args[i]).append(" ");
        }
        final String asciiMathInput = inputBuilder.toString();

        final AsciiMathParser parser = new AsciiMathParser();
        final Document result = parser.parseAsciiMath(asciiMathInput);

        System.out.println(inputBuilder.toString());
        System.out.println(XmlUtilities.serializeMathmlDocument(result));
    }
}
