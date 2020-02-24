/*
 * Copyright (c) 2002, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 4652655 4857717 8025633 8026567 8071982 8164407 8182765 8205593
 * @summary This test verifies that class cross references work properly.
 * @library ../../lib
 * @modules jdk.javadoc/jdk.javadoc.internal.tool
 * @build javadoc.tester.*
 * @build TestClassCrossReferences
 * @run main TestClassCrossReferences
 */

import javadoc.tester.JavadocTester;

public class TestClassCrossReferences extends JavadocTester {

    static final String uri = "http://docs.oracle.com/javase/8/docs/api/";

    public static void main(String... args) throws Exception {
        TestClassCrossReferences tester = new TestClassCrossReferences();
        tester.runTests();
    }

    @Test
    public void test() {
        javadoc("-d", "out",
                "-source", "8",
                "-Xdoclint:none",
                "-sourcepath", testSrc,
                "-linkoffline", uri, testSrc,
                testSrc("C.java"));
        checkExit(Exit.OK);

        checkOutput("C.html", true,
                "<a href=\"" + uri + "java/math/package-summary.html\" class=\"externalLink\">"
                + "<code>Link to math package</code></a>",
                "<a href=\"" + uri + "javax/swing/text/AbstractDocument.AttributeContext.html\" "
                + "title=\"class or interface in javax.swing.text\" class=\"externalLink\"><code>Link to AttributeContext innerclass</code></a>",
                "<a href=\"" + uri + "java/math/BigDecimal.html\" "
                + "title=\"class or interface in java.math\" class=\"externalLink\"><code>Link to external class BigDecimal</code></a>",
                "<a href=\"" + uri + "java/math/BigInteger.html#gcd(java.math.BigInteger)\" "
                + "title=\"class or interface in java.math\" class=\"externalLink\"><code>Link to external member gcd</code></a>",
                "<a href=\"" + uri + "javax/tools/SimpleJavaFileObject.html#uri\" "
                + "title=\"class or interface in javax.tools\" class=\"externalLink\"><code>Link to external member URI</code></a>",
                "<dl>\n"
                + "<dt><span class=\"overrideSpecifyLabel\">Overrides:</span></dt>\n"
                + "<dd><code>toString</code>&nbsp;in class&nbsp;<code>java.lang.Object</code></dd>\n"
                + "</dl>");
    }

    @Test
    public void test_error() {
        javadoc("-d", "out-error",
                "-Xdoclint:none",
                "-sourcepath", testSrc,
                "-linkoffline", uri, testSrc,
                testSrc("C.java"));
        checkExit(Exit.ERROR);
        checkOutput(Output.OUT, true,
                "The code being documented uses modules but the packages defined"
                + " in http://docs.oracle.com/javase/8/docs/api/ are in the unnamed module");
    }
}
