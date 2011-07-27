/*
 * Supernatural Players Plugin for Bukkit
 * Copyright (C) 2011  Matt Walker <mmw167@gmail.com>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package me.matterz.supernaturals.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class SNPlayerHandler {
	
	@SuppressWarnings("unchecked")
    public static List<SuperNPlayer> load(File file) {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(SuperNPlayer.class, new Tag("player")));

        Yaml yaml = new Yaml(constructor);

        try {
            List<SuperNPlayer> supernaturals = (List<SuperNPlayer>) yaml.load(new FileReader(file));

            if (supernaturals == null)
                return new ArrayList<SuperNPlayer>();
            
            return supernaturals;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void save(List<SuperNPlayer> supernaturals, File file) {
        Representer representer = new Representer();
        representer.addClassTag(SuperNPlayer.class, new Tag("player"));

        DumperOptions options = new DumperOptions();
        options.setWidth(300);
        options.setIndent(4);

        Yaml yaml = new Yaml(representer, options);

        try {
            yaml.dump(supernaturals, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
