package Base;

import applet.Main;
import Engine.Adler32;
import Engine.Camera;
import Engine.MystFont;
import Engine.Mystery;
import MenuPck.GUIDecorator;
import MenuPck.GUIHandler;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;
import MenuPck.ListTypes.OtherMenuList;
import desktop.PlatformRuntime;
import Moduls.PlayerGameState;
import Moduls.Skill;
import Moduls.StrategAnimGroup;
import Moduls.StrategAnimSprite;
import Moduls.StrategGraphic;
import Moduls.StrategProgramAnim;
import Moduls.StrategRace;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.TownAnimationFrames;
import Moduls.AuraType;
import Base.Black;
import Moduls.Body;
import Moduls.Decor;
import Moduls.Enemy;
import Moduls.EnemysMap;
import Moduls.Head;
import Moduls.ImageLevelAreaDesc;
import Moduls.InventoryItem;
import Moduls.Legionery;
import Moduls.MapBuffer;
import Moduls.chat.Chat;
import Moduls.indirectevents.HeroEventsParser;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestsOnMap;
import Moduls.strategevents.StrategEventsParser;
import Resources.StringResources;
import battlepck.Battle;
import battlepck.BattleUnit;
import battlepck.client.BattleTh;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import desktop.io.HttpConnectionAdapter;
import desktop.io.SocketConnection;
import desktop.ui.Display;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;
import desktop.storage.RecordStore;
import desktop.storage.RecordStoreException;
import desktop.storage.RecordStoreNotFoundException;

