/*
	Copyright 2015-2020 Rivoreo

	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE
	FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
	CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package rivoreo.minecraft.plugin.boomarrow;

import rivoreo.config.JavaPropertiesConfiguration;
import rivoreo.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BukkitPlugin extends JavaPlugin implements Listener {
	private Logger log;
	private PluginManager pm;
	private int explosion_power;
	private boolean explosion_with_fire;

	@EventHandler
	public void on_projectile_hit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		//System.err.println(projectile);
		if(projectile.getType() != EntityType.ARROW) return;
		Location location = projectile.getLocation();
		projectile.getWorld().createExplosion(location, explosion_power, explosion_with_fire);
		projectile.remove();
	}

	@Override
	public void onLoad() {
		log = getLogger();
		pm = getServer().getPluginManager();
		File data_dir = getDataFolder();
		data_dir.mkdir();
		File config_file = new File(data_dir, "config");
		Configuration config;
		try {
			config = JavaPropertiesConfiguration.create_from_file(config_file);
		} catch(IOException e) {
			throw new RuntimeException("Failed to create or load configuration file " + config_file.toString(), e);
		}
		explosion_power = config.get_int("ExplosionPower", 4);
		explosion_with_fire = config.get_boolean("ExplosionWithFire", false);
		try {
			config.save();
		} catch(IOException e) {
			log.log(Level.WARNING, "Failed to write to configuration file " + config_file.toString(), e);
		}
	}

	@Override
	public void onEnable() {
		pm.registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Listener)this);
	}
}
