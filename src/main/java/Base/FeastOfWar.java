package Base;

import desktop.PlatformRuntime;
import desktop.PlatformListener;
import battlepck.client.BattleNetTh;
import desktop.ui.Display;
import desktop.midlet.Midlet;

public final class FeastOfWar extends Midlet implements PlatformListener {
   public static String browserPrefControl;
   public static String platform;
   public static String profiles;
   public static String configuration;
   public static String string;
   private static boolean wasStartApp = false;
   private static int wasStopPaintBeforePause = 0;

   public FeastOfWar() {
      Com.display = Display.getDisplay();
      Com.midlet = this;
      PlatformRuntime.getInstance().setPlatformListener((PlatformListener)this);
   }

   public final void startApp() {
      if (!wasStartApp) {
         wasStartApp = true;

         try {
            if (System.getProperty("microedition.platform") != null) {
               platform = System.getProperty("microedition.platform");
            }

            if (System.getProperty("microedition.profiles") != null) {
               profiles = System.getProperty("microedition.profiles");
            }

            if (System.getProperty("microedition.configuration") != null) {
               configuration = System.getProperty("microedition.configuration");
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         if (browserPrefControl == null) {
            browserPrefControl = "";
         }

         try {
            Com.isAndroid = true;
         } catch (Exception exception2) {
            exception2.printStackTrace();
         }

         Com.isAndroid = false;
         if (Com.mainTh == null) {
            Com.mainTh = new MainTh();
         }
      }

      if (wasStopPaintBeforePause != 0) {
         Com.mainTh.stopPaint = wasStopPaintBeforePause == 1;
      }

      Com.initSystemGUI();
      if (!Com.flgs_MidletStart) {
         Com.settingsChanged = false;
         Com.flgs_MidletStart = true;
         Com.LoadMostNecesory();
         Com.CoverTh.Set(0);
         Com.mainTh.SetAsCurrent((Display)Com.display);
      } else {
         Com.mainTh.stopFunctionate = false;
      }
   }

   public static void resumePainting() {
      if (Com.mainTh != null) {
         wasStopPaintBeforePause = Com.mainTh.stopPaint ? 1 : 2;
         Com.mainTh.stopPaint = true;
         Com.mainTh.stopFunctionate = true;
         Com.mainTh.callReleasAllEvent();
      }

   }

   public final void quit() {
      PlatformRuntime.getInstance();
      Com.isActiveRequest = false;
      Com.flgs_MidletStart = false;
      Com.flgs_GameRuned = false;
      wasStartApp = false;
      wasStopPaintBeforePause = 0;
      if (Com.mainTh != null) {
         Com.mainTh.StopCurrent();
      }

      GlobalMapTh.suspend();
      BattleNetTh.setActive();
      MainRep.setActive();

      try {
         if (Com.socketConnection != null) {
            Com.socketConnection.close();
         }

      } catch (Exception exception) {
      }
   }
}
