package MenuPck;

import Moduls.StrategGraphic;
import Moduls.InventoryItem;
import java.util.Hashtable;

public final class MenuSupport {
   private static char[] PASSWORD_NOT_JUST = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
   public static final int[] SILENCE_TIMES = new int[]{5, 10, 60, 180};
   public int newGameMode = 0;
   public String curSelectedClanName = "";
   public boolean isMembersForClanRequest = false;
   public boolean isCreateStrategFromServiceMenu = false;
   public String adminSaveText = "";
   public MenuPlayerDescription[] otherPlayers;
   public int curFriend;
   public MenuPlayerDescription otherPlayerMenuPlayer = new MenuPlayerDescription("", "", 1, new StrategGraphic());
   public int otherPlayerMenuBack;
   public boolean otherPlayerMenuPeace;
   public String otherPlayerMenuAtackInfo;
   public String[] otherPlayerMenuIcons;
   public String[] otherPlayerMenuItems;
   public String[] otherPlayerMenuItemsInfos;
   public InventoryItem[] otherPlayerMenuItemsCont;
   public byte[] photoData;
   public String string;
   public String photoPath;
   public String string2;
   public String string3;
   public String string4;
   public int tempStartegId;
   public String menuCurDir;
   public String moderBattlesListSaveSearchString = "";
   public int menuAuraInd;
   public String string5;
   public String string6;
   public InventoryItem[] itemsSellerSoldItems;
   public InventoryItem[] itemsSellerItems;
   public String questSuccessInfoMap = "";
   public int curActiveTownInd;
   public int[] itemsEquealsIds;
   public String string7;
   public int monsterSellerHead;
   public int monsterSellerBody;
   public String[] helpTexts;
   public String[] helpMenuList = new String[0];
   public String[] helpHintsList = new String[0];
   public Hashtable lastSelectedEls = new Hashtable();
   public static MenuSupport instance = new MenuSupport();

   public static boolean isPasswordHaveJustLetters(String messageText) {
      int validCharacterCount = 0;

      for(int lengthIndex = 0; lengthIndex < messageText.length(); ++lengthIndex) {
         char ch = messageText.charAt(lengthIndex);

         for(int pASSWORD_NOT_JUSTIndex = 0; pASSWORD_NOT_JUSTIndex < 58; ++pASSWORD_NOT_JUSTIndex) {
            if (ch == PASSWORD_NOT_JUST[pASSWORD_NOT_JUSTIndex]) {
               ++validCharacterCount;
            }
         }
      }

      if (validCharacterCount == messageText.length()) {
         return true;
      } else {
         return false;
      }
   }
}
