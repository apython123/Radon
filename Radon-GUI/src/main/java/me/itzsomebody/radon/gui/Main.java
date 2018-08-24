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

package me.itzsomebody.radon.gui;

import java.io.File;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        runChecks();
        new RadonGUI();
    }

    private static void runChecks() {
        File file = new File("Radon-Program.jar");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Radon-Program.jar was not found in directory.", "Radon not found.", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        }

        try {
            Class.forName("me.itzsomebody.radon.Radon");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "me.itzsomebody.radon.Radon class was not found.", "Radon instance class not found.", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        }
    }
}
