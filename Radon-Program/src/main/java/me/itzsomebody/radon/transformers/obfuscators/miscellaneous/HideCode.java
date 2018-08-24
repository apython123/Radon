/*
 * Copyright (C) 2018 ItzSomebody
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.itzsomebody.radon.transformers.obfuscators.miscellaneous;

import java.util.concurrent.atomic.AtomicInteger;
import me.itzsomebody.radon.exclusions.ExclusionType;
import me.itzsomebody.radon.transformers.Transformer;
import me.itzsomebody.radon.utils.AccessUtils;
import me.itzsomebody.radon.utils.BytecodeUtils;
import me.itzsomebody.radon.utils.LoggerUtils;
import org.objectweb.asm.tree.ClassNode;

public class HideCode extends Transformer {
    @Override
    public void transform() {
        AtomicInteger counter = new AtomicInteger();

        this.getClassWrappers().parallelStream().filter(classWrapper -> !excluded(classWrapper)).forEach(classWrapper -> {
            ClassNode classNode = classWrapper.classNode;
            if (!AccessUtils.isSynthetic(classNode.access) && !BytecodeUtils.hasAnnotations(classNode)) {
                classNode.access |= ACC_SYNTHETIC;
                counter.incrementAndGet();
            }

            classNode.methods.parallelStream().filter(methodNode -> !BytecodeUtils.hasAnnotations(methodNode)).forEach(methodNode -> {
                boolean hidOnce = false;
                if (!AccessUtils.isSynthetic(methodNode.access)) {
                    methodNode.access |= ACC_SYNTHETIC;
                    hidOnce = true;
                }

                if (!AccessUtils.isBridge(methodNode.access) && !methodNode.name.startsWith("<")) {
                    methodNode.access |= ACC_BRIDGE;
                    hidOnce = true;
                }

                if (hidOnce)
                    counter.incrementAndGet();
            });

            if (classNode.fields != null)
                classNode.fields.parallelStream().filter(fieldNode -> !BytecodeUtils.hasAnnotations(fieldNode)
                        && !AccessUtils.isSynthetic(fieldNode.access)).forEach(fieldNode -> {
                    fieldNode.access |= ACC_SYNTHETIC;
                    counter.incrementAndGet();
                });
        });

        LoggerUtils.stdOut(String.format("Hid %d members.", counter.get()));
    }

    @Override
    protected ExclusionType getExclusionType() {
        return ExclusionType.HIDE_CODE;
    }

    @Override
    public String getName() {
        return "Hide code";
    }
}
