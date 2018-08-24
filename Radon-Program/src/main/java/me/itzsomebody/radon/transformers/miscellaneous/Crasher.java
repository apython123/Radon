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

package me.itzsomebody.radon.transformers.miscellaneous;

import java.util.concurrent.atomic.AtomicInteger;
import me.itzsomebody.radon.exclusions.ExclusionType;
import me.itzsomebody.radon.transformers.Transformer;
import me.itzsomebody.radon.utils.LoggerUtils;
import me.itzsomebody.radon.utils.RandomUtils;
import me.itzsomebody.radon.utils.StringUtils;
import org.objectweb.asm.tree.ClassNode;

public class Crasher extends Transformer {
    @Override
    public void transform() {
        AtomicInteger counter = new AtomicInteger();

        this.getClassWrappers().parallelStream().filter(classWrapper -> !excluded(classWrapper) && classWrapper.classNode.signature == null).forEach(classWrapper -> {
            ClassNode classNode = classWrapper.classNode;
            classNode.signature = randomString(4);
            counter.incrementAndGet();
        });

        LoggerUtils.stdOut(String.format("Added %d crashers.", counter.get()));
    }

    @Override
    protected ExclusionType getExclusionType() {
        return ExclusionType.CRASHER;
    }

    @Override
    public String getName() {
        return "Crasher";
    }
}
