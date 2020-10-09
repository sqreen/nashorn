/*
 * Copyright (c) 2010, 2016, Oracle and/or its affiliates. All rights reserved.
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

import static io.sqreen.internal.org.objectweb.asm.Opcodes.BIPUSH;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.I2D;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.I2L;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IADD;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IAND;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_0;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_1;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_2;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_3;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_4;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_5;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ICONST_M1;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ILOAD;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IMUL;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.INEG;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IOR;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IRETURN;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ISHL;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ISHR;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ISTORE;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.ISUB;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IUSHR;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.IXOR;
import static io.sqreen.internal.org.objectweb.asm.Opcodes.SIPUSH;
import static io.sqreen.nashorn.internal.codegen.CompilerConstants.staticCallNoLookup;

import io.sqreen.nashorn.internal.codegen.CompilerConstants;
import io.sqreen.nashorn.internal.runtime.JSType;
import io.sqreen.nashorn.internal.runtime.UnwarrantedOptimismException;
import io.sqreen.internal.org.objectweb.asm.MethodVisitor;

/**
 * Type class: INT
 */
class IntType extends BitwiseType {
    private static final long serialVersionUID = 1L;

    private static final CompilerConstants.Call TO_STRING = CompilerConstants.staticCallNoLookup(Integer.class, "toString", String.class, int.class);
    private static final CompilerConstants.Call VALUE_OF  = CompilerConstants.staticCallNoLookup(Integer.class, "valueOf", Integer.class, int.class);

    protected IntType() {
        super("int", int.class, 2, 1);
    }

    @Override
    public Type nextWider() {
        return io.sqreen.nashorn.internal.codegen.types.Type.NUMBER;
    }

    @Override
    public Class<?> getBoxedType() {
        return Integer.class;
    }

    @Override
    public char getBytecodeStackType() {
        return 'I';
    }

    @Override
    public Type ldc(final MethodVisitor method, final Object c) {
        assert c instanceof Integer;

        final int value = ((Integer) c);

        switch (value) {
        case -1:
            method.visitInsn(ICONST_M1);
            break;
        case 0:
            method.visitInsn(ICONST_0);
            break;
        case 1:
            method.visitInsn(ICONST_1);
            break;
        case 2:
            method.visitInsn(ICONST_2);
            break;
        case 3:
            method.visitInsn(ICONST_3);
            break;
        case 4:
            method.visitInsn(ICONST_4);
            break;
        case 5:
            method.visitInsn(ICONST_5);
            break;
        default:
            if (value == (byte) value) {
                method.visitIntInsn(BIPUSH, value);
            } else if (value == (short) value) {
                method.visitIntInsn(SIPUSH, value);
            } else {
                method.visitLdcInsn(c);
            }
            break;
        }

        return Type.INT;
    }

    @Override
    public Type convert(final MethodVisitor method, final Type to) {
        if (to.isEquivalentTo(this)) {
            return to;
        }

        if (to.isNumber()) {
            method.visitInsn(I2D);
        } else if (to.isLong()) {
            method.visitInsn(I2L);
        } else if (to.isBoolean()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, JSType.TO_BOOLEAN_I);
        } else if (to.isString()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, TO_STRING);
        } else if (to.isObject()) {
            io.sqreen.nashorn.internal.codegen.types.Type.invokestatic(method, VALUE_OF);
        } else {
            throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
        }

        return to;
    }

    @Override
    public Type add(final MethodVisitor method, final int programPoint) {
        if(programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            method.visitInsn(IADD);
        } else {
            ldc(method, programPoint);
            JSType.ADD_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type shr(final MethodVisitor method) {
        method.visitInsn(IUSHR);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type sar(final MethodVisitor method) {
        method.visitInsn(ISHR);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type shl(final MethodVisitor method) {
        method.visitInsn(ISHL);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type and(final MethodVisitor method) {
        method.visitInsn(IAND);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type or(final MethodVisitor method) {
        method.visitInsn(IOR);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type xor(final MethodVisitor method) {
        method.visitInsn(IXOR);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type load(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(ILOAD, slot);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public void store(final MethodVisitor method, final int slot) {
        assert slot != -1;
        method.visitVarInsn(ISTORE, slot);
    }

    @Override
    public Type sub(final MethodVisitor method, final int programPoint) {
        if(programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            method.visitInsn(ISUB);
        } else {
            ldc(method, programPoint);
            JSType.SUB_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type mul(final MethodVisitor method, final int programPoint) {
        if(programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            method.visitInsn(IMUL);
        } else {
            ldc(method, programPoint);
            JSType.MUL_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type div(final MethodVisitor method, final int programPoint) {
        if (programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            JSType.DIV_ZERO.invoke(method);
        } else {
            ldc(method, programPoint);
            JSType.DIV_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type rem(final MethodVisitor method, final int programPoint) {
        if (programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            JSType.REM_ZERO.invoke(method);
        } else {
            ldc(method, programPoint);
            JSType.REM_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type neg(final MethodVisitor method, final int programPoint) {
        if(programPoint == UnwarrantedOptimismException.INVALID_PROGRAM_POINT) {
            method.visitInsn(INEG);
        } else {
            ldc(method, programPoint);
            JSType.NEGATE_EXACT.invoke(method);
        }
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public void _return(final MethodVisitor method) {
        method.visitInsn(IRETURN);
    }

    @Override
    public Type loadUndefined(final MethodVisitor method) {
        method.visitLdcInsn(JSType.UNDEFINED_INT);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type loadForcedInitializer(final MethodVisitor method) {
        method.visitInsn(ICONST_0);
        return io.sqreen.nashorn.internal.codegen.types.Type.INT;
    }

    @Override
    public Type cmp(final MethodVisitor method, final boolean isCmpG) {
        throw new UnsupportedOperationException("cmp" + (isCmpG ? 'g' : 'l'));
    }

    @Override
    public Type cmp(final MethodVisitor method) {
        throw new UnsupportedOperationException("cmp");
    }

}
