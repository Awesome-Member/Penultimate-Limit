package desktop.midlet;

import applet.Main;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;
import java.net.URL;

public class Midlet {
   public void startApp() {
   }

   public final void openBrowser(String messageText) {
      if (Main.instance.standalone) {
         messageText = messageText;
         Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
         Desktop desktop2 = desktop;
         if (desktop == null || !desktop2.isSupported(Action.BROWSE)) {
            return;
         }

         try {
            desktop2.browse(URI.create(messageText));
         } catch (Exception exception) {
            return;
         }
      } else {
         messageText = messageText;

         try {
            Main.instance.getAppletContext().showDocument(new URL(messageText), "_blank");
            return;
         } catch (Exception exception2) {
         }
      }

   }

   public static String getApplicationPlatform() {
      try {
         String messageText = "fdsCnbtldmsA`rd";
         char[] characters;
         int characterCount = (characters = "fdsCnbtldmsA`rd".toCharArray()).length;

         for(int characterIndex = 0; characterIndex < characterCount; ++characterIndex) {
            char encodedCharacter = characters[characterIndex];
            messageText = messageText + (char)(encodedCharacter + 1);
         }

         return Main.instance.getClass().getMethod(messageText.substring(messageText.length() / 2)).invoke(Main.instance).toString();
      } catch (Exception exception) {
         return "midlet.someZ89IL()";
      }
   }
}
