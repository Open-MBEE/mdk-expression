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

/**
 * Trivial Exception thrown when an unexpected error occurs using the {@link AsciiMathParser}.
 *
 * @author David McKain
 */
public final class AsciiMathParserException extends RuntimeException {

    private static final long serialVersionUID = -6496941952396385125L;

    public AsciiMathParserException(final String message) {
        super(message);
    }

    public AsciiMathParserException(final Throwable cause) {
        super(cause);
    }

    public AsciiMathParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
