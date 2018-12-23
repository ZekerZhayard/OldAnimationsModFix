package io.github.zekerzhayard.unresponsivefix;

import java.awt.Color;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import com.spiderfrog.oldanimations.OldAnimations;
import com.spiderfrog.oldanimations.cosmetic.Cosmetic;
import com.spiderfrog.oldanimations.cosmetic.CosmeticManager;
import com.spiderfrog.oldanimations.cosmetic.LoadCosmeticData;
import com.spiderfrog.oldanimations.utils.AnimatedInfo;

import net.minecraft.util.EnumChatFormatting;

public class LoadCosmeticDataHandler {
	public static void onLoadCosmetics(LoadCosmeticData lcd) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field hostField = LoadCosmeticData.class.getDeclaredField("host");
		Field protField = LoadCosmeticData.class.getDeclaredField("port");
		Field databaseField = LoadCosmeticData.class.getDeclaredField("database");
		Field usernameField = LoadCosmeticData.class.getDeclaredField("username");
		Field passwordField = LoadCosmeticData.class.getDeclaredField("password");
		Field connectionField = LoadCosmeticData.class.getDeclaredField("connection");
		hostField.setAccessible(true);
		protField.setAccessible(true);
		databaseField.setAccessible(true);
		usernameField.setAccessible(true);
		passwordField.setAccessible(true);
		connectionField.setAccessible(true);
		new Thread(() -> {
			try {
				hostField.set(lcd, "host.oldanimationsmod.net");
				protField.set(lcd, 3306);
				databaseField.set(lcd, "spiderfrog");
				usernameField.set(lcd, "modac");
				passwordField.set(lcd, "GIsT555PGoFdHYat");
				String info = "lcd user can only read :P";
				Connection connection = null;
				try {
					CosmeticManager.getCosmetics().clear();
					lcd.openConnection();
					connection = (Connection) connectionField.get(lcd);
					Statement statement = connection.createStatement();
					ResultSet result = statement.executeQuery("SELECT * FROM `OldAnimationsMod` WHERE enabled = 1");
					OldAnimations.sendMessage("Load Cosmetic Data");
					while (result.next()) {
						UUID uuid = null;
						try {
							uuid = UUID.fromString(result.getString("user_id"));
						} catch (IllegalArgumentException e) {
							OldAnimations.sendMessage("Skip invalid UUID");
							e.printStackTrace();
							uuid = null;
						}
						int type = result.getInt("cosmetic_id");
						String data = result.getString("data");
						String extra = result.getString("extra");
						ArrayList<Cosmetic> list = new ArrayList<Cosmetic>();
						if (uuid != null) {
							if (CosmeticManager.getCosmetics().containsKey(uuid)) {
								list.addAll(CosmeticManager.getCosmetics().get(uuid));
							}
							if (data.equalsIgnoreCase("null")) {
								list.add(new Cosmetic(type, (String) null, extra));
							} else {
								list.add(new Cosmetic(type, data, extra));
							}
							CosmeticManager.getCosmetics().put(uuid, list);
						}
					}
					AnimatedInfo.queueInfoMessage(EnumChatFormatting.GREEN + "Reloaded Cosmetics!", "Successfully reloaded Cosmetic data.", Color.gray);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					OldAnimations.sendError("Couldn't find SQL Driver");
					AnimatedInfo.queueInfoMessage(EnumChatFormatting.DARK_RED + "No Driver found!", EnumChatFormatting.GRAY + "Please Reinstall the OldAnimationsMod!", Color.red);
				} catch (SQLException e) {
					e.printStackTrace();
					OldAnimations.sendError("Failed to load Cosmetic data");
					AnimatedInfo.queueInfoMessage(EnumChatFormatting.DARK_RED + "Failed!", EnumChatFormatting.GRAY + "Can't connect to Database.", Color.red);
				}
				try {
					connection.close();
					OldAnimations.sendMessage("Close connection to database");
				} catch (Exception e) {
					OldAnimations.sendError("Failed to close connection to database");
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}).start();
	}
}
