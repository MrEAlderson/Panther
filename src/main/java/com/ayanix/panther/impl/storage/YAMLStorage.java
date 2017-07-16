/*
 *                                                            _...---.._
 *                                                        _.'`       -_ ``.
 *                                                    .-'`                 `.
 *                                                 .-`                     q ;
 *                                              _-`                       __  \
 *                                          .-'`                  . ' .   \ `;/
 *                                      _.-`                    /.      `._`/
 *                              _...--'`                        \_`..._
 *                           .'`                         -         `'--:._
 *                        .-`                           \                  `-.
 *                       .                               `-..__.....----...., `.
 *                      '                   `'''---..-''`'              : :  : :
 *                    .` -                '``                           `'   `'
 *                 .-` .` '             .``
 *             _.-` .-`   '            .
 *         _.-` _.-`    .' '         .`
 * (`''--'' _.-`      .'  '        .'
 *  `'----''        .'  .`       .`
 *                .'  .'     .-'`    _____               _    _
 *              .'   :    .-`       |  __ \             | |  | |
 *              `. .`   ,`          | |__) |__ _  _ __  | |_ | |__    ___  _ __
 *               .'   .'            |  ___// _` || '_ \ | __|| '_ \  / _ \| '__|
 *              '   .`              | |   | (_| || | | || |_ | | | ||  __/| |
 *             '  .`                |_|    \__,_||_| |_| \__||_| |_| \___||_|
 *             `  '.
 *             `.___;
 */
package com.ayanix.panther.impl.storage;

import com.ayanix.panther.storage.IDefaultStorage;
import com.ayanix.panther.storage.IYAMLStorage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * Panther - Developed by Lewes D. B.
 * All rights reserved 2017.
 */
public class YAMLStorage implements IYAMLStorage
{

	private String            name;
	private File              file;
	private YamlConfiguration configuration;
	private Plugin            plugin;

	/**
	 * Initiate a Configuration instance.
	 *
	 * @param plugin Plugin storing configuration.
	 * @param name   Name of configuration.
	 */
	public YAMLStorage(Plugin plugin, String name)
	{
		if (plugin == null)
		{
			throw new IllegalArgumentException("Plugin cannot be null");
		}

		if (name == null)
		{
			throw new IllegalArgumentException("File name cannot be null");
		}

		this.plugin = plugin;
		this.name = name;

		this.file = new File(plugin.getDataFolder(), name + ".yml");

		if (!file.exists())
		{
			file.getParentFile().mkdirs();

			try
			{
				if (!file.createNewFile())
				{
					plugin.getLogger().severe("Unable to create file: " + name + ".yml");
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		this.configuration = YamlConfiguration.loadConfiguration(file);
	}

	@Override
	public File getFile()
	{
		return this.file;
	}

	@Override
	public YamlConfiguration getConfig()
	{
		return this.configuration;
	}

	@Override
	public void insertDefault(@NonNull IDefaultStorage defaultStorage)
	{
		if (defaultStorage == null)
		{
			throw new IllegalArgumentException("Default storage cannot be null");
		}

		boolean changes = false;

		for (String key : defaultStorage.getDefaultValues().keySet())
		{
			if (configuration.isSet(key))
			{
				continue;
			}

			configuration.set(key, defaultStorage.getDefaultValues().get(key));
			plugin.getLogger().info("Loaded default value of " + key + " into " + name + ".yml");

			changes = true;
		}

		if (changes)
		{
			save();
		}
	}

	@Override
	public void save()
	{
		try
		{
			configuration.save(file);
		} catch (IOException e)
		{
			plugin.getLogger().severe("Unable to save to file: " + name + ".yml");
		}
	}

}