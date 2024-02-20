package org.metatrans.commons.questionnaire.menu;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.menu.Config_MenuMain_Base;
import org.metatrans.commons.cfg.menu.Config_MenuMain_Melody;
import org.metatrans.commons.cfg.menu.IConfigurationMenu_Main;
import org.metatrans.commons.cfg.sound.Config_MenuMain_Sound;
import org.metatrans.commons.menu.Activity_Menu_Main_Base;
import org.metatrans.commons.questionnaire.R;

import java.util.ArrayList;
import java.util.List;


public class Activity_Menu_Main extends Activity_Menu_Main_Base {


	public static int CFG_MENU_SOUND		 			= 13;
	public static int CFG_MENU_RESULT			 		= 16;
	public static int CFG_MENU_ACHIEVEMENTS		 		= 17;

	public static int CFG_MENU_STOP_ADS		 			= 137;


	@Override
	protected List<IConfigurationMenu_Main> getEntries() {


		List<IConfigurationMenu_Main> result = new ArrayList<IConfigurationMenu_Main>();


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


		if (getRewardedVideoName() != null) {
			result.add(new Config_MenuMain_Base() {

				@Override
				public int getName() {
					return R.string.new_stopads_title;
				}

				@Override
				public int getIconResID() {
					return R.drawable.ic_action_tv;
				}

				@Override
				public int getID() {
					return CFG_MENU_STOP_ADS;
				}

				@Override
				public String getDescription_String() {
					return getString(R.string.new_stopads_desc);
				}

				@Override
				public Runnable getAction() {

					return new Runnable() {

						@Override
						public void run() {

							Activity_Menu_Main.this.openRewardedVideo();
						}
					};
				}
			});
		}


		result.add(new Config_MenuMain_Sound());


		if (Application_Base.getInstance().supportMelodies()) {

			result.add(new Config_MenuMain_Melody());
		}


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
