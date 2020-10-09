/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
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

/**
 * JDK-8055034: jjs exits interactive mode if exception was thrown when trying to print value of last evaluated expression
 *
 * @test
 * @option -scripting
 * @run
 */

// assume that this script is run with "nashorn.jar" System
// property set to relative or absolute path of nashorn.jar

if (typeof fail != 'function') {
    fail = print;
}

var System = java.lang.System;
var File = java.io.File;

// we want to use nashorn.jar passed and not the one that comes with JRE
var jjsCmd = System.getProperty("jjs.executable");
if (! new File(jjsCmd).isFile()) {
	fail("no jjs executable");
}

$ENV.PWD=System.getProperty("user.dir") // to avoid RE on Cygwin
$EXEC(jjsCmd, "var x = Object.create(null);\nx;\nprint('PASSED');\nexit(0)");

// $ERR has all interactions including prompts! Just check for error substring.
var err = $ERR.trim();
if (! err.contains("TypeError: Cannot get default string value")) {
    fail("Error stream does not contain expected error message");
}

// should print "PASSED"
print($OUT.trim());
// exit code should be 0
print("exit code = " + $EXIT);
