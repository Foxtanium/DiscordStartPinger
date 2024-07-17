package com.github.fluffyfoxuwu.startstoppinger;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class StartStopPinger extends Plugin {
  YamlConfiguration provider =  (YamlConfiguration) ConfigurationProvider.getProvider(YamlConfiguration.class);
  Configuration config;
  File configFile = new File(this.getDataFolder(), "config.yml");
  DiscordWebhook webhook;
  
  @Override
  public void onEnable() {
    this.getLogger().info("Hello World UwU");
    try {
      config = provider.load(configFile);
    } catch (IOException e) {
      throw new RuntimeException("Cant load config", e);
    }
    
    webhook = new DiscordWebhook(config.getString("webhookURL"));
    webhook.setUsername(config.getString("username"));
    webhook.setAvatarUrl(config.getString("avatarURL"));
    webhook.setContent(config.getString("startMessage"));
    try {
      webhook.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void onDisable() {
    webhook.setUsername(config.getString("username"));
    webhook.setAvatarUrl(config.getString("avatarURL"));
    webhook.setContent(config.getString("stopMessage"));
    
    try {
      webhook.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    if (config != null) {
      try {
        provider.save(config, configFile);
      } catch (IOException e) {
        throw new RuntimeException("Cant save config", e);
      }
    }
  }
}
