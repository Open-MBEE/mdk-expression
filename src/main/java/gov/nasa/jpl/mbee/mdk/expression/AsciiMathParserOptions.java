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

import java.io.Serializable;

/**
 * Encapsulates various options for tweaking the behaviour of {@link AsciiMathParser}
 *
 * @author David McKain
 */
public final class AsciiMathParserOptions implements Serializable {

    private static final long serialVersionUID = -9109763775165601298L;

    private boolean displayMode;
    private boolean addSourceAnnotation;

    /**
     * Returns whether the resulting MathML will be marked up to be shown in "display mode", i.e.
     * whether it will have a 'display="block"' attribute.
     *
     * @return true if the resulting MathML will be in display mode, false otherwise.
     */
    public boolean isDisplayMode() {
        return displayMode;
    }

    /**
     * Sets whether the resulting MathML will be marked up to be shown in "display mode", i.e.
     * whether it will have a 'display="block"' attribute.
     *
     * @param displayMode true if the resulting MathML shuld be in display mode, false otherwise.
     */
    public void setDisplayMode(final boolean displayMode) {
        this.displayMode = displayMode;
    }


    /**
     * Returns whether the resulting MathML will contain an annotation containing the
     * original ASCIIMath source.
     *
     * @return true if an ASCIIMath source annotation will be included, false otherwise.
     */
    public boolean isAddSourceAnnotation() {
        return addSourceAnnotation;
    }

    /**
     * Sets whether the resulting MathML will contain an annotation containing the
     * original ASCIIMath source.
     *
     * @param addSourceAnnotation true if an ASCIIMath source annotation should be included,
     *   false otherwise.
     */
    public void setAddSourceAnnotation(final boolean addSourceAnnotation) {
        this.addSourceAnnotation = addSourceAnnotation;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this))
                + "(displayMode=" + displayMode
                + ",addSourceAnnotations=" + addSourceAnnotation
                + ")";
    }


}
