/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

package io.sqreen.nashorn.internal.codegen.types;

import static io.sqreen.internal.org.objectweb.asm.Opcodes.DADD;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DCMPG;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DCMPL;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DCONST_0;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DCONST_1;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DDIV;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DLOAD;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DMUL;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DNEG;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DREM;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DRETURN;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DSTORE;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.DSUB;
import static io.sqreen.nashorn.internal.codegen.CompilerConstants.staticCallNoLookup;

import io.sqreen.nashorn.internal.codegen.CompilerConstants;
import io.sqreen.nashorn.internal.runtime.JSType;
import io.sqreen.internal.org.objectweb.asm.MethodVisitor;

class NumberType extends NumericType {
    private static final long serialVersionUID = 1L;

    private static final CompilerConstants.Call VALUE_OF = CompilerConstants.staticCallNoLookup(Double.class, "valueOf", Double.class, double.class);

    protected NumberType() {
        super("double", double.class, 4, 2);
    }

    @Override
    public Type nextWider() {
        return io.sqreen.nashorn.internal.codegen.types.Type.OBJECT;
    }

    @Override
    public Class<?> getBoxedType() {
        return Double.class;
    }

    @Override
    public char getBytecodeStackType() {
        return 'D';
    }

    @Override
    public Type cmp(final MethodVisitor method, final boolean isCmpG) {
        method.visitInsn(isCmpG ? DCMPG : DCMPL);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(DLOAD, slot);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(DSTORE, slot);
    }

    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(JSType.UNDEFINED_DOUBLE);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(DCONST_0);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Double;

        final double value = (Double) c;

        if (Double.doubleToLongBits(value) == 0L) { // guard against -0.0
            method.visitInsn(DCONST_0);
        } else if (value == 1.0) {
            method.visitInsn(DCONST_1);
        } else {
            method.visitLdcInsn(value);
        }

        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (isEquivalentTo(to)) {
            return null;
        }

        if (to.isInteger()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, JSType.TO_INT32_D);
        } else if (to.isLong()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, JSType.TO_LONG_D);
        } else if (to.isBoolean()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, JSType.TO_BOOLEAN_D);
        } else if (to.isString()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, JSType.TO_STRING_D);
        } else if (to.isObject()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, VALUE_OF);
        } else {
            throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
        }

        return to;
    }

    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DADD);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type sub(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DSUB);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type mul(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DMUL);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type div(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DDIV);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type rem(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DREM);
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Type neg(final MethodVisitor method, final int programPoint) {
        method.visitInsn(DNEG);
        return NUMBER;
    }

    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(DRETURN);
    }
}
