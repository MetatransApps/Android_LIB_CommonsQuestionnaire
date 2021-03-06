package org.metatrans.commons.questionnaire.menu;


import android.app.Activity;
import android.content.Intent;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.menu.Config_MenuMain_Base;
import org.metatrans.commons.cfg.menu.IConfigurationMenu_Main;
import org.metatrans.commons.cfg.sound.ConfigurationUtils_Sound;
import org.metatrans.commons.cfg.sound.IConfigurationSound;
import org.metatrans.commons.menu.Activity_Menu_Colours_Base;
import org.metatrans.commons.menu.Activity_Menu_Main_Base;
import org.metatrans.commons.questionnaire.R;
import org.metatrans.commons.questionnaire.model.UserSettings;

import java.util.ArrayList;
import java.util.List;


public class Activity_Menu_Main extends Activity_Menu_Main_Base {


	public static int CFG_MENU_SOUND		 			= 13;
	public static int CFG_MENU_RESULT			 		= 16;
	public static int CFG_MENU_ACHIEVEMENTS		 		= 17;


	@Override
	protected List<IConfigurationMenu_Main> getEntries() {


		List<IConfigurationMenu_Main> result = new ArrayList<IConfigurationMenu_Main>();


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.sound;
			}


			@Override
			public int getIconResID() {

				int sound_cfg_id = ((UserSettings)Application_Base.getInstance().getUserSettings()).sound_cfg_id;

				if (sound_cfg_id == IConfigurationSound.CFG_SOUND_ON) {

					return ConfigurationUtils_Sound.getConfigByID(IConfigurationSound.CFG_SOUND_ON).getIconResID();

				} else {

					return ConfigurationUtils_Sound.getConfigByID(IConfigurationSound.CFG_SOUND_OFF).getIconResID();
				}
			}


			@Override
			public int getID() {
				return CFG_MENU_SOUND;
			}

			@Override
			public String getDescription_String() {

				int sound_cfg_id = ((UserSettings)Application_Base.getInstance().getUserSettings()).sound_cfg_id;

				if (sound_cfg_id == IConfigurationSound.CFG_SOUND_ON) {

					return getString(R.string.on);

				} else {

					return getString(R.string.silent);
				}
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						Activity currentActivity = Application_Base.getInstance().getCurrentActivity();

						if (currentActivity != null) {

							currentActivity.finish();

							Intent i = new Intent(currentActivity, Activity_Menu_Sound.class);

							currentActivity.startActivity(i);
						}
					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.results;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_123;
			}

			@Override
			public int getID() {
				return CFG_MENU_RESULT;
			}

			@Override
			public String getDescription_String() {
				return "";
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						int modeID = Application_Base.getInstance().getUserSettings().modeID;

						Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().openLeaderboard_LocalOnly(modeID);

						Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().openLeaderboard(modeID);
					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.achievements;
			}

			@Override
			public int getIconResID() {
				return org.metatrans.commons.R.drawable.ic_cup;
			}

			@Override
			public int getID() {
				return Activity_Menu_Main.CFG_MENU_ACHIEVEMENTS;
			}

			@Override
			public String getDescription_String() {
				return "";
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						Application_Base.getInstance().getEngagementProvider().getAchievementsProvider().openAchievements();
					}
				};
			}
		});


		List<IConfigurationMenu_Main> entries = super.getEntries();


		result.addAll(entries);


		return result;
	}
}
