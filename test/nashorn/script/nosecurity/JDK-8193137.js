/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 * JDK-8193137 : Nashorn crashes when given an empty script file.
 *
 * @test
 * @option -scripting
 * @run
 */

var System = java.lang.System;
var File = java.io.File;

var jjsCmd = System.getProperty("jjs.executable");
if (! new File(jjsCmd).isFile()) {
	fail("Could not find jjs executable");
}

$ENV.PWD=System.getProperty("user.dir")

var emptyFile = new File($ENV.PWD+File.separator+"empty.js");
emptyFile.createNewFile();
emptyFile.deleteOnExit();

$EXEC(jjsCmd + " --no-deprecation-warning empty.js");
if($ERR != "")
    fail("jjs fails with empty script file");