public final class Com {
   private static int[] c_net_ports_main = new int[]{5190, 5050, 50000, 10000, 2000};
   private static int[] c_net_ports_second = new int[]{5193};
   private static RecordStore connRS;
   public static boolean isAndroid;
   public static boolean drawCover = true;
   public static boolean safeBuffersDisable = false;
   private static int monstersLevel = 0;
   private static int animSpritesLevel = 0;
   private static int decorsLevel = 0;
   private static int strategsLevel = 0;
   private static int tilesLevel = 0;
   private static int animationLevel = 0;
   private static boolean drawAuras = true;
   public static boolean isEverythingBad = true;
   public static int touchControlType = 1;
   private static int menuLineAddingHeight = 0;
   private static int softLineAddingHeight = 0;
   public static int dumpWorkType = 1;
   private static boolean dumping = true;
   public static boolean settingsChanged = false;
   private static boolean drawMiniMap = true;
   public static boolean autoGameEnter = true;
   public static String savedPassword = null;
   public static Display display;
   public static int PacketID = 0;
   public static int serverProtocolVersion = 1;
   public static SocketConnection socketConnection;
   private static InputStream net_pdis;
   private static OutputStream net_os;
   private static int lastValidPort = -1;
   public static long lastConnectTime = 0L;
   public static MainTh mainTh;
   public static TextWrite textWrite = new TextWrite();
   public static Game GameTh;
   public static BattleTh battleTh;
   public static Cover CoverTh;
   public static TrackBar PrBarTh;
   public static Black BlackTh = new Black();
   public static Tile[] Tiles;
   public static Decor[] Decors;
   public static int[] triangleDecors;
   public static int curSaveCode = 0;
   public static int decorMaxLeft = 0;
   public static int decorMaxRight = 0;
   public static int decorMaxTop = 0;
   public static int decorMaxBottom = 0;
   public static Head[] Heads;
   public static Body[] Bodys;
   public static AuraType[] AurasTypes;
   public static MystFont fontSmall;
   public static MystFont fontBig;
   public static MystFont fontDigitGame;
   public static MystFont fontDigitGameRed;
   public static MystFont fontClan;
   public static MystFont fontRed;
   public static MystFont fontGreen;
   private static int lastCantLoginCode = 0;
   private static String loginInfo = "";
   private static long lastLoginLaterTime = 0L;
   private static String lastLoginEmail = null;
   public static boolean dontFightAndAction = false;
   public static boolean skipGameLoop = false;
   public static TileMap currentGameMap = new TileMap();
   public static TileMap currentBattleMap = new TileMap();
   public static EnemysMap EnsMap = new EnemysMap();
   public static MapBuffer MB = new MapBuffer();
   public static Camera cameraScreen = new Camera(0, 0, 200, 250, 0, 0, 1, 1, 700);
   public static Camera cameraWorld = new Camera(0, 0, 200, 250, 0, 0, 1, 1, 700);
   public static Camera cameraBattle = new Camera(0, 0, 200, 250, 0, 0, 1, 1, 700);
   public static int playersInChat;
   public static int playersInClanChat = 0;
   public static int nearbyPlayerCount = 0;
   public static boolean cht_Moder;
   public static int playersInGame = 0;
   public static int petOffHead = 0;
   public static int petOffBody = 0;
   private static Vector enteredNames = new Vector();
   public static boolean flgs_GameRuned;
   public static boolean flgs_MidletStart = false;
   private static int ScreenFullW;
   private static int ScreenFullH;
   private static int ScreenCurW;
   private static int ScreenCurH;
   public static int chatFontSize;
   public static FeastOfWar midlet;
   private static boolean firstStartGameMap = true;
   public static boolean firstMapLoadPending;
   private static long lagShowTimeLastSecTime = 0L;
   public static long lagShowTime = 100L;
   private static long lagShowTimeCurMax = 0L;
   public static long lagTimeLast = 0L;
   private static long lagTime = 0L;
   public static int newg_sprite = 0;
   public static String newg_name = "";
   public static int newg_pol = 0;
   public static String newg_password = "";
   public static String newg_contact = "";
   public static int c_fast_fight_party_join_for_client_pixels_x = 36;
   public static int c_fast_fight_party_join_for_client_pixels_y = 36;
   public static int c_trading_items_max_count = 5;
   public static long c_trading_expires_time = 600000L;
   public static int c_world_map_cur_pos_x = 0;
   public static int c_world_map_cur_pos_y = 0;
   public static String c_world_map_cur_loc_info = "";
   public static int c_world_map_zero_pos_x = 137;
   public static int c_world_map_zero_pos_y = 46;
   public static int c_world_map_step_x = 11;
   public static int c_world_map_step_y = 11;
   public static GameImage c_world_map_image = null;
   private static String masterPlayer = "";
   private static String string2 = "";
   public static String string3 = "";
   public static boolean afterFatalLoadClient = false;
   private static String toOtherClientURL = "c.ll.2.7.3";
   public static int c_font_width = 8;
   public static int c_quests_max_count = 5;
   public static String c_share_vk_image_url = "";
   private static int c_maps_cach_data_version = 0;
   public static int c_name_min_len = 3;
   public static int c_name_max_len = 15;
   public static int c_pass_min_len = 3;
   public static int c_pass_max_len = 15;
   public static int c_serv_skills = 30;
   public static int c_serv_gold_add = 300;
   public static int c_serv_class_cost = 15;
   public static int c_pet_rename_cost = 1;
   public static int c_clan_max_description_len = 200;
   public static int c_clan_create_lvl = 1;
   public static int c_clan_join_lvl = 1;
   public static int c_clan_create_cost = 25;
   public static int c_clan_join_cost = 3;
   public static int c_trade_offer_tax_clan = 12;
   public static int c_clan_teleport_cost = 5;
   public static int c_clan_give_cost = 300;
   public static int c_clan_give_add = 7;
   public static long clanLastCheckArenaTime;
   public static int c_player_block_timeout = 300;
   public static int c_enemy_levels_dif_weak = -10;
   public static int c_enemy_levels_dif_strong = 10;
   public static int c_base_bag_size = 50;
   public static int c_trade_to_gos_cost_procent = 10;
   public static int c_trade_offer_tax = 25;
   public static int[] c_items_get_on_count;
   public static int c_pet_off_base_y = 458;
   public static int c_pet_off_x_up = 5;
   public static int c_master_avail_max_lvl = -5;
   public static int c_pet_off_base_x = 5;
   public static int c_photo_max_size = 250000;
   public static int c_battle_action_random_max = 500;
   public static int c_battle_action_random_add = 1500;
   public static int c_photo_max_number_for_player = 20;
   public static long c_protection_time_for_one_pay = 86400000L;
   public static long c_vip_time_for_one_pay = 1800000L;
   public static int[] c_vip_cost = new int[]{10};
   public static String[] c_vip_exp_coof = new String[0];
   public static String[] c_vip_money_coof = new String[0];
   private static String[] c_vip_drop_coof = new String[0];
   public static int c_protection_cost = 12;
   public static int c_inventory_cost = 20;
   public static int c_gender_choo_cost = 50;
   public static int c_party_fight_tiles = 5;
   public static int[] c_items_upgrade_cost_money = new int[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
   public static int[] c_items_upgrade_chance = new int[]{99, 85, 65, 28, 24, 20, 18, 15, 12, 9, 9, 9, 6, 3, 1, 1, 1, 1};
   public static int[] c_items_upgrade_cost_chip = new int[]{5};
   public static String c_items_upgrade_chip_item_symb = "";
   public static long c_items_upgrade_chip_item_id = 0L;
   public static boolean c_items_upgrade_chip_item_on_market = false;
   public static int[] c_items_upgrade_power_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int[] c_items_pet_chance_success = new int[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
   public static int[] c_items_pet_cost_chip = new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
   public static int[] c_items_pet_cost_money = new int[]{1000};
   public static int[] c_items_pet_speed_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_speed_chip_item_id = 738;
   public static boolean c_items_pet_speed_chip_item_on_market = false;
   public static String c_items_pet_speed_chip_item_symb = "";
   public static int[] c_items_pet_pull_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_pull_chip_item_id = 738;
   public static boolean c_items_pet_pull_chip_item_on_market = false;
   public static String c_items_pet_pull_chip_item_symb = "";
   public static int[] c_items_pet_friends_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_friends_chip_item_id = 738;
   public static boolean c_items_pet_friends_chip_item_on_market = false;
   public static String c_items_pet_friends_chip_item_symb = "";
   public static int[] c_items_pet_answer_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_answer_chip_item_id = 738;
   public static boolean c_items_pet_answer_chip_item_on_market = false;
   public static String c_items_pet_answer_chip_item_symb = "";
   public static int[] c_items_pet_paralize_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_paralize_chip_item_id = 738;
   public static boolean c_items_pet_paralize_chip_item_on_market = false;
   public static String c_items_pet_paralize_chip_item_symb = "";
   public static int[] c_items_pet_poison_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_poison_chip_item_id = 738;
   public static boolean c_items_pet_poison_chip_item_on_market = false;
   public static String c_items_pet_poison_chip_item_symb = "";
   public static int[] c_items_pet_hit_dist_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_hit_dist_chip_item_id = 738;
   public static boolean c_items_pet_hit_dist_chip_item_on_market = false;
   public static String c_items_pet_hit_dist_chip_item_symb = "";
   public static int[] c_items_pet_health_inc = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
   public static int c_items_pet_health_chip_item_id = 738;
   public static boolean c_items_pet_health_chip_item_on_market = false;
   public static String c_items_pet_health_chip_item_symb = "";
   private static boolean fgDataPresent = false;
   private static String fgDataString = "";
   public static int c_clan_treasure_max_percents_per_day = 10;
   public static int c_market_min_cost_percent = 1;
   public static int c_market_max_cost_percent = 1000;
   public static int c_market_place_cost_percent = 10;
   private static byte[] rint = new byte[4];
   public static long sentBytes = 0L;
   public static long getBytes = 0L;
   public static long lastSendRequestTime = System.currentTimeMillis();
   public static boolean isActiveRequest = false;
   private static final Object sendLock = new Object();

   public static void initSystemGUI() {
      if (Chat.chatWindow.form == null) {
         System.out.println("ChatWindow fail initSystemGUI");
      }

      textWrite.initSystemGUI();
   }

   public static void onQuestsChanged(boolean mainThread) {
      if (!GameCommons.instance.loadMap) {
         if (currentGameMap != null && Strategist.instance.curMapId != null && Strategist.instance.curMapId.equals(currentGameMap.nameId)) {
            QuestStrateg instance2 = QuestStrateg.instance;
            Strategist instance3 = Strategist.instance;
            TileMap currentGameMap2 = currentGameMap;

            label112:
            for(int questerIndex = 0; questerIndex < currentGameMap2.questers.length; ++questerIndex) {
               QuestsOnMap questsOnMap = currentGameMap2.questers[questerIndex];
               Strategist strategist2 = instance3;
               QuestsOnMap questsOnMap2 = questsOnMap;
               questsOnMap.drawSignType = 0;
               questsOnMap2.questsStateCach = 0;
               QuestInPlayer questInPlayer;
               Vector vector3;
               if ((vector3 = QuestsOnMap.getQuestsInProgress(currentGameMap2, questerIndex, instance2)).size() > 0) {
                  for(int sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
                     if ((questInPlayer = (QuestInPlayer)vector3.elementAt(sizeIndex)).questSingle.backToOtherQuester.length() == 0 && questInPlayer.questSingle.isDone(strategist2, questInPlayer)) {
                        if (!questsOnMap2.noIndication) {
                           questsOnMap2.drawSignType = 2;
                        }

                        questsOnMap2.questsStateCach = 2;
                        continue label112;
                     }
                  }
               }

               String messageText;
               Vector vector4;
               Vector vector5;
               if ((messageText = questsOnMap2.nameId).length() == 0) {
                  vector5 = null;
               } else {
                  Vector vector6 = new Vector();
                  vector4 = instance2.quests;

                  for(int sizeIndex2 = 0; sizeIndex2 < vector4.size(); ++sizeIndex2) {
                     QuestInPlayer questInPlayer2;
                     if ((questInPlayer2 = (QuestInPlayer)vector4.elementAt(sizeIndex2)).questSingle.backToOtherQuester.length() > 0 && messageText.equals(questInPlayer2.questSingle.backToOtherQuester)) {
                        vector6.addElement(questInPlayer2);
                     }
                  }

                  vector5 = vector6;
               }

               vector4 = vector5;
               if (vector5 != null && vector4.size() > 0) {
                  for(int sizeIndex3 = 0; sizeIndex3 < vector4.size(); ++sizeIndex3) {
                     if ((questInPlayer = (QuestInPlayer)vector4.elementAt(sizeIndex3)).questSingle.isDone(strategist2, questInPlayer)) {
                        if (!questsOnMap2.noIndication) {
                           questsOnMap2.drawSignType = 2;
                        }

                        questsOnMap2.questsStateCach = 2;
                        continue label112;
                     }
                  }
               }

               if (questsOnMap2.getQuestsForTake(strategist2, instance2).size() > 0) {
                  if (!questsOnMap2.noIndication) {
                     questsOnMap2.drawSignType = 4;
                  }

                  questsOnMap2.questsStateCach = 4;
               } else if (vector3.size() > 0) {
                  if (!questsOnMap2.noIndication) {
                     questsOnMap2.drawSignType = 3;
                  }

                  questsOnMap2.questsStateCach = 3;
               } else if (questsOnMap2.getQuestsWillCanTakeCount(strategist2, instance2) > 0) {
                  if (!questsOnMap2.noIndication) {
                     questsOnMap2.drawSignType = 1;
                  }

                  questsOnMap2.questsStateCach = 1;
               } else {
                  if (!questsOnMap2.noIndication) {
                     questsOnMap2.drawSignType = 0;
                  }

                  questsOnMap2.questsStateCach = 0;
               }
            }

            if (mainThread) {
               QuestStrateg.instance.calculateWays(currentGameMap, EnsMap, Strategist.instance);
               return;
            }

            QuestStrateg.instance.needCalculateWays();
         }

      }
   }

   public static void onStrategChanged(boolean mainThread) {
      onQuestsChanged(mainThread);
   }

   public static int findValueIndex(int[] integers, int searchedValue) {
      if (searchedValue < 0) {
         searchedValue = 0;
      }

      if (searchedValue > integers.length - 1) {
         searchedValue = integers.length - 1;
      }

      return integers[searchedValue];
   }

   public static void showDefaultCursor() {
      Main.showDefaultCursor();
   }

   public static void showGameCursor() {
      Main.showGameCursor();
   }

   public static String getThreadStack(Thread thread2) {
      try {
         StackTraceElement[] st = thread2.getStackTrace();
         String txt = "";

         for(int i = 0; i < st.length; ++i) {
            txt = txt + st[i].toString() + "\n";
         }

         return txt;
      } catch (Exception exception) {
         return exception.toString();
      }
   }

   private static String getAllThreadsStack() {
      Iterator iterator = Thread.getAllStackTraces().keySet().iterator();

      String allThreadsStack;
      Thread thread2;
      String threadStack;
      for(allThreadsStack = ""; iterator.hasNext(); allThreadsStack = allThreadsStack + thread2.getName() + "\n" + threadStack) {
         threadStack = getThreadStack(thread2 = (Thread)iterator.next());
      }

      return allThreadsStack;
   }

   private static String getStackTrace(Throwable throwable) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      return stringWriter.getBuffer().toString();
   }

   private static void loadAuraTypes(DataInputStream dataInputStream, boolean draw) {
      try {
         Com.mayThrowIOException();
      AurasTypes = new AuraType[dataInputStream.readInt()];
      int i;
      if (draw) {
         for(i = 0; i < AurasTypes.length; ++i) {
            AurasTypes[i] = new AuraType(dataInputStream, true);
         }

      } else {
         for(i = 0; i < AurasTypes.length; ++i) {
            AurasTypes[i] = new AuraType(dataInputStream, false);
         }

      }
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static NetRequest smallReqWithMap(int packId, int add, boolean saveBytes) {
      try {
         Com.mayThrowIOException();
      NetRequest netRequest;
      (netRequest = new NetRequest(saveBytes)).writeHeader(1200000);
      netRequest.request.writeInt(add);
      netRequest.request.writeUTF(Strategist.instance.curMapId);
      sendRequest(netRequest, true);
      netRequest.response.readInt();
      return netRequest;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static NetRequest smallReq(int packId, int add, boolean saveBytes) {
      try {
      NetRequest netRequest;
      (netRequest = new NetRequest(saveBytes)).writeHeader(1000000);
      netRequest.request.writeInt(add);
      sendRequest(netRequest, true);
      netRequest.response.readInt();
      return netRequest;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static NetRequest smallReq(int packId, int add, int add2, boolean saveBytes) {
      try {
      NetRequest netRequest;
      (netRequest = new NetRequest(saveBytes)).writeHeader(1000000);
      netRequest.request.writeInt(add);
      netRequest.request.writeInt(add2);
      sendRequest(netRequest, true);
      netRequest.response.readInt();
      return netRequest;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static NetRequest smallReq(int packId, int add, int[] adds, boolean saveBytes) {
      try {
      NetRequest netRequest;
      (netRequest = new NetRequest(saveBytes)).writeHeader(1000000);
      netRequest.request.writeInt(add);

      for(add = 0; add < adds.length; ++add) {
         netRequest.request.writeInt(adds[add]);
      }

      sendRequest(netRequest, true);
      netRequest.response.readInt();
      return netRequest;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static NetRequest smallReq(int packId, int add, int add2, short add3, boolean saveBytes) {
      try {
      NetRequest netRequest;
      (netRequest = new NetRequest(saveBytes)).writeHeader(1000000);
      netRequest.request.writeInt(971);
      netRequest.request.writeInt(add2);
      netRequest.request.writeShort(add3);
      sendRequest(netRequest, true);
      netRequest.response.readInt();
      return netRequest;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static String getSendID() {
      return FeastOfWar.platform + "; " + FeastOfWar.profiles + "; " + FeastOfWar.configuration + "; " + Integer.toString(cameraScreen.getCw()) + "x" + Integer.toString(cameraScreen.getCh());
   }

   public static void applyRewardEvents(NetRequest netRequest) {
      boolean unusedProgressMarker = false;
      int GP_Level2 = Strategist.instance.GP_Level;
      int GP_FireLevel2 = Strategist.instance.GP_FireLevel;
      int GP_IceLevel2 = Strategist.instance.GP_IceLevel;
      int GP_HeroLevel2 = Strategist.instance.GP_HeroLevel;
      int GP_Money2 = Strategist.instance.GP_Money;
      int GP_Prisoners2 = Strategist.instance.GP_Prisoners;
      int GP_Exp2 = Strategist.instance.GP_Exp;
      int GP_Life2 = Strategist.instance.GP_Life;
      byte vipStatus2 = Strategist.instance.vipStatus;
      boolean protectionStatus2 = Strategist.instance.protectionStatus;
      int previousItemCount = 0;

      for(int sizeIndex = 0; sizeIndex < Strategist.instance.items.size(); ++sizeIndex) {
         previousItemCount += ((InventoryItem)Strategist.instance.items.elementAt(sizeIndex)).count;
      }

      boolean unusedProgressMarker2 = false;
      IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
      if (GP_Level2 != Strategist.instance.GP_Level) {
         GP_Exp2 = 0;
         GameCommons.instance.showFloatText((String)"Ты получил\rуровень!", 3000, 0);
         unusedProgressMarker2 = true;
      }

      if (vipStatus2 != Strategist.instance.vipStatus) {
         if (Strategist.instance.vipStatus >= 0) {
            GameCommons.instance.showFloatText((String)"Героизм\rдействует", 3000, 0);
         } else {
            GameCommons.instance.showFloatText((String)"Героизм\rзакончился", 3000, 0);
         }

         unusedProgressMarker2 = true;
      }

      if (protectionStatus2 != Strategist.instance.protectionStatus) {
         if (Strategist.instance.protectionStatus) {
            GameCommons.instance.showFloatText((String)"Ты теперь\rв безопасности!", 3000, 0);
         } else {
            GameCommons.instance.showFloatText((String)"Ты больше не\rв безопасности!", 3000, 0);
         }
      }

      BattleTh.resultBattleClear();
      BattleTh.res_ChangeFire = Strategist.instance.GP_FireLevel - GP_FireLevel2;
      BattleTh.res_ChangeIce = Strategist.instance.GP_IceLevel - GP_IceLevel2;
      BattleTh.res_ChangeHero = Strategist.instance.GP_HeroLevel - GP_HeroLevel2;
      BattleTh.res_WinPrisons = Strategist.instance.GP_Money - GP_Money2;
      BattleTh.res_WinPrisoners = Strategist.instance.GP_Prisoners - GP_Prisoners2;
      BattleTh.res_WinMoney = Strategist.instance.GP_Exp - GP_Exp2;
      BattleTh.res_ChangeLife = Strategist.instance.GP_Life - GP_Life2;
      int currentItemCount = 0;

      for(GP_Level2 = 0; GP_Level2 < Strategist.instance.items.size(); ++GP_Level2) {
         currentItemCount += ((InventoryItem)Strategist.instance.items.elementAt(GP_Level2)).count;
      }

      BattleTh.res_WinItem = currentItemCount - previousItemCount;
      GameTh.trophyStart(0, BattleTh.res_WinPrisons, BattleTh.res_WinMoney, BattleTh.res_WinItem, BattleTh.res_ChangeFire, BattleTh.res_ChangeIce, BattleTh.res_ChangeHero, 0);
   }

   public static void startOnPlayerClick(int id, int backMod) {
      try {
      PrBarTh.Set(true);
      NetRequest netRequest;
      (netRequest = new NetRequest()).writeHeader(4630000);
      netRequest.request.writeInt(id);
      sendRequest(netRequest, true);
      if ((PacketID = netRequest.response.readInt()) == 4630001) {
         MenuSupport.instance.otherPlayerMenuBack = backMod;
         MenuSupport.instance.otherPlayerMenuPlayer.id = id;
         MenuSupport.instance.otherPlayerMenuPlayer.name = netRequest.response.readUTF();
         MenuSupport.instance.otherPlayerMenuPlayer.aboutMe = netRequest.response.readUTF();
         MenuSupport.instance.otherPlayerMenuPlayer.strategGraphic = new StrategGraphic(netRequest.response);
         MenuSupport.instance.otherPlayerMenuPeace = netRequest.response.readBoolean();
         MenuSupport.instance.otherPlayerMenuPlayer.race = netRequest.response.readByte();
         MenuSupport.instance.otherPlayerMenuPlayer.gender = netRequest.response.readByte();
         id = netRequest.response.readInt();
         MenuSupport.instance.otherPlayerMenuIcons = new String[id];
         MenuSupport.instance.otherPlayerMenuItems = new String[id];
         MenuSupport.instance.otherPlayerMenuItemsInfos = new String[id];
         MenuSupport.instance.otherPlayerMenuItemsCont = new InventoryItem[id];

         for(backMod = 0; backMod < id; ++backMod) {
            MenuSupport.instance.otherPlayerMenuIcons[backMod] = netRequest.response.readUTF();
         }

         for(backMod = 0; backMod < id; ++backMod) {
            MenuSupport.instance.otherPlayerMenuItems[backMod] = netRequest.response.readUTF();
         }

         for(backMod = 0; backMod < id; ++backMod) {
            MenuSupport.instance.otherPlayerMenuItemsInfos[backMod] = netRequest.response.readUTF();
         }

         for(backMod = 0; backMod < id; ++backMod) {
            if (netRequest.response.readBoolean()) {
               InventoryItem inventoryItem2;
               (inventoryItem2 = new InventoryItem()).readAdditional(netRequest.response);
               MenuSupport.instance.otherPlayerMenuItemsCont[backMod] = inventoryItem2;
            } else {
               MenuSupport.instance.otherPlayerMenuItemsCont[backMod] = null;
            }
         }

         MenuSupport.instance.otherPlayerMenuAtackInfo = netRequest.response.readUTF();
         MenuManager.instance.showMenuById(63);
      } else {
         GameCommons.instance.showFloatText((String)"Игрок занят", 4000);
      }

      PrBarTh.Stop();
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void startExitGame() {
      try {
         PrBarTh.Set(true);
         ClientState.instance.set(2);
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1408000);
         sendRequest(netRequest, true);
         PrBarTh.Stop();
         if (netRequest.response.readInt() != 1408001) {
            ClientState.instance.revert();
            return;
         }

         makeNewGameExit();
      } catch (Exception exception) {
      }

   }

   public static void makeNewGameExit() {
      ClientState.instance.set(2);
      BlackTh.Set();
      PlatformRuntime.getInstance();
      MenuManager.instance.showMenuById(1);
   }

   public static void loginFromMenu(String withText) {
      boolean loginToServer;
      label97: {
         try {
            PrBarTh.Set(true);
            if (withText != null) {
               PrBarTh.setText(withText);
            }

            loginToServer = loginToServer(true);
            break label97;
         } catch (Exception exception) {
            showFatalScreen("Ошибка связи. Перезапустите приложение и повторите попытку.");
         } finally {
            PrBarTh.Stop();
         }

         return;
      }

      if (loginToServer) {
         addEnterName(newg_name);
         savePlatformSetting(curSaveCode);
      } else {
         switch (lastCantLoginCode) {
            case 2:
               GameCommons.instance.showFloatText("Неверное имя");
               MenuManager.instance.showMenuById(1);
               return;
            case 3:
               GameCommons.instance.showFloatText("Неверный пароль");
               (new OtherMenuList()).showHeroMenu();
               return;
            case 4:
               GameCommons.instance.showFloatText((String)loginInfo, 5000);
               MenuManager.instance.showMenuById(1);
               return;
            case 7:
               OtherMenuList otherMenuList = new OtherMenuList();
               if (lastLoginEmail == null) {
                  otherMenuList.showTextEnter(210, "Герою нужна привязка к почте (email) для дальнейшей игры. Укажите вашу почту, затем проверьте ее: загляните в папку ВХОДЯЩИЕ или СПАМ, получите письмо от игры, и перейдите по ссылке из письма.", "", 1);
                  return;
               }

               otherMenuList.showTextEnter(210, Mystery.stringReplace("На почту % уже было выслано письмо для активации. Загляните в почтовую папку СПАМ. Если письма нет и там, то вы можете указать другую почту, или ввести тот же самый адрес для повторной отправки письма со ссылкой.", '%', lastLoginEmail), "", 1);
               return;
            case 8:
               long time;
               int seconds = (int)((time = lastLoginLaterTime / 1000L) % 60L);
               int minutes = (int)((time /= 60L) % 60L);
               int hours = (int)((time /= 60L) % 24L);
               int days = (int)(time /= 24L);
               String s = "email героя забанен на\n";
               if (days > 0) {
                  s = s + Mystery.stringFormat("% дн % ч % м % с", '%', new String[]{String.valueOf(days), String.valueOf(hours), String.valueOf(minutes), String.valueOf(seconds)});
               } else if (hours > 0) {
                  s = s + Mystery.stringFormat("% ч % м % с", '%', new String[]{String.valueOf(hours), String.valueOf(minutes), String.valueOf(seconds)});
               } else if (minutes > 0) {
                  s = s + Mystery.stringFormat("% м % с", '%', new String[]{String.valueOf(minutes), String.valueOf(seconds)});
               } else {
                  s = s + Mystery.stringFormat("% с", '%', new String[]{String.valueOf(seconds)});
               }

               GameCommons.instance.showFloatText(s);
               MenuManager.instance.showMenuById(1);
            case 5:
            case 6:
            default:
         }
      }
   }

   public static void stopGlobalMap(int enterCode) {
      if (GlobalMapTh.gmTh != null) {
         GlobalMapTh.gmTh.pauseAndWait(enterCode);
      }

   }

   public static void resumeGlobalMap() {
      if (GlobalMapTh.gmTh != null) {
         GlobalMapTh.gmTh.resumeNoWait();
      }

   }

   public static void drawLifeBar(GameGraphics gameGraphics2, int sX, int sY, int dW, int dH, long curProg, long maxProg) {
      drawLifeBar(gameGraphics2, sX, sY, dW, dH, curProg, maxProg, false);
   }

   public static void drawLifeBar(GameGraphics gameGraphics2, int sX, int sY, int dW, int dH, long curProg, long maxProg, boolean backdraw) {
      if (backdraw) {
         gameGraphics2.setColor(0);
         gameGraphics2.fillRect(sX, sY, dW, dH);
      }

      maxProg = Math.max(1L, maxProg);
      int maximum;
      int maximum2;
      if ((curProg = Math.max(0L, Math.min(curProg, maxProg))) > maxProg / 2L) {
         maximum = 255;
         if ((maximum2 = Math.max(0, (int)(-curProg * 255L / maxProg + 255L))) < 0) {
            maximum2 = 0;
         }
      } else {
         maximum2 = 255;
         if ((maximum = Math.max(0, (int)((curProg * 255L << 1) / maxProg))) > 255) {
            maximum = 255;
         }
      }

      gameGraphics2.setColor(maximum2, maximum, 0);
      gameGraphics2.fillRect(sX, sY, Math.max(0, (int)(1L + (long)(dW - 1) * curProg / maxProg)), dH);
   }

   private static boolean loadMapStartData() {
      boolean cacheAvailable = false;

      try {
         GameCommons.instance.loadMap = true;
         NetRequest netRequest;
         String nameId = (netRequest = smallReqWithMap(1200000, 1, false)).response.readUTF();
         short mapIndex = netRequest.response.readShort();
         int integerCode = netRequest.response.readInt();
         long timestamp = netRequest.response.readLong();
         BattleTh.resultBattleClear();
         boolean useCachedMap = true;
         if (dumpWorkType == 3) {
            useCachedMap = false;
         }

         boolean cacheInitialized = false;
         String messageText = "";
         boolean mapDataLoaded = false;
         String messageText3 = "";
         if (useCachedMap) {
            Adler32 adler32;
            Adler32 adler322 = adler32 = new Adler32();
            byte[] encodedBytes = nameId.getBytes();
            adler322.updateChecksum(encodedBytes, 0, encodedBytes.length);
            StringBuilder textBuilder = new StringBuilder();
            int serverProtocolVersion2 = serverProtocolVersion;
            boolean unusedProgressMarker = true;
            String[] cachedMap;
            String messageText2;
            if ((cachedMap = Black.listRecordStoresWithPrefix(messageText2 = textBuilder.append("22@").append(String.valueOf(adler32.getValue())).toString())) != null && cachedMap.length > 0) {
               for(int lengthIndex = 0; lengthIndex < cachedMap.length; ++lengthIndex) {
                  if (Integer.parseInt(cachedMap[lengthIndex].substring(cachedMap[lengthIndex].lastIndexOf(64) + 1)) == integerCode) {
                     mapDataLoaded = true;
                     messageText3 = cachedMap[lengthIndex];
                  } else {
                     Black.deleteMapCache(cachedMap[lengthIndex], true);
                  }
               }
            }

            if (mapDataLoaded) {
               byte[] byteValue;
               if ((byteValue = Black.readMapCacheRecord(messageText3, 6)) == null) {
                  mapDataLoaded = false;
                  Black.deleteMapCache(messageText3, true);
               } else {
                  DataInputStream dataInputStream;
                  int cachedMapVersion = (dataInputStream = new DataInputStream(new ByteArrayInputStream(byteValue))).readInt();
                  dataInputStream.close();
                  if (cachedMapVersion != c_maps_cach_data_version) {
                     mapDataLoaded = false;
                     Black.deleteMapCache(messageText3, true);
                  }
               }
            }

            long cachedMapChecksum = 0L;
            if (mapDataLoaded) {
               if (Black.readMapCacheRecord(messageText3, 4) == null) {
                  mapDataLoaded = false;
                  Black.deleteMapCache(messageText3, true);
               } else {
                  Adler32 adler323 = new Adler32();
                  byte[] byteValue2;
                  if ((byteValue2 = Black.readMapCacheRecord(messageText3, 0)) == null) {
                     mapDataLoaded = false;
                     Black.deleteMapCache(messageText3, true);
                  } else {
                     adler323.updateChecksum(byteValue2, 4, byteValue2.length - 4);
                     if ((byteValue2 = Black.readMapCacheRecord(messageText3, 1)) == null) {
                        mapDataLoaded = false;
                        Black.deleteMapCache(messageText3, true);
                     } else {
                        adler323.updateChecksum(byteValue2, 4, byteValue2.length - 4);
                        if ((byteValue2 = Black.readMapCacheRecord(messageText3, 3)) == null) {
                           mapDataLoaded = false;
                           Black.deleteMapCache(messageText3, true);
                        } else {
                           adler323.updateChecksum(byteValue2, 4, byteValue2.length - 4);
                           cachedMapChecksum = adler323.getValue();
                        }
                     }
                  }
               }
            }

            if (mapDataLoaded && cachedMapChecksum != timestamp) {
               mapDataLoaded = false;
            }

            messageText = messageText2 + "@" + integerCode;
            if (mapDataLoaded) {
               cacheAvailable = false;
            } else {
               cacheAvailable = cacheInitialized = Black.initializeMapCache(messageText, 200000);
            }
         }

         long mapPart = 0L;

         int townIndex;
         for(townIndex = 2; townIndex <= 6; ++townIndex) {
            mapPart = loadMapPart(cacheAvailable, cacheInitialized, messageText, mapDataLoaded, messageText3, mapPart, townIndex, townIndex - 2, townIndex - 1);
            if (townIndex - 2 == 4) {
               currentBattleMap.applyVirginFromMap(currentGameMap, true);
            }
         }

         mapPart = loadMapPart(cacheAvailable, cacheInitialized, messageText, mapDataLoaded, messageText3, mapPart, 18, 5, 18);
         if (cacheInitialized) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream;
            (dataOutputStream = new DataOutputStream(byteArrayOutputStream)).writeInt(c_maps_cach_data_version);
            dataOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            Black.appendMapCacheRecord(messageText, bytes, 6);
         }

         System.out.println("economy " + mapPart);
         if (mapDataLoaded) {
            for(townIndex = 8; townIndex <= 8; ++townIndex) {
               netRequest = smallReqWithMap(1200000, townIndex, false);
               currentGameMap.loadFromStream(netRequest, netRequest.response, fontSmall, townIndex - 1);
            }
         }

         netRequest = smallReqWithMap(1200000, 19, false);
         currentGameMap.loadFromStream(netRequest, netRequest.response, fontSmall, 16);
         currentGameMap.loadFromStream(netRequest, netRequest.response, fontSmall, 6);

         for(townIndex = 0; townIndex < currentGameMap.towns.length; ++townIndex) {
            Town town;
            if ((town = currentGameMap.towns[townIndex]).type == 406) {
               currentBattleMap.addDecors(((TownAnimationFrames)town.additional).frames, false);
            }
         }

         netRequest.response.readInt();
         netRequest.response.readUTF();
         netRequest.response.readUTF();
         netRequest.response.readUTF();
         netRequest.response.readUTF();
         netRequest.response.readBoolean();
         c_world_map_cur_pos_x = netRequest.response.readInt();
         c_world_map_cur_pos_y = netRequest.response.readInt();
         c_world_map_cur_loc_info = netRequest.response.readUTF();
         currentGameMap.initAfterLoad();
         if (currentGameMap.home) {
            Strategist.instance.stableAllItems();
         }

         currentGameMap.nameId = nameId;
         currentGameMap.serverIndex = mapIndex;
         currentBattleMap.nameId = nameId;
         currentBattleMap.serverIndex = mapIndex;
      } catch (Exception exception) {
         GameCommons.instance.loadMap = false;
         SendMistace("Кеш неисправен. Удалите игру и поставьте заново. Нажмите НЕТ при вопросе о сохранении старых данных приложения, или сотрите папку temp. Подробнее: www.lastlimit.ru/support/", true, true, exception);
         return false;
      }

      GameCommons.instance.loadMap = false;
      onStrategChanged(true);
      return true;
   }

   private static long loadMapPart(boolean saveBytes, boolean dumpMapToCach, String dumpMapName, boolean mapCached, String chachMapRecordName, long safe, int serverPartInd, int recordStorePartImd, int mapLoadEtapInd) {
      try {
      NetRequest netRequest = new NetRequest();
      if (!mapCached) {
         netRequest = smallReqWithMap(1200000, serverPartInd, saveBytes);
      } else {
         byte[] tmp = Black.readMapCacheRecord(chachMapRecordName, recordStorePartImd);
         safe += (long)tmp.length;
         netRequest.response = new DataInputStream(new ByteArrayInputStream(tmp));
         netRequest.response.readInt();
      }

      currentGameMap.loadFromStream(netRequest, netRequest.response, fontSmall, mapLoadEtapInd);
      if (dumpMapToCach) {
         Black.appendMapCacheRecord(dumpMapName, netRequest.responseBytes, recordStorePartImd);
         netRequest.responseBytes = null;
      }

      return safe;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void sendWhatInBlock() {
      if (!GameTh.sendWhatInBlock) {
         GameTh.sendWhatInBlock = true;
         PrBarTh.Set(true);
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(4100000);
         sendRequest(netRequest, true);
         PrBarTh.Stop();
      }
   }

   public static void startGameMap(boolean justLoading) {
      if (justLoading) {
         PrBarTh.Set(true);
         ClientState.instance.set(3);

         try {
            if (!loadMapStartData()) {
               return;
            }
         } catch (Exception exception) {
            exception.printStackTrace();
            return;
         }

         PrBarTh.setProgress(100);
         cameraWorld.setLimit(-Tile.width, -Tile.height, currentGameMap.widthPx + (Tile.width << 1), currentGameMap.heightPx + (Tile.height << 1));
         GameCommons.instance.redrawMapBuffer();
         ClientState.instance.revert();
         PrBarTh.Stop();
      } else {
         PrBarTh.Set(true);
         ClientState.instance.set(3);
         GameTh.sendWhatInBlock = false;
         lagShowTime = 100L;
         lagShowTimeLastSecTime = Mystery.currentTimeMillis;
         lagShowTimeCurMax = 0L;
         lagTime = 0L;
         lagTimeLast = Mystery.currentTimeMillis;

         try {
            if (!loadMapStartData()) {
               return;
            }
         } catch (Exception exception2) {
            exception2.printStackTrace();
            return;
         }

         PrBarTh.setProgress(100);
         cameraWorld.setLimit(-Tile.width, -Tile.height, currentGameMap.widthPx + (Tile.width << 1), currentGameMap.heightPx + (Tile.height << 1));
         MB.Reset();
         MB.TryRedraw(cameraWorld, currentGameMap, Tiles);
         tryCloseMenuWithNecessaryCheck();
         GameTh.Reset();
         mainTh.setStopPaint();
         MenuSupport.instance.curActiveTownInd = -1;
         GameTh.waitChangeMap = false;
         long currentTimeMillis2 = Mystery.currentTimeMillis;
         GameTh.Set();
         ClientState.instance.set(4);
         if (firstStartGameMap) {
            firstStartGameMap = false;
            ChangeResolution(cameraScreen.getCw(), cameraScreen.getCh(), false);
         }

         try {
            String[] res;
            if ((res = QuestStrateg.instance.checkGetnQuestsForDone(currentGameMap, -1, (QuestsOnMap)null, Strategist.instance, false))[0] == "CANT_GIVE_REWARD" || res[0] == "SUCCESS") {
               (new OtherMenuList()).showMenuMessage(185, res[1]);
            }

            if (res[0] == "SUCCESS_FLOAT") {
               GameCommons.instance.showFloatText(res[1]);
            }
         } catch (Exception exception3) {
            exception3.printStackTrace();
            GameCommons.instance.showFloatText("Ошибка при попытке сдать квест на карте");
         }

         PrBarTh.Stop();
      }
   }

   public static void fixLag() {
      long ppt;
      lagTime = (ppt = Mystery.currentTimeMillis) - lagTimeLast;
      lagTimeLast = ppt;
      if (lagTime > lagShowTimeCurMax) {
         lagShowTimeCurMax = lagTime;
      }

      if (ppt - lagShowTimeLastSecTime >= 5000L) {
         lagShowTimeLastSecTime = ppt;
         lagShowTime = lagShowTimeCurMax;
         lagShowTimeCurMax = 0L;
      }

   }

   public static String getNearbyPlayerCountText() {
      return nearbyPlayerCount > 9 ? "9+" : String.valueOf(nearbyPlayerCount);
   }

   public static void ChangeResolution(int w, int h, boolean justPaint) {
      ScreenFullW = w;
      ScreenFullH = h;
      if (ScreenFullW < 128) {
         ScreenFullW = 128;
      }

      if (ScreenFullW > 10000) {
         ScreenFullW = 10000;
      }

      if (ScreenFullH < 128) {
         ScreenFullH = 128;
      }

      if (ScreenFullH > 10000) {
         ScreenFullH = 10000;
      }

      ScreenCurW = ScreenFullW;
      ScreenCurH = ScreenFullH;
      savePlatformSetting(curSaveCode);
      cameraScreen.setCw(ScreenFullW);
      cameraScreen.setCh(ScreenFullH);
      initCameras();
      if (PlatformRuntime.getInstance().getPlatformType() != 2) {
         if (!isEverythingBad) {
            Mystery.initDoubleBuffer(cameraScreen.getCw(), cameraScreen.getCh());
         } else {
            Mystery.initDoubleBufferLow(cameraScreen.getCw(), cameraScreen.getCh());
         }
      } else {
         Mystery.initDoubleBufferLow(cameraScreen.getCw(), cameraScreen.getCh());
      }

      if (GameTh != null) {
         GameTh.onChangeResolution(cameraScreen.getCw(), cameraScreen.getCh());
      }

      if (battleTh != null) {
         battleTh.onChangeResolution(cameraScreen.getCw(), cameraScreen.getCh());
      }

      MenuManager.instance.onChangeResolution(cameraScreen.getCw(), cameraScreen.getCh(), justPaint);
      GameCommons.instance.onChangeResolution();
   }

   public static void changeFontSize(int fs) {
      chatFontSize = fs;
      savePlatformSetting(curSaveCode);
   }

   private static void addEnterName(String name) {
      boolean b = true;

      for(int i = 0; i < enteredNames.size(); ++i) {
         if (((EnterName)enteredNames.elementAt(i)).name.equals(name)) {
            ((EnterName)enteredNames.elementAt(i)).date = System.currentTimeMillis();
            b = false;
            break;
         }
      }

      if (b) {
         enteredNames.addElement(new EnterName(name));
      }

      savePlatformSetting(curSaveCode);
   }

   public static void savePlatformSetting(int saveCode) {
      SaveMainData(saveCode, PlatformRuntime.getInstance().getPlatformType());
   }

   private static void SaveMainData(int saveCode, int renderType) {
      try {
         curSaveCode = saveCode;
         String messageText = getFGCopyString() + "_main_data";
         boolean createStore = true;
         RecordStore recordStore = getCacheStore(messageText, createStore);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         DataOutputStream dataOutputStream;
         (dataOutputStream = new DataOutputStream(byteArrayOutputStream)).writeInt(30022);
         dataOutputStream.writeInt(lastValidPort);
         dataOutputStream.writeInt(ScreenCurW);
         dataOutputStream.writeInt(ScreenCurH);
         dataOutputStream.writeInt(chatFontSize);
         dataOutputStream.writeUTF(newg_name);
         dataOutputStream.writeInt(saveCode);
         dataOutputStream.writeBoolean(drawMiniMap);
         dataOutputStream.writeBoolean(autoGameEnter);
         if (savedPassword != null) {
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeUTF(savedPassword);
         } else {
            dataOutputStream.writeBoolean(false);
         }

         dataOutputStream.writeByte(strategsLevel);
         dataOutputStream.writeByte(animSpritesLevel);
         dataOutputStream.writeByte(decorsLevel);
         dataOutputStream.writeByte(tilesLevel);
         dataOutputStream.writeByte(monstersLevel);
         dataOutputStream.writeByte(dumpWorkType);
         dataOutputStream.writeBoolean(safeBuffersDisable);
         dataOutputStream.writeBoolean(drawCover);
         dataOutputStream.writeByte(animationLevel);
         dataOutputStream.writeInt(menuLineAddingHeight);
         dataOutputStream.writeInt(enteredNames.size());

         for(saveCode = 0; saveCode < enteredNames.size(); ++saveCode) {
            dataOutputStream.writeUTF(((EnterName)enteredNames.elementAt(saveCode)).name);
            dataOutputStream.writeLong(((EnterName)enteredNames.elementAt(saveCode)).date);
         }

         dataOutputStream.writeInt(softLineAddingHeight);
         dataOutputStream.writeBoolean(drawAuras);
         dataOutputStream.writeBoolean(isEverythingBad);
         dataOutputStream.writeInt(touchControlType);
         dataOutputStream.writeByte(renderType);
         dataOutputStream.flush();
         byte[] res = byteArrayOutputStream.toByteArray();
         if (recordStore.getNumRecords() > 0) {
            recordStore.setRecord(1, res, 0, res.length);
         } else {
            recordStore.addRecord(res, 0, res.length);
         }

         dataOutputStream.close();
         byteArrayOutputStream.close();
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   private static String getFGCopyString() {
      return "NewFOWOL " + toOtherClientURL;
   }

   private static String getRecordStoreForNetResourcesName() {
      return getFGCopyString() + "_net_data" + (serverProtocolVersion >= 9 ? "_2" : "");
   }

   public static RecordStore innerOpenRecordStore(String rName, boolean create) throws RecordStoreException {
      return RecordStore.openRecordStore(rName, create);
   }

   public static void closeRecordStore(RecordStore recordStore) {
   }

   public static DataInputStream connectToRSForNetResources(int rsIndex) {
      try {
      String recordStoreForNetResourcesName = getRecordStoreForNetResourcesName();
      boolean createStore = true;
      if ((connRS = getCacheStore(recordStoreForNetResourcesName, createStore)).getNumRecords() >= rsIndex) {
         byte[] recordData = connRS.getRecord(rsIndex);
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recordData);
         DataInputStream dataInputStream;
         (dataInputStream = new DataInputStream(byteArrayInputStream)).readInt();
         return dataInputStream;
      } else {
         return null;
      }
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void dumpToRSForNetResources(int rsIndex, byte[] bts) {
      String recordStoreForNetResourcesName = getRecordStoreForNetResourcesName();
      boolean createStore = true;
      if ((connRS = getCacheStore(recordStoreForNetResourcesName, createStore)).getNumRecords() >= rsIndex) {
         connRS.setRecord(rsIndex, bts, 0, bts.length);
      } else {
         connRS.addRecord(bts, 0, bts.length);
      }
   }

   private static int[][] loadInt2(NetRequest netRequest) {
      try {
      int[][] res = new int[netRequest.response.readInt()][];

      for(int i = 0; i < res.length; ++i) {
         res[i] = loadInt1(netRequest);
      }

      return res;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static int[] loadInt1(NetRequest netRequest) {
      try {
      int[] res = new int[netRequest.response.readInt()];

      for(int i = 0; i < res.length; ++i) {
         res[i] = netRequest.response.readInt();
      }

      return res;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static void LoadConstants(NetRequest netRequest) {
      try {
         loadInt1(netRequest);
         netRequest.response.readUTF();
         netRequest.response.readBoolean();
         netRequest.response.readUTF();
         netRequest.response.readUTF();
         netRequest.response.readUTF();
         MenuSupport.instance.helpTexts = new String[netRequest.response.readInt()];

         int integerCode;
         for(integerCode = 0; integerCode < MenuSupport.instance.helpTexts.length; ++integerCode) {
            MenuSupport.instance.helpTexts[integerCode] = netRequest.response.readUTF();
         }

         MenuSupport instance2 = MenuSupport.instance;
         instance2.helpMenuList = new String[instance2.helpTexts.length];

         for(integerCode = 0; integerCode < MenuSupport.instance.helpTexts.length; ++integerCode) {
            MenuSupport.instance.helpMenuList[integerCode] = netRequest.response.readUTF();
         }

         instance2 = MenuSupport.instance;
         instance2.helpHintsList = new String[instance2.helpTexts.length];

         for(integerCode = 0; integerCode < MenuSupport.instance.helpTexts.length; ++integerCode) {
            MenuSupport.instance.helpHintsList[integerCode] = netRequest.response.readUTF();
         }

         Enemy.c_AwaySensRadCoof = netRequest.response.readInt();
         Enemy.c_StartBattleDist = netRequest.response.readInt();
         Enemy.c_StartInteresDist = netRequest.response.readInt();
         NetRequest netRequest2 = netRequest;
         int[][][] c_MovPatterns = new int[netRequest.response.readInt()][][];

         for(int lengthIndex = 0; lengthIndex < c_MovPatterns.length; ++lengthIndex) {
            c_MovPatterns[lengthIndex] = loadInt2(netRequest2);
         }

         Enemy.c_MovPatterns = c_MovPatterns;
         Strategist.cd_wi = netRequest.response.readInt();
         Strategist.cd_hi = netRequest.response.readInt();
         netRequest.response.readInt();
         c_serv_skills = netRequest.response.readInt();
         c_serv_gold_add = netRequest.response.readInt();
         netRequest.response.readUTF();
         c_enemy_levels_dif_weak = netRequest.response.readInt();
         c_enemy_levels_dif_strong = netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         c_base_bag_size = netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         c_trade_offer_tax = netRequest.response.readInt();
         c_serv_class_cost = netRequest.response.readInt();
         c_clan_create_lvl = netRequest.response.readInt();
         c_clan_join_lvl = netRequest.response.readInt();
         c_clan_create_cost = netRequest.response.readInt();
         c_clan_join_cost = netRequest.response.readInt();
         c_trade_offer_tax_clan = netRequest.response.readInt();
         c_clan_teleport_cost = netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         c_clan_give_cost = netRequest.response.readInt();
         c_clan_give_add = netRequest.response.readInt();
         c_name_min_len = netRequest.response.readInt();
         c_name_max_len = netRequest.response.readInt();
         c_pass_min_len = netRequest.response.readInt();
         c_pass_max_len = netRequest.response.readInt();
         c_trade_to_gos_cost_procent = netRequest.response.readInt();
         c_player_block_timeout = netRequest.response.readInt();
         c_items_get_on_count = new int[netRequest.response.readInt()];

         for(integerCode = 0; integerCode < c_items_get_on_count.length; ++integerCode) {
            c_items_get_on_count[integerCode] = netRequest.response.readInt();
         }

         c_pet_rename_cost = netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         netRequest.response.readInt();
         c_photo_max_size = netRequest.response.readInt();
         c_photo_max_number_for_player = netRequest.response.readInt();
         netRequest.response.readInt();
         c_battle_action_random_max = netRequest.response.readInt();
         c_battle_action_random_add = netRequest.response.readInt();
         c_vip_cost = new int[netRequest.response.readInt()];

         for(integerCode = 0; integerCode < c_vip_cost.length; ++integerCode) {
            c_vip_cost[integerCode] = netRequest.response.readInt();
         }

         c_protection_cost = netRequest.response.readInt();
         c_inventory_cost = netRequest.response.readInt();
         c_vip_time_for_one_pay = netRequest.response.readLong();
         c_protection_time_for_one_pay = netRequest.response.readLong();
         integerCode = netRequest.response.readInt();

         int integerCode2;
         for(integerCode2 = 0; integerCode2 < integerCode; ++integerCode2) {
            MystFont.COLOR_GROUPS.put(netRequest.response.readUTF(), new int[]{netRequest.response.readInt(), netRequest.response.readInt(), netRequest.response.readInt()});
         }

         netRequest.response.readInt();
         c_gender_choo_cost = netRequest.response.readInt();
         integerCode2 = netRequest.response.readInt();

         for(integerCode = 0; integerCode < integerCode2; ++integerCode) {
            netRequest.response.readUTF();
         }

         c_party_fight_tiles = netRequest.response.readInt();
         c_items_pet_chance_success = loadInt1(netRequest);
         c_items_pet_cost_chip = loadInt1(netRequest);
         c_items_pet_cost_money = loadInt1(netRequest);
         c_items_pet_speed_inc = loadInt1(netRequest);
         c_items_pet_speed_chip_item_id = netRequest.response.readInt();
         c_items_pet_speed_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_pull_inc = loadInt1(netRequest);
         c_items_pet_pull_chip_item_id = netRequest.response.readInt();
         c_items_pet_pull_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_friends_inc = loadInt1(netRequest);
         c_items_pet_friends_chip_item_id = netRequest.response.readInt();
         c_items_pet_friends_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_answer_inc = loadInt1(netRequest);
         c_items_pet_answer_chip_item_id = netRequest.response.readInt();
         c_items_pet_answer_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_paralize_inc = loadInt1(netRequest);
         c_items_pet_paralize_chip_item_id = netRequest.response.readInt();
         c_items_pet_paralize_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_poison_inc = loadInt1(netRequest);
         c_items_pet_poison_chip_item_id = netRequest.response.readInt();
         c_items_pet_poison_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_hit_dist_inc = loadInt1(netRequest);
         c_items_pet_hit_dist_chip_item_id = netRequest.response.readInt();
         c_items_pet_hit_dist_chip_item_symb = netRequest.response.readUTF();
         c_items_pet_health_inc = loadInt1(netRequest);
         c_items_pet_health_chip_item_id = netRequest.response.readInt();
         c_items_pet_health_chip_item_symb = netRequest.response.readUTF();
         c_items_upgrade_chip_item_symb = netRequest.response.readUTF();
         c_items_upgrade_chip_item_id = netRequest.response.readLong();
         c_items_upgrade_cost_chip = loadInt1(netRequest);
         c_items_upgrade_cost_money = loadInt1(netRequest);
         c_items_upgrade_chance = loadInt1(netRequest);
         c_items_upgrade_power_inc = loadInt1(netRequest);
         c_vip_exp_coof = new String[netRequest.response.readInt()];

         for(integerCode = 0; integerCode < c_vip_exp_coof.length; ++integerCode) {
            c_vip_exp_coof[integerCode] = netRequest.response.readUTF();
         }

         c_vip_money_coof = new String[netRequest.response.readInt()];

         for(integerCode = 0; integerCode < c_vip_money_coof.length; ++integerCode) {
            c_vip_money_coof[integerCode] = netRequest.response.readUTF();
         }

         c_vip_drop_coof = new String[netRequest.response.readInt()];

         for(integerCode = 0; integerCode < c_vip_drop_coof.length; ++integerCode) {
            c_vip_drop_coof[integerCode] = netRequest.response.readUTF();
         }

         Battle.c_rage_Add = netRequest.response.readInt();
         Battle.c_rage_Sub = netRequest.response.readInt();
         Battle.c_rage_Colors = loadInt1(netRequest);
         Battle.c_rage_Levels = loadInt1(netRequest);
         Battle.c_rage_Coofs_int = loadInt1(netRequest);
         Battle.c_battle_action_random_min = netRequest.response.readInt();
         Battle.c_battle_action_random_max = netRequest.response.readInt();
         Battle.c_rage_AddVal = netRequest.response.readInt();
         Battle.c_rage_AddTime = netRequest.response.readInt();
         Battle.c_rage_cost = netRequest.response.readInt();
         Battle.c_battle_add_power_legioner_vs_higher = netRequest.response.readInt();
         netRequest.response.readInt();
         Battle.c_fix_ai_refresh_time = netRequest.response.readInt();
         Battle.c_InertSpeed = netRequest.response.readInt();
         Battle.c_JumpOffsets = loadInt2(netRequest);
         (Battle.c_btl_fieldSize = new int[2])[0] = netRequest.response.readInt();
         Battle.c_btl_fieldSize[1] = netRequest.response.readInt();
         (Battle.c_battle_field_margins = new int[2])[0] = netRequest.response.readInt();
         Battle.c_battle_field_margins[1] = netRequest.response.readInt();
         Battle.c_HorrorTime = netRequest.response.readInt();
         Battle.c_OkopTime = netRequest.response.readInt();
         Battle.c_JumpPause = netRequest.response.readInt();
         Battle.c_ClosetsDist = netRequest.response.readInt();
         Battle.c_DyingTime = netRequest.response.readInt();
         Battle.c_JumpSpeed = netRequest.response.readInt();
         Battle.c_JumpDist = netRequest.response.readInt();
         Battle.c_PoisonTime = netRequest.response.readInt();
         Battle.c_battle_InertSpeed = netRequest.response.readInt();
         Battle.c_AcidTime = netRequest.response.readInt();
         Battle.c_battle_weak_fire_new = netRequest.response.readInt();
         Battle.c_RegenTime = netRequest.response.readInt();
         Battle.c_battle_min_time_between_hits = netRequest.response.readInt();
         Battle.c_all_apply_atacks_time = netRequest.response.readInt();
         Battle.c_all_finish_atacks_time = netRequest.response.readInt();
         Battle.CD_H = netRequest.response.readInt();
         Battle.CD_W = netRequest.response.readInt();
         Battle.CD_HW = netRequest.response.readInt();
         Battle.CD_HH = netRequest.response.readInt();
         Battle.c_battle_time_between_actions_temporal = netRequest.response.readInt();
         Battle.c_rage_Bushs = loadInt1(netRequest);
         netRequest.response.readInt();
         c_clan_treasure_max_percents_per_day = netRequest.response.readInt();
         c_market_min_cost_percent = netRequest.response.readInt();
         c_market_max_cost_percent = netRequest.response.readInt();
         c_market_place_cost_percent = netRequest.response.readInt();
         netRequest.response.readUTF();
         netRequest.response.readLong();
         netRequest.response.readInt();
         c_clan_max_description_len = netRequest.response.readInt();
         c_items_upgrade_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_speed_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_pull_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_answer_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_friends_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_paralize_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_poison_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_hit_dist_chip_item_on_market = netRequest.response.readBoolean();
         c_items_pet_health_chip_item_on_market = netRequest.response.readBoolean();
         c_share_vk_image_url = netRequest.response.readUTF();
         c_quests_max_count = netRequest.response.readInt();
         c_trading_expires_time = netRequest.response.readLong();
         c_trading_items_max_count = netRequest.response.readInt();
         netRequest.response.readInt();
         StringResources.c_chat_channels_names_new = new String[netRequest.response.readShort()];

         for(integerCode = 0; integerCode < StringResources.c_chat_channels_names_new.length; ++integerCode) {
            StringResources.c_chat_channels_names_new[integerCode] = netRequest.response.readUTF();
         }

         StringResources.c_chat_sex_symb = new String[netRequest.response.readShort()];

         for(integerCode = 0; integerCode < StringResources.c_chat_sex_symb.length; ++integerCode) {
            StringResources.c_chat_sex_symb[integerCode] = netRequest.response.readUTF();
         }

         StringResources.c_chat_class_symb = new String[netRequest.response.readShort()];

         for(integerCode = 0; integerCode < StringResources.c_chat_class_symb.length; ++integerCode) {
            StringResources.c_chat_class_symb[integerCode] = netRequest.response.readUTF();
         }

         Battle.c_step_back_allow_time = netRequest.response.readInt();
         Battle.c_unit_atack_start_cooldown_part_1000 = netRequest.response.readInt();
         Battle.c_unit_death_time_before_remove = netRequest.response.readInt();
         Battle.c_unit_jump_height = netRequest.response.readInt();
         c_fast_fight_party_join_for_client_pixels_x = netRequest.response.readInt();
         c_fast_fight_party_join_for_client_pixels_y = netRequest.response.readInt();
         Battle.c_battle_rage_push_min_rnd_d3 = netRequest.response.readInt();
         Battle.c_battle_rage_push_max_rnd_d3 = netRequest.response.readInt();
         Battle.c_battle_rage_push_100 = netRequest.response.readInt();
         Battle.c_rage_distance = netRequest.response.readInt();
      } catch (Exception exception) {
         SendMistace("Can't load constants", false, true, exception);
      }
   }

   private static void LoadSomeData(boolean byDump) {
      try {
      NetRequest netRequest = new NetRequest();
      int writeInt = 4;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Описание рас");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(4);
         }
      } else {
         netRequest = smallReq(1000000, 970, dumping);
         if (dumping) {
            dumpToRSForNetResources(4, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Расы получены");
      StrategRace.loadAllFromStream(netRequest.response);
      netRequest.response.close();
      ++writeInt;
      int bodyIndex;
      if (PlatformRuntime.getInstance().getPlatformType() == 1) {
         for(bodyIndex = 0; bodyIndex < ImagesGlobal.strategGameImagesPhasesCount; bodyIndex = (short)(bodyIndex + 1)) {
            PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Игровые спрайты " + (bodyIndex + 1) + "/" + ImagesGlobal.strategGameImagesPhasesCount);
            if (byDump) {
               if (dumping) {
                  netRequest.response = connectToRSForNetResources(writeInt);
               }
            } else {
               netRequest = smallReq(1000000, 971, strategsLevel, (short)bodyIndex, dumping);
               if (dumping) {
                  dumpToRSForNetResources(writeInt, netRequest.responseBytes);
                  netRequest.responseBytes = null;
               }
            }

            ImagesGlobal.loadStrategGameImages(netRequest.response, bodyIndex);
            netRequest.response.close();
            ++writeInt;
         }
      } else {
         writeInt = ImagesCach.loadCach("Игровые спрайты", "Игровые спрайты", 5, new int[]{decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, byDump, dumping, 990);
         ImagesGlobal.setStrategGameImage(ImagesCach.getImage(ImagesCach.lastName));
      }

      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Описание программных анимации");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         netRequest = smallReq(1000000, 972, dumping);
         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      StrategProgramAnim.loadAllFromStream(netRequest.response);
      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Описание спрайтов");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, 991, new int[]{decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, 973, strategsLevel, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      StrategAnimSprite.loadAllFromStream(netRequest.response, ImagesGlobal.strategGameImages);
      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Описание спрайтов в игре");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         netRequest = smallReq(1000000, 974, strategsLevel, dumping);
         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      StrategAnimGroup.loadAllFromStream(netRequest.response);
      netRequest.response.close();
      ++writeInt;
      if (PlatformRuntime.getInstance().getPlatformType() == 2) {
         writeInt = ImagesCach.loadCach("Графика\rземель", "Графика\rземли", writeInt, new int[]{tilesLevel, decorsLevel}, byDump, dumping, 968);
      } else {
         writeInt = ImagesCach.loadCach("Графика\rтел", "Графика\rтела", writeInt, new int[]{animSpritesLevel}, byDump, dumping, 910);
         writeInt = ImagesCach.loadCach("Графика\rдекора", "Графика\rдекора", writeInt, new int[]{decorsLevel}, byDump, dumping, 920);
         writeInt = ImagesCach.loadCach("Графика\rземель", "Графика\rземли", writeInt, new int[]{tilesLevel}, byDump, dumping, 930);
         writeInt = ImagesCach.loadCach("Графика\rголов", "Графика\rголовы", writeInt, new int[]{animSpritesLevel}, byDump, dumping, 940);
         writeInt = ImagesCach.loadCach("Графика\rэффектов", "Графика\rэффекта", writeInt, new int[]{0}, byDump, dumping, 950);
      }

      if (PlatformRuntime.getInstance().getPlatformType() == 1) {
         writeInt = ImagesCach.loadCach("Дополнительные спрайты", "Дополнительные спрайты", writeInt, new int[0], byDump, dumping, 997);
      }

      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Дополнительные спрайты");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, 995, new int[]{tilesLevel, decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, 996, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      ImagesGlobal.loadAddingAnimSprites(netRequest.response);
      if (PlatformRuntime.getInstance().getPlatformType() == 2) {
         ImageLevelAreaDesc imageLevelAreaDesc2 = new ImageLevelAreaDesc(netRequest.response);
         ImageLevelAreaDesc imageLevelAreaDesc3 = new ImageLevelAreaDesc(netRequest.response);
         fontSmall = new MystFont(imageLevelAreaDesc2.img, imageLevelAreaDesc2.rx, imageLevelAreaDesc2.ry, imageLevelAreaDesc2.rw, imageLevelAreaDesc2.rh, 8, 8, (int[])null, "/Data/All/FontAnim/", 200, 1);
         PlatformRuntime.getInstance();
         fontClan = new MystFont(imageLevelAreaDesc3.img, imageLevelAreaDesc3.rx, imageLevelAreaDesc3.ry, imageLevelAreaDesc3.rw, imageLevelAreaDesc3.rh, 8);
      }

      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rтел");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, 992, new int[]{decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, 961, animSpritesLevel, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Тела\rполучены");
      Bodys = new Body[netRequest.response.readInt()];

      for(bodyIndex = 0; bodyIndex < Bodys.length; ++bodyIndex) {
         PrBarTh.setText("Тело\r" + bodyIndex + "/" + Bodys.length);
         Bodys[bodyIndex] = new Body(netRequest.response);
      }

      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rдекора");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, 969, new int[]{tilesLevel, decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, 962, decorsLevel, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Декор\rполучен");
      decorMaxLeft = 0;
      decorMaxRight = 0;
      decorMaxTop = 0;
      decorMaxBottom = 0;
      Vector vector3 = new Vector();
      Decors = new Decor[netRequest.response.readInt()];

      int decorIndex;
      for(decorIndex = 0; decorIndex < Decors.length; ++decorIndex) {
         PrBarTh.setText("Декорация\r" + decorIndex + "/" + Decors.length);
         Decors[decorIndex] = new Decor(netRequest.response);
         Decor decor2;
         if ((decor2 = Decors[decorIndex]).w2 > decorMaxLeft) {
            decorMaxLeft = decor2.w2;
         }

         if (decor2.w2 > decorMaxRight) {
            decorMaxRight = decor2.w2;
         }

         if (decor2.Offset > decorMaxTop) {
            decorMaxTop = decor2.Offset;
         }

         if (decor2.height - decor2.Offset > decorMaxBottom) {
            decorMaxBottom = decor2.height - decor2.Offset;
         }

         if (decor2.style >= 0) {
            vector3.addElement(decorIndex);
         }
      }

      triangleDecors = new int[vector3.size()];

      for(decorIndex = 0; decorIndex < vector3.size(); ++decorIndex) {
         triangleDecors[decorIndex] = (Integer)vector3.elementAt(decorIndex);
      }

      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rземель");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         netRequest = smallReq(1000000, PlatformRuntime.getInstance().getPlatformType() == 2 ? 967 : 963, PlatformRuntime.getInstance().getPlatformType() == 2 ? new int[]{tilesLevel, decorsLevel} : new int[]{tilesLevel}, dumping);
         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Земли\rполучены");
      Tiles = new Tile[netRequest.response.readInt()];

      for(decorIndex = 0; decorIndex < Tiles.length; ++decorIndex) {
         PrBarTh.setText("Земля\r" + decorIndex + "/" + Tiles.length);
         Tiles[decorIndex] = new Tile(netRequest.response);
      }

      Tile.calculateConstants();
      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rголов");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, 993, new int[]{decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, 964, animSpritesLevel, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Головы\rполучены");
      Heads = new Head[netRequest.response.readInt()];

      for(decorIndex = 0; decorIndex < Heads.length; ++decorIndex) {
         PrBarTh.setText("Голова\r" + decorIndex + "/" + Heads.length);
         Heads[decorIndex] = new Head(netRequest.response);
      }

      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rсуществ");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            netRequest = smallReq(1000000, drawAuras ? 994 : 966, drawAuras ? new int[]{decorsLevel, animSpritesLevel, drawAuras ? 0 : 1, strategsLevel} : new int[]{monstersLevel}, dumping);
         } else {
            netRequest = smallReq(1000000, drawAuras ? 965 : 966, monstersLevel, dumping);
         }

         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Существа\rполучены");
      Legionery.LoadTypes(netRequest);
      if (drawAuras) {
         ImagesGlobal.loadAnimSprites(netRequest.response);
         loadAuraTypes(netRequest.response, true);
      } else {
         loadAuraTypes(netRequest.response, false);
      }

      netRequest.response.close();
      ++writeInt;
      PrBarTh.setText((byDump ? "[Кэш]" : "[Сеть]") + "Получение\rкарты мира");
      if (byDump) {
         if (dumping) {
            netRequest.response = connectToRSForNetResources(writeInt);
         }
      } else {
         netRequest = smallReq(1000000, 71, dumping);
         if (dumping) {
            dumpToRSForNetResources(writeInt, netRequest.responseBytes);
            netRequest.responseBytes = null;
         }
      }

      PrBarTh.setText("Карта получена");
      c_world_map_zero_pos_x = netRequest.response.readInt();
      c_world_map_zero_pos_y = netRequest.response.readInt();
      c_world_map_step_x = netRequest.response.readInt();
      c_world_map_step_y = netRequest.response.readInt();
      byte[] arr = new byte[netRequest.response.readInt()];
      netRequest.response.read(arr);
      c_world_map_image = GameImage.createImage(arr, 0, arr.length);
      netRequest.response.close();
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static void loadCommonData() {
      try {
      boolean loadedFromCache = true;
      PrBarTh.setText("[Сеть]\rИдентификация");
      if (dumping) {
         int integerCode;
         DataInputStream dataInputStream;
         if ((dataInputStream = connectToRSForNetResources(1)) != null) {
            integerCode = dataInputStream.readInt();
            dataInputStream.close();
         } else {
            integerCode = -1;
         }

         NetRequest netRequest;
         int integerCode2 = (netRequest = smallReq(1000000, 10, true)).response.readInt();
         c_maps_cach_data_version = netRequest.response.readInt();
         if (integerCode2 == integerCode) {
            loadedFromCache = false;
         }

         dumpToRSForNetResources(1, netRequest.responseBytes);
      }

      PrBarTh.setText("[Сеть]Время");
      NetRequest netRequest2;
      (netRequest2 = new NetRequest(false)).writeHeader(8000020);
      sendRequest(netRequest2, true);
      if (netRequest2.response.readInt() == 8000021) {
         long timestamp = netRequest2.response.readLong();
         long clientServerTimeDelta = (new Date()).getTime() - timestamp;
         Chat.instance.setDeltaTimeClientServer(clientServerTimeDelta);
      }

      PrBarTh.setText("[Сеть]Шрифты");
      netRequest2 = smallReq(1000000, 130, false);

      netRequest2.response.readBoolean();
      netRequest2.response.readBoolean();
      for (int group = 0; group < 2; group++) {
         int count = netRequest2.response.readInt();
         for (int i = 0; i < count; i++) netRequest2.response.readBoolean();
      }

      fontClan = new MystFont(netRequest2.response, 8);
      if (curSaveCode != 3) {
         loadedFromCache = true;
      }

      if (loadedFromCache) {
         savePlatformSetting(2);
      }

      PrBarTh.setProgress(70);
      if (loadedFromCache) {
         boolean bypassCache = false;
         if (dumping) {
            bypassCache = true;
         }

         netRequest2 = new NetRequest(bypassCache);
         PrBarTh.setText("[Сеть]Получение\rнавыков");
         netRequest2.writeHeader(1000000);
         netRequest2.request.writeInt(1);
         netRequest2.request.writeByte(strategsLevel);
         sendRequest(netRequest2, true);
         netRequest2.response.readInt();
         if (dumping) {
            dumpToRSForNetResources(2, netRequest2.responseBytes);
            netRequest2.responseBytes = null;
         }

         PrBarTh.setText("Навыки\rполучены");
         ImagesGlobal.strategGameImagesPhasesCount = netRequest2.response.readInt();
         Skill.LoadSkills(netRequest2);
         PrBarTh.setProgress(75);
         PrBarTh.setProgress(80);
         netRequest2 = new NetRequest(bypassCache);
         PrBarTh.setText("[Сеть]Получение\rпараметров");
         netRequest2.writeHeader(1000000);
         netRequest2.request.writeInt(3);
         netRequest2.request.writeUTF("newlastlimit099");
         netRequest2.request.writeBoolean(false);
         sendRequest(netRequest2, true);
         netRequest2.response.readInt();
         if (dumping) {
            dumpToRSForNetResources(3, netRequest2.responseBytes);
            netRequest2.responseBytes = null;
         }

         PrBarTh.setText("Загрузка\rпараметров");
         LoadConstants(netRequest2);
         PrBarTh.setProgress(85);
         LoadSomeData(false);
         PrBarTh.setProgress(95);
      } else {
         netRequest2 = new NetRequest();
         PrBarTh.setText("[Кэш]Получение\rнавыков");
         netRequest2.response = connectToRSForNetResources(2);
         ImagesGlobal.strategGameImagesPhasesCount = netRequest2.response.readInt();
         Skill.LoadSkills(netRequest2);
         netRequest2.response.close();
         PrBarTh.setText("[Кэш]Получение\rпараметров");
         netRequest2.response = connectToRSForNetResources(3);
         LoadConstants(netRequest2);
         netRequest2.response.close();
         LoadSomeData(true);
         smallReq(1000000, 11, false);
      }

      PrBarTh.setText("Данные\rполучены");
      savePlatformSetting(3);
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static void initCameras() {
      cameraWorld.setCw(cameraScreen.getCw());
      cameraWorld.setCh(cameraScreen.getCh() - (fontBig != null ? GUIHandler.getSoftkeyHeight(fontBig, GameCommons.instance.guiSoftSpacing) : 0));
      cameraBattle.setCw(cameraScreen.getCw());
      cameraBattle.setCh(cameraScreen.getCh() - (fontBig != null ? GUIHandler.getSoftkeyHeight(fontBig, GameCommons.instance.guiSoftSpacing) + 2 : 0) - (fontBig != null ? BattleTh.getSlotHeight() + 2 : 0));
   }

   public static boolean LoadMostNecesory() {
      try {
         Com.mayThrowIOException();
      boolean loadedSuccessfully = true;
      CoverTh = new Cover();
      chatFontSize = 8;
      ScreenFullW = MainTh.getWidth();
      ScreenFullH = MainTh.getHeight();
      if (ScreenFullW < 128) {
         ScreenFullW = 128;
      }

      if (ScreenFullW > 10000) {
         ScreenFullW = 10000;
      }

      if (ScreenFullH < 128) {
         ScreenFullH = 128;
      }

      if (ScreenFullH > 10000) {
         ScreenFullH = 10000;
      }

      ScreenCurW = ScreenFullW;
      ScreenCurH = ScreenFullH;
      cameraScreen.setCw(ScreenCurW);
      cameraScreen.setCh(ScreenCurH);
      PlatformRuntime.getInstance();
      menuLineAddingHeight = 0;
      PlatformRuntime.getInstance();
      softLineAddingHeight = 0;
      animationLevel = 0;
      touchControlType = 1;
      drawAuras = true;
      isEverythingBad = false;

      RecordStore recordStore;
      boolean createStore;
      int glyphRowCount;
      String fGCopyString;
      try {
         fGCopyString = getFGCopyString() + "_main_data";
         createStore = true;
         recordStore = getCacheStore(fGCopyString, createStore);
         StatCollect.fixMesure("LoadMainData openRecordStore");
         curSaveCode = 1;
         if (recordStore.getNumRecords() > 0) {
            StatCollect.fixMesure("LoadMainData getNumRecords");
            byte[] recordData = recordStore.getRecord(1);
            StatCollect.fixMesure("LoadMainData getRecord");
            int recordLength;
            if ((recordLength = recordData.length) <= 0) {
               savePlatformSetting(curSaveCode);
            } else {
               ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recordData);
               DataInputStream dataInputStream;
               int dataVersion = (dataInputStream = new DataInputStream(byteArrayInputStream)).readInt();
               drawMiniMap = true;
               autoGameEnter = true;
               savedPassword = null;
               boolean cacheExpired = false;
               drawCover = true;
               StatCollect.fixMesure("LoadMainData read ver " + dataVersion);
               int glyphRow;
               if (dataVersion != 30017 && dataVersion != 30018 && dataVersion != 30019 && dataVersion != 30020 && dataVersion != 30021 && dataVersion != 30022) {
                  if (dataVersion == 30016) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     autoGameEnter = dataInputStream.readBoolean();
                     if (dataInputStream.readBoolean()) {
                        savedPassword = dataInputStream.readUTF();
                     } else {
                        savedPassword = null;
                     }

                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     drawCover = dataInputStream.readBoolean();
                     dataInputStream.readByte();
                     dataInputStream.readByte();
                     StatCollect.fixMesure("LoadMainData read base data");
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     StatCollect.fixMesure("LoadMainData read names");
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30015) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     drawCover = dataInputStream.readBoolean();
                     dataInputStream.readByte();
                     dataInputStream.readByte();
                     StatCollect.fixMesure("LoadMainData read base data");
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     StatCollect.fixMesure("LoadMainData read names");
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30014) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     drawCover = dataInputStream.readBoolean();
                     dataInputStream.readByte();
                     StatCollect.fixMesure("LoadMainData read base data");
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     StatCollect.fixMesure("LoadMainData read names");
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30013) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     drawCover = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30012) {
                     lastValidPort = dataInputStream.readInt();
                     MainTh.getWidth();
                     MainTh.getHeight();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     drawCover = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30011) {
                     lastValidPort = dataInputStream.readInt();
                     MainTh.getWidth();
                     MainTh.getHeight();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte();
                     animSpritesLevel = dataInputStream.readByte();
                     decorsLevel = dataInputStream.readByte();
                     tilesLevel = dataInputStream.readByte();
                     monstersLevel = dataInputStream.readByte();
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30010) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     strategsLevel = dataInputStream.readByte() - 1;
                     dumpWorkType = dataInputStream.readByte();
                     safeBuffersDisable = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30009) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30008) {
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     glyphRowCount = dataInputStream.readInt();

                     for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                        enteredNames.addElement(new EnterName(dataInputStream.readUTF()));
                     }

                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30007) {
                     cacheExpired = true;
                     lastValidPort = dataInputStream.readInt();
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30006) {
                     cacheExpired = true;
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     drawMiniMap = dataInputStream.readBoolean();
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30005) {
                     cacheExpired = true;
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     chatFontSize = dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30004) {
                     cacheExpired = true;
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     curSaveCode = dataInputStream.readInt();
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else if (dataVersion == 30003) {
                     cacheExpired = true;
                     dataInputStream.readInt();
                     dataInputStream.readInt();
                     newg_name = dataInputStream.readUTF();
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  } else {
                     cacheExpired = true;
                     dataInputStream.close();
                     byteArrayInputStream.close();
                  }
               } else {
                  lastValidPort = dataInputStream.readInt();
                  dataInputStream.readInt();
                  dataInputStream.readInt();
                  chatFontSize = dataInputStream.readInt();
                  newg_name = dataInputStream.readUTF();
                  curSaveCode = dataInputStream.readInt();
                  drawMiniMap = dataInputStream.readBoolean();
                  autoGameEnter = dataInputStream.readBoolean();
                  if (dataInputStream.readBoolean()) {
                     savedPassword = dataInputStream.readUTF();
                  } else {
                     savedPassword = null;
                  }

                  strategsLevel = dataInputStream.readByte();
                  animSpritesLevel = dataInputStream.readByte();
                  decorsLevel = dataInputStream.readByte();
                  tilesLevel = dataInputStream.readByte();
                  monstersLevel = dataInputStream.readByte();
                  dumpWorkType = dataInputStream.readByte();
                  safeBuffersDisable = dataInputStream.readBoolean();
                  drawCover = dataInputStream.readBoolean();
                  if (dataVersion >= 30018) {
                     animationLevel = dataInputStream.readByte();
                  } else {
                     dataInputStream.readByte();
                  }

                  menuLineAddingHeight = dataInputStream.readInt();
                  StatCollect.fixMesure("LoadMainData read base data");
                  glyphRowCount = dataInputStream.readInt();

                  for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                     enteredNames.addElement(new EnterName(dataInputStream.readUTF(), dataInputStream.readLong()));
                  }

                  if (dataVersion >= 30019) {
                     softLineAddingHeight = dataInputStream.readInt();
                     drawAuras = dataInputStream.readBoolean();
                  }

                  if (dataVersion >= 30020) {
                     isEverythingBad = dataInputStream.readBoolean();
                  }

                  if (dataVersion >= 30021) {
                     touchControlType = dataInputStream.readInt();
                  }

                  if (dataVersion >= 30022) {
                     PlatformRuntime.getInstance().setPlatformType(dataInputStream.readByte());
                  }

                  StatCollect.fixMesure("LoadMainData read names");
                  dataInputStream.close();
                  byteArrayInputStream.close();
               }

               if (cacheExpired) {
                  for(glyphRowCount = recordStore.getNumRecords(); glyphRowCount > 1; --glyphRowCount) {
                     recordStore.deleteRecord(glyphRowCount);
                  }
               }

               StatCollect.fixMesure("LoadMainData removeAll");
               glyphRowCount = 0;

               while(true) {
                  if (glyphRowCount >= enteredNames.size()) {
                     if (strategsLevel < 0) {
                        strategsLevel = 0;
                     }

                     if (strategsLevel > 4) {
                        strategsLevel = 4;
                     }

                     StatCollect.fixMesure("LoadMainData proc names");
                     StatCollect.fixMesure("LoadMainData closeRecordStore");
                     savePlatformSetting(curSaveCode);
                     StatCollect.fixMesure("LoadMainData SaveMainData");
                     break;
                  }

                  for(glyphRow = 0; glyphRow < glyphRowCount; ++glyphRow) {
                     if (((EnterName)enteredNames.elementAt(glyphRowCount)).name.equals(((EnterName)enteredNames.elementAt(glyphRow)).name)) {
                        enteredNames.removeElementAt(glyphRowCount);
                        --glyphRowCount;
                        break;
                     }
                  }

                  ++glyphRowCount;
               }
            }
         } else {
            savePlatformSetting(curSaveCode);
         }

         if (curSaveCode == 2) {
            try {
         Com.mayThrowRecordStoreException();
               deleteCacheStore(getRecordStoreForNetResourcesName());
            } catch (RecordStoreException exception2) {
               exception2.printStackTrace();
            }
         }
      } catch (Exception exception3) {
         exception3.printStackTrace();
      }

      StatCollect.fixMesure("LoadMainData exit");
      if (touchControlType != 4 && touchControlType != 1) {
         touchControlType = 4;
      }

      touchControlType = 4;
      if (FeastOfWar.browserPrefControl.equalsIgnoreCase("key_control")) {
         touchControlType = 1;
      }

      try {
         Com.mayThrowRecordStoreException();
         fGCopyString = getFGCopyString();
         createStore = true;
         recordStore = getCacheStore(fGCopyString, createStore);
         switch (dumpWorkType) {
            case 1:
               if (recordStore.getSize() + 1073741824 < 512000) {
                  dumping = false;
               }
               break;
            case 2:
               dumping = true;
               break;
            case 3:
               dumping = false;
         }
      } catch (RecordStoreException exception4) {
         exception4.printStackTrace();
      }

      if (ScreenCurW < 128) {
         ScreenCurW = 128;
      }

      if (ScreenCurW > 10000) {
         ScreenCurW = 10000;
      }

      if (ScreenCurH < 128) {
         ScreenCurH = 128;
      }

      if (ScreenCurH > 10000) {
         ScreenCurH = 10000;
      }

      if (cameraScreen.getCw() < ScreenCurW || cameraScreen.getCh() < ScreenCurH) {
         loadedSuccessfully = false;
      }

      GameCommons.instance.guiAnimate = animationLevel == 0;
      cameraScreen.setCw(ScreenCurW);
      cameraScreen.setCh(ScreenCurH);
      CoverTh.loadData();
      fontSmall = new MystFont("/Data/All/Font2.png", "/Data/All/FontAnim/", 8, 8, false, 200, 1);
      if (isEverythingBad) {
         fontBig = new MystFont(fontSmall, StringResources.FONT_ACCORDINGS);
      } else {
         fontBig = new MystFont("/Data/All/font_big.png", "/Data/All/font_anim_big/", 8, 16, false, 200, 1);
      }

      fontDigitGame = new MystFont("/Data/All/font_digit_game.png", (String)null, 8, 10, new int[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 13, 11, 26, 5, 14, 146, 143, 0}, 200, 0);
      fontDigitGameRed = new MystFont("/Data/All/font_digit_game_red.png", (String)null, 8, 10, new int[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 13, 11, 26, 5, 14, 146, 143, 0, 88, 67, 86, 66}, 200, 0);

      try {
         Com.mayThrowIOException();
         GUIHandler.loadResources();
         GUIDecorator.loadResources(drawCover);
      } catch (IOException exception5) {
         exception5.printStackTrace();
      }

      MenuManager instance2 = MenuManager.instance;
      MystFont fontBig2 = fontBig;
      MystFont fontDigitGame2 = fontDigitGame;
      boolean guiAnimate2 = GameCommons.instance.guiAnimate;
      int menuLineSpacing = 2 + menuLineAddingHeight;
      int softLineSpacing = 2 + softLineAddingHeight;
      int cw = cameraScreen.getCw();
      glyphRowCount = cameraScreen.getCh();
      int cw2 = cw;
      boolean unusedProgressMarker = true;
      int softLineSpacing2 = softLineSpacing;
      int menuLineSpacing2 = menuLineSpacing;
      boolean guiAnimate22 = guiAnimate2;
      MystFont mystFont = fontDigitGame2;
      MystFont mystFont2 = fontBig2;
      MenuManager menuManager = instance2;
      instance2.guiHandler.init(mystFont2, mystFont, guiAnimate22, menuLineSpacing2, softLineSpacing2, 1, cw2, glyphRowCount, menuManager);
      GameCommons.instance.guiFont = fontBig;
      GameCommons.instance.guiLineSpacing = 2 + menuLineAddingHeight;
      GameCommons.instance.guiSoftSpacing = 2 + softLineAddingHeight;
      initCameras();
      if (PlatformRuntime.getInstance().getPlatformType() != 2) {
         if (!isEverythingBad) {
            Mystery.initDoubleBuffer(cameraScreen.getCw(), cameraScreen.getCh());
         } else {
            Mystery.initDoubleBufferLow(cameraScreen.getCw(), cameraScreen.getCh());
         }
      } else {
         Mystery.initDoubleBufferLow(cameraScreen.getCw(), cameraScreen.getCh());
      }

      return loadedSuccessfully;
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static Vector addForAtlasing0(Vector vector3) {
      fontSmall.addForAtlasing(vector3);
      fontBig.addForAtlasing(vector3);
      fontDigitGame.addForAtlasing(vector3);
      fontDigitGameRed.addForAtlasing(vector3);
      fontRed.addForAtlasing(vector3);
      fontGreen.addForAtlasing(vector3);
      GUIHandler.addForAtlasing(vector3);
      GUIDecorator.addForAtlasing(vector3);
      return vector3;
   }

   private static int ReadInt() {
      int bytesRead;
      int read;
      try {
         bytesRead = 0;

         for(long startRep = System.currentTimeMillis(); bytesRead < 4; Thread.yield()) {
            if ((read = net_pdis.read(rint, bytesRead, 4 - bytesRead)) == -1) {
               return -1;
            }

            if (read > 0) {
               startRep = System.currentTimeMillis();
               bytesRead += read;
            } else if (System.currentTimeMillis() - startRep >= 120000L) {
               return -1;
            }
         }
      } catch (Exception exception) {
         SendMistace("ERROR in read int is " + exception.toString(), true, true, exception);
         return -1;
      }

      bytesRead = rint[0] + 128;
      read = rint[1] + 128;
      int thirdByte = rint[2] + 128;
      int fourthByte = rint[3] + 128;
      return bytesRead | read << 8 | thirdByte << 16 | fourthByte << 24;
   }

   public static boolean sendRequest(NetRequest netRequest) {
      return sendRequest(netRequest, true);
   }

   private static boolean sendRequest(NetRequest netRequest, boolean fullRead) {
      lastSendRequestTime = System.currentTimeMillis();
      isActiveRequest = true;

      try {
         netRequest.success = true;
         int responseLength = -10000;

         try {
            synchronized(sendLock) {
               sentBytes += (long)netRequest.requestBuffer.size();
               byte[] bytes = netRequest.requestBuffer.toByteArray();
               netRequest.requestBuffer.reset();
               int bytesCount = bytes.length - 4;
               bytes[0] = (byte)((bytesCount & 255) - 128);
               bytes[1] = (byte)((bytesCount >> 8 & 255) - 128);
               bytes[2] = (byte)((bytesCount >> 16 & 255) - 128);
               bytes[3] = (byte)((bytesCount >>> 24) - 128);
               fullRead = true;
               net_os.write(bytes);
               net_os.flush();
               netRequest.requestBuffer.close();
               netRequest.request.close();
               netRequest.requestBuffer = null;
               netRequest.request = null;
               if ((responseLength = ReadInt()) <= 0) {
                  netRequest.success = false;
               } else if (responseLength > 2097152) {
                  netRequest.success = false;
               } else {
                  getBytes += (long)(responseLength + 4);
                  bytes = new byte[responseLength];
                  bytesCount = 0;

                  for(long startRep = System.currentTimeMillis(); bytesCount < responseLength; Thread.yield()) {
                     int read;
                     if ((read = net_pdis.read(bytes, bytesCount, responseLength - bytesCount)) == -1) {
                        responseLength = -13000;
                        netRequest.success = false;
                        break;
                     }

                     if (read > 0) {
                        startRep = System.currentTimeMillis();
                        bytesCount += read;
                     } else if (System.currentTimeMillis() - startRep >= 120000L) {
                        responseLength = -12000;
                        netRequest.success = false;
                        break;
                     }
                  }

                  if (netRequest.success) {
                     byte[] bytes2 = bytes;
                     boolean unusedProgressMarker = true;
                     byte[] responseBytes = bytes2;
                     if (netRequest.retainResponseBytes) {
                        netRequest.responseBytes = responseBytes;
                     }

                     netRequest.responseBuffer = new ByteArrayInputStream(responseBytes);
                     netRequest.response = new DataInputStream(netRequest.responseBuffer);
                     netRequest.responseReceived = true;
                     netRequest.success = true;
                  }
               }
            }
         } catch (Exception exception) {
            System.out.println("SendRequest error");
            netRequest.success = false;
            SendMistace("ERROR in SendRequest is " + exception.toString(), true, false, exception);
         }

         if (!netRequest.success) {
            System.out.println("SendRequest error");
            SendMistace("BAD in SendRequest is " + responseLength + " ", true, false, (Exception)null);
            showFatalScreen("Соединение с сервером потеряно. Перезапустите игру.");
         }
      } finally {
         lastSendRequestTime = System.currentTimeMillis();
         isActiveRequest = false;
      }

      return netRequest.success;
   }

   public static void showFatalScreen(String messageText) {
      GameCommons.instance.stopTimer();
      PrBarTh.Stop();
      if (mainTh.stopPaint) {
         textWrite.totalExit();
         Chat.chatWindow.diactivate();
      }

      MenuManager.instance.guiHandler.hideAllWindows();
      MenuManager.instance.lockMenuReplacement();
      if (BlackTh != null) {
         BlackTh.setLockReplace(true);
      }

      mainTh.setStopPaint();
      mainTh.setPFter1(BlackTh);
      (new OtherMenuList()).showMessageForMenu(172, messageText);
   }

   public static void SendMistace(String s, boolean sendNet, boolean fatal, Exception exception) {
      try {
         String add = s;
         if (exception != null) {
            add = s + "\nStack trace:\n" + getStackTrace((Throwable)exception);
         }

         add = add + "\n== ALL THREADS ==\n" + getAllThreadsStack();
         System.out.println("Browser " + add);
         if (exception != null) {
            exception.printStackTrace();
         }

         if (sendNet) {
            System.out.println(s);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream;
            (dataOutputStream = new DataOutputStream(byteArrayOutputStream)).writeUTF(newg_name + " || newlastlimit099" + " || " + s + add);
            dataOutputStream.flush();
            byte[] arr = byteArrayOutputStream.toByteArray();
            DataOutputStream dataOutputStream2;
            SocketConnection socketConnection2;
            (dataOutputStream2 = (socketConnection2 = (SocketConnection)Black.openConnection("socket://win.lastlimit.ru:54001")).openDataOutputStream()).write((byte)((arr.length & 255) - 128));
            dataOutputStream2.write((byte)((arr.length >> 8 & 255) - 128));
            dataOutputStream2.write((byte)((arr.length >> 16 & 255) - 128));
            dataOutputStream2.write((byte)((arr.length >>> 24) - 128));
            dataOutputStream2.write(arr);
            dataOutputStream2.flush();
            dataOutputStream.close();
            socketConnection2.close();
         }
      } catch (Exception exception2) {
         exception2.printStackTrace();
      }

      if (fatal) {
         showFatalScreen(s);
      }

   }

   public static void tryCloseMenuWithNecessaryCheck() {
      boolean close = true;
      if (MenuManager.instance.getCurMenuList() != null) {
         switch (MenuManager.instance.getCurMenuList().getCurMenuId()) {
            case 185:
            case 195:
               close = false;
         }
      }

      if (close) {
         MenuManager.instance.guiHandler.hideAllWindows();
      }

   }

   public static boolean tryMoveToNextMap(int direct) {
      boolean res = false;
      PrBarTh.Set(true);

      try {
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(6100000);
         netRequest.request.writeInt(direct);
         netRequest.request.writeInt(Strategist.instance.cx);
         netRequest.request.writeInt(Strategist.instance.cy);
         sendRequest(netRequest, true);

         try {
            if ((direct = netRequest.response.readInt()) == 6100001) {
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               res = true;
            } else if (direct == 6100002) {
               GameCommons.instance.showFloatText("Проход заблокирован");
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      } catch (IOException exception2) {
         exception2.printStackTrace();
      }

      PrBarTh.Stop();
      return res;
   }

   private static int connectToServer() {
      boolean tryOtherPorts = false;
      boolean useMainPorts = true;
      int connectionResult = 0;

      try {
         HttpConnectionAdapter httpConnectionAdapter = null;
         DataInputStream dataInputStream = null;
         boolean closeBootstrapConnectionInFinally = false;

         int usePortIndex;
         label251: {
            label252: {
               try {
                  closeBootstrapConnectionInFinally = true;
                  if ((httpConnectionAdapter = (HttpConnectionAdapter)Black.openConnection("http://win.lastlimit.ru/lastlimit_connect_main.php?client_version_id=newlastlimit099")).getResponseCode() != 200) {
                     closeBootstrapConnectionInFinally = false;
                  } else {
                     StringBuffer stringBuffer = new StringBuffer();
                     dataInputStream = httpConnectionAdapter.openDataInputStream();

                     while((usePortIndex = dataInputStream.read()) != -1) {
                        stringBuffer.append((char)usePortIndex);
                     }

                     useMainPorts = !stringBuffer.toString().equals("0");
                     closeBootstrapConnectionInFinally = false;
                  }
                  break label252;
               } catch (Exception exception) {
                  exception.printStackTrace();
                  useMainPorts = true;
                  closeBootstrapConnectionInFinally = false;
               } finally {
                  if (closeBootstrapConnectionInFinally) {
                     if (dataInputStream != null) {
                        dataInputStream.close();
                     }

                     if (httpConnectionAdapter != null) {
                        httpConnectionAdapter.close();
                     }

                  }
               }

               if (dataInputStream != null) {
                  dataInputStream.close();
               }

               if (httpConnectionAdapter != null) {
                  httpConnectionAdapter.close();
               }
               break label251;
            }

            if (dataInputStream != null) {
               dataInputStream.close();
            }

            httpConnectionAdapter.close();
         }

         int[] usePorts = useMainPorts ? c_net_ports_main : c_net_ports_second;
         if (lastValidPort == -1) {
            lastValidPort = usePorts[0];
         }

         if (lastValidPort == -1) {
            tryOtherPorts = true;
         } else {
            label264: {
               boolean savedPortAvailable = false;

               for(usePortIndex = 0; usePortIndex < usePorts.length; ++usePortIndex) {
                  int candidatePort = usePorts[usePortIndex];
                  if (lastValidPort == candidatePort) {
                     savedPortAvailable = true;
                     break;
                  }
               }

               if (savedPortAvailable) {
                  if ((connectionResult = tryPort(lastValidPort)) != 1) {
                     break label264;
                  }

                  lastValidPort = -1;
               }

               tryOtherPorts = true;
            }
         }

         if (tryOtherPorts) {
            for(int lengthIndex = 0; lengthIndex < usePorts.length; ++lengthIndex) {
               if ((connectionResult = tryPort(usePorts[lengthIndex])) != 1) {
                  lastValidPort = usePorts[lengthIndex];
                  savePlatformSetting(curSaveCode);
                  return 4;
               }
            }

            showFatalScreen("Не удалось выполнить соединение");
         }
      } catch (Exception exception2) {
         connectionResult = 1;
      }

      return connectionResult;
   }

   private static int tryPort(int portnumber) {
      byte connectionResult = 1;

      try {
         PrBarTh.setText("Соединение\rна порт " + portnumber);
         ServerConnectTask serverConnectTask = new ServerConnectTask(portnumber);
         Thread thread2 = new Thread(serverConnectTask);
         long beginTime = System.currentTimeMillis();
         thread2.start();

         while(System.currentTimeMillis() - beginTime < 60000L && !serverConnectTask.connected && serverConnectTask.connecting) {
            Thread.sleep(1000L);
         }

         if (!serverConnectTask.connected) {
            return 1;
         }

         net_os = socketConnection.openOutputStream();
         net_pdis = socketConnection.openInputStream();
         NetRequest netRequest;
         (netRequest = new NetRequest()).request.writeInt(8000000);
         netRequest.request.writeUTF("newlastlimit099");
         netRequest.request.writeUTF(FeastOfWar.getApplicationPlatform());
         sendRequest(netRequest, true);
         if (netRequest.response.readInt() == 1) {
            connectionResult = 3;
         } else {
            connectionResult = 2;
         }

         lastConnectTime = System.currentTimeMillis();
         if (connectionResult == 3) {
            (netRequest = new NetRequest()).request.writeInt(8000010);
            sendRequest(netRequest, true);
            if (netRequest.response.readInt() == 8000011) {
               serverProtocolVersion = netRequest.response.readInt();
            }

            if (serverProtocolVersion < 0) {
               connectionResult = 2;

               try {
                  socketConnection.close();
               } catch (Exception exception) {
                  exception.printStackTrace();
               }
            }
         }
      } catch (Exception exception2) {
         exception2.printStackTrace();
      }

      return connectionResult;
   }

   public static void startMIDlet() {
      try {
         Com.mayThrowIOException();
      IndirectNetworkHandler.instance.clearParsers();
      IndirectNetworkHandler.instance.addParser((EventsStreamParser)(new StrategEventsParser()));
      IndirectNetworkHandler.instance.addParser((EventsStreamParser)(new HeroEventsParser()));
      MB.Create(cameraWorld, Tiles);
      ImagesGlobal.loadBaseImages();
      (PrBarTh = new TrackBar()).Set(true);
      ClientState.instance.set(1);
      PrBarTh.setProgress(10);
      fgDataPresent = false;

      try {
         Com.mayThrowRecordStoreNotFoundException();
         PrBarTh.setProgress(20);
         PrBarTh.setText("Шрифт");
         fontRed = new MystFont("/Data/All/DigitRed.png", (String)null, 8, 8, true, 200, 0);
         fontGreen = new MystFont("/Data/All/DigitGreen.png", (String)null, 8, 8, true, 200, 0);
         BattleUnit.mFont = fontSmall;
         BattleUnit.dGFont = fontGreen;
         BattleUnit.dRFont = fontRed;
         PrBarTh.setProgress(30);
         PrBarTh.setText("Рисунки\rиз клиента");
         ImagesGlobal.loadCommonImages();
         PrBarTh.setProgress(40);
         PrBarTh.setText("Анимации\rиз клиента");
         ImagesGlobal.loadAnimSprites();
         if (PlatformRuntime.getInstance().getPlatformType() == 2) {
            PlatformRuntime.getInstance();
            ImagesGlobal.addForAtlasing(addForAtlasing0(new Vector()));
         }

         PrBarTh.setProgress(50);
         PrBarTh.setText("Получение\rid");

         try {
            masterPlayer = null;
            if (null == null) {
               masterPlayer = "";
            }
         } catch (Exception exception2) {
            masterPlayer = "";
         }

         try {
            string2 = null;
            if (null == null) {
               string2 = "";
            }
         } catch (Exception exception3) {
            string2 = "";
         }

         try {
            string3 = null;
            if (null == null) {
               string3 = "";
            }
         } catch (Exception exception4) {
            string3 = "";
         }

         PrBarTh.setProgress(60);
         PrBarTh.setText("Установка\rсоединения");
         int connectionResult = connectToServer();
         flgs_GameRuned = false;
         if (connectionResult == 3) {
            GameCommons.instance.init();
            GameTh = new Game();
            battleTh = new BattleTh();
            PrBarTh.setText("Данные с сервера");
            loadCommonData();
            Strategist instance2 = Strategist.instance;
            short cd_wi = (short)Strategist.cd_wi;
            short cdH = (short)Strategist.cd_hi;
            short cdW = cd_wi;
            Strategist strategist2 = instance2;
            instance2.cdH = cdH;
            strategist2.cdW = cdW;
            strategist2.cdHW = (short)(cdW / 2);
            strategist2.cdHH = (short)(cdH / 2);
         }

         PrBarTh.setProgress(100);
         PrBarTh.Stop();
         if (connectionResult == 3) {
            if (autoGameEnter && savedPassword != null && newg_name != null) {
               ClientState.instance.set(12);
               newg_password = savedPassword;
               BlackTh.Set();
               GlobalMapTh.startThread();
               loginFromMenu("Авто-вход");
            } else {
               ClientState.instance.set(2);
               BlackTh.Set();
               MenuManager.instance.showMenuById(1);
               GlobalMapTh.startThread();
            }
         } else {
            ClientState.instance.set(11);
            if (connectionResult == 2) {
               afterFatalLoadClient = true;
               showFatalScreen("Файл игры устарел. Нажмите \"ок\" для получения новой версии игры, или скачайте её на\nwww.LastLimit.ru\nЕсли установка проходит неудачно, то нужно удалить старую версию игры.");
            } else {
               if (connectionResult != 4) {
                  showFatalScreen("Нет соединения с сервером. Подробности на \rwww.LastLimit.ru");
                  return;
               }

               showFatalScreen("Сканирование открытых портов провайдера завершено. Пожалуйста, перезапустите приложение для оптимальной работы.");
            }
         }
      } catch (RecordStoreNotFoundException exception5) {
         String[] list = RecordStore.listRecordStores();

         for(int lengthIndex = 0; lengthIndex < list.length; ++lengthIndex) {
            try {
               deleteCacheStore(list[lengthIndex]);
            } catch (Exception exception6) {
            }
         }

         SendMistace("Переполнение кеша. Кеш очищен, запустите игру заново.", false, true, exception5);
         return;
      } catch (Exception exception7) {
         SendMistace("Exception in StartMIDlet " + exception7.toString(), true, true, exception7);
      }

   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void tryRegistration() {
      int yu;
      if (1 == (yu = registerHero())) {
         addEnterName(newg_name);
         savePlatformSetting(curSaveCode);
      } else if (yu == 2) {
         (new OtherMenuList()).showWorldMapMenu();
         GameCommons.instance.showFloatText("Имя уже занято!");
      } else if (yu == 3) {
         (new OtherMenuList()).showTextEnter(177, "Введи свой email или номер телефона, по которому с тобой можно связаться в случае необходимости", newg_contact);
         GameCommons.instance.showFloatText("Контактная информация неверна!");
      } else if (yu == 4) {
         (new OtherMenuList()).showWorldMapMenu();
         GameCommons.instance.showFloatText("Превышена частота регистраций, попробуйте через час.");
      } else {
         showFatalScreen("Ошибка связи. Перезапустите приложение и повторите попытку.");
      }
   }

   private static void removeDeviceBan() {
      try {
         deleteCacheStore("banned_player_id");
      } catch (Exception exception) {
      }

      try {
         deleteCacheStore("rqWPBnn_56");
      } catch (Exception exception2) {
      }
   }

   private static long getBannedDevicePlayerId() {
      RecordStore recordStore;
      try {
         recordStore = getCacheStore("rqWPBnn_56", false);
      } catch (Exception exception) {
         try {
            recordStore = getCacheStore("banned_player_id", false);
         } catch (Exception exception2) {
            return -1L;
         }
      }

      try {
         if (recordStore.getNumRecords() <= 0) {
            return -1L;
         } else {
            byte[] arr = recordStore.getRecord(1);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr);
            DataInputStream dataInputStream;
            long playerId = (dataInputStream = new DataInputStream(byteArrayInputStream)).readLong();
            dataInputStream.close();
            byteArrayInputStream.close();
            return playerId;
         }
      } catch (Exception exception3) {
         exception3.printStackTrace();
         return -1L;
      }
   }

   private static boolean loginToServer(boolean resetIndirectData) {
      try {
         Com.mayThrowIOException();
      IndirectNetworkHandler.instance.resetData();
      PlayerGameState.instance.reset();
      NetRequest netRequest;
      (netRequest = new NetRequest()).writeHeader(1100040);
      netRequest.request.writeUTF(newg_name);
      netRequest.request.writeUTF(newg_password);
      netRequest.request.writeUTF(getSendID());
      DataOutputStream request2 = netRequest.request;
      PlatformRuntime.getInstance();
      request2.writeUTF(PlatformRuntime.getPlatformName());
      long banDevice = getBannedDevicePlayerId();
      netRequest.request.writeLong(banDevice);
      netRequest.request.writeUTF("newlastlimit099");
      netRequest.request.writeByte(IndirectNetworkHandler.instance.getCurDataBigVersion());
      netRequest.request.writeInt(IndirectNetworkHandler.instance.getCurDataVersion());
      netRequest.request.writeInt(enteredNames.size());

      for(int i = 0; i < enteredNames.size(); ++i) {
         EnterName enterName = (EnterName)enteredNames.elementAt(i);
         netRequest.request.writeUTF(enterName.name);
         netRequest.request.writeLong(enterName.date);
      }

      sendRequest(netRequest, true);
      if ((PacketID = netRequest.response.readInt()) == 1100001) {
         if (netRequest.response.readBoolean() && banDevice >= 0L) {
            removeDeviceBan();
         }

         if (netRequest.response.readBoolean()) {
            Strategist.accountEmail = netRequest.response.readUTF();
         } else {
            Strategist.accountEmail = null;
         }

         netRequest.response.readInt();
         dontFightAndAction = true;
         PlatformRuntime.getInstance();
         GlobalMapTh.gmTh.clientDeltaAntichitReset();
         IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
         return true;
      } else {
         if ((lastCantLoginCode = netRequest.response.readInt()) == 4) {
            loginInfo = netRequest.response.readUTF();
         }

         if (lastCantLoginCode == 7) {
            if (netRequest.response.readBoolean()) {
               lastLoginEmail = netRequest.response.readUTF();
            } else {
               lastLoginEmail = null;
            }
         }

         if (lastCantLoginCode == 8) {
            lastLoginLaterTime = netRequest.response.readLong();
         }

         return false;
      }
   
      } catch (IOException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static int registerHero() {
      byte registrationResult = 0;

      try {
         PrBarTh.Set(true);
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1100050);
         netRequest.request.writeUTF(newg_name);
         netRequest.request.writeUTF(newg_password);
         netRequest.request.writeInt(newg_pol);
         netRequest.request.writeInt(newg_sprite);
         netRequest.request.writeUTF(getSendID());
         netRequest.request.writeUTF("newlastlimit099");
         netRequest.request.writeUTF(newg_contact);
         netRequest.request.writeUTF(masterPlayer);
         netRequest.request.writeUTF(string2);
         sendRequest(netRequest, true);
         if ((PacketID = netRequest.response.readInt()) == 1100011) {
            if (loginToServer(true)) {
               registrationResult = 1;
            } else {
               newg_name = "";
               registrationResult = 2;
            }
         } else if (PacketID == 1100012) {
            newg_name = netRequest.response.readUTF();
            registrationResult = 2;
         } else if (PacketID == 1100013) {
            registrationResult = 3;
         } else if (PacketID == 1100014) {
            registrationResult = 4;
         }
      } catch (Exception exception) {
         showFatalScreen("Ошибка связи. Перезапустите приложение и повторите попытку.");
      }

      PrBarTh.Stop();
      return registrationResult;
   }

   public static void ProcessClanActions(int backIndex) {
      NetRequest netRequest = new NetRequest();

      try {
         int requestOpcode = 994450000;
         boolean withFriend = false;
         switch (backIndex) {
            case 105:
               requestOpcode = 4480000;
            case 106:
            case 107:
            case 109:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            default:
               break;
            case 108:
               withFriend = true;
               requestOpcode = 4402000;
               break;
            case 110:
               withFriend = true;
               requestOpcode = 4490010;
               break;
            case 116:
               requestOpcode = 4450000;
               break;
            case 127:
               withFriend = true;
               requestOpcode = 4404000;
               break;
            case 128:
               withFriend = true;
               requestOpcode = 4405000;
               break;
            case 129:
               withFriend = true;
               requestOpcode = 4490000;
         }

         netRequest.writeHeader(requestOpcode);
         if (withFriend) {
            netRequest.request.writeInt(MenuSupport.instance.curFriend);
         }

         sendRequest(netRequest, true);
         if (netRequest.response.readInt() == requestOpcode + 1) {
            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      PrBarTh.Stop();
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }


   private static void mayThrowIOException() throws IOException {}

   private static void mayThrowRecordStoreNotFoundException() throws RecordStoreNotFoundException {}

   private static RecordStore getCacheStore(String name, boolean create) {
      try {
         return RecordStore.openRecordStore(name, create);
      } catch (RecordStoreException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static void deleteCacheStore(String name) {
      try {
         RecordStore.deleteRecordStore(name);
      } catch (RecordStoreException exception) {
         Com.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   private static void mayThrowRecordStoreException() throws RecordStoreException {}

   private Com() {
   }

}
