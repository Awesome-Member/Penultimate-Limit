package MenuPck;

import Base.Com;
import Base.GameCommons;
import MenuPck.ListTypes.AvatarMenuList;
import MenuPck.ListTypes.BattleListMenuList;
import MenuPck.ListTypes.ChatMenuList;
import MenuPck.ListTypes.ClanMembersMenuList;
import MenuPck.ListTypes.ClanMenuList;
import MenuPck.ListTypes.FirstMenuList;
import MenuPck.ListTypes.ItemsMenuList;
import MenuPck.ListTypes.LiederActionsMenuList;
import MenuPck.ListTypes.MainMenuList;
import MenuPck.ListTypes.MarketMenuList;
import MenuPck.ListTypes.NeighborsMenuList;
import MenuPck.ListTypes.OtherMenuList;
import MenuPck.ListTypes.PartyMemberActionList;
import MenuPck.ListTypes.PartyMembersMenuList;
import MenuPck.ListTypes.ServicesMenuList;
import MenuPck.ListTypes.SkillsMenuList;
import MenuPck.ListTypes.StrategMenuList;
import MenuPck.ListTypes.TradeMenuList;
import MenuPck.ListTypes.TrustMenuList;

public final class MenuManager extends GUIHandlerEvents {
   private static final MenuList[] _menuLists;
   public GUIHandler guiHandler = new GUIHandler();
   protected MenuList curMenuList = null;
   public static MenuManager instance;

   public final void onChangeResolution(int w, int h, boolean justPaint) {
      this.guiHandler.onChangeResolution(w, h, justPaint);
   }

   public final void onMenuContentChanged2() {
      GameCommons.instance.repositionateFloatText();
   }

   public final void onStartShowMenus() {
      if (Com.GameTh != null) {
         Com.GameTh.playerControllDisableAll();
      }

   }

   public final void showMenuById(int menuId) {
      getMenuListById(menuId).showListMenuInner(menuId, false);
   }

   public static void showTextInput(String titleText, String defaultText, int id, int backIndex, int maxLen) {
      Com.textWrite.activate(0, titleText, defaultText, id, backIndex, false, maxLen);
   }

   public final void textEnterResult(int id, int backIndex, boolean res, String messageText) {
      try {
         getMenuListById(id).onTextEnterEvent(backIndex, res, messageText);
         if (this.curMenuList != null) {
            this.curMenuList.onCloseMenuEvent(this.curMenuList.curMenuId);
         }

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public static MenuList getMenuListById(int id) {
      for(int menuListIndex = 0; menuListIndex < _menuLists.length; ++menuListIndex) {
         int[] integers = _menuLists[menuListIndex].getJointIds();

         for(int lengthIndex = 0; lengthIndex < integers.length; ++lengthIndex) {
            if (integers[lengthIndex] == id) {
               return _menuLists[menuListIndex];
            }
         }
      }

      return null;
   }

   public final void lockMenuReplacement() {
      this.guiHandler.setLockReplace(true);
   }

   public final void activateListEl(int i) {
      if (this.curMenuList != null) {
         this.curMenuList.activateEl(i);
      }
   }

   public final MenuList getCurMenuList() {
      return this.curMenuList;
   }

   static {
      _menuLists = new MenuList[]{new MainMenuList(0, -2, "ɚɛМИР|ɜɝЧАТ|ɞɟГЕРОЙ|ɠɡИНВЕНТАРЬ|ɢɣБОНУСЫ|ɤɥНАСТРОЙКИ"), new MainMenuList(71, 0, "Помощь|Информация|Подпись|Шрифт чата|Расширенный интерфейс|Компас|Поток чата|Миникарта|Отказ от сделок|Отказ от нападений|Отказ от отряда|Авто-починка|Авто-вход|Сменить пароль|Выход из игры"), new MainMenuList(36, 0, "Карта|Сделки|Нападения|Соседи|Цитадели|Клан|Отряд|Рынок|Записи боёв"), new FirstMenuList(1, -1, "Начать игру|Продолжить игру|Забыл пароль|Завершить игру"), new ChatMenuList(58, 0, "Общий чат|Чат клана|Чат отряда|Приват|Друзья|Игнор"), new ChatMenuList(59, 58, ""), new ChatMenuList(90, 58, ""), new ChatMenuList(216, 58, ""), new ChatMenuList(218, 216, ""), new ChatMenuList(217, 216, ""), new ChatMenuList(91, 90, ""), new AvatarMenuList(2, 1, new String[0]), new ServicesMenuList(37, 0, "|||Золото|Переобучение|Премиум|Безопасность|Вместимость|Смена расы|Смена пола|Смена имени питомца"), new ServicesMenuList(92, 37, ""), new StrategMenuList(4, 0, "Качества|Умения|Квесты|Фотографии"), new ServicesMenuList(82, 37, "̾̿Мужской|̼̽Женский"), new NeighborsMenuList(250, 36, ""), new NeighborsMenuList(70, 36, ""), new NeighborsMenuList(63, 36, "О герое|Изучить|Пригласить в отряд|Напасть|Приват|Торговать|Фотографии"), new ClanMenuList(48, 36, "ɾɿСоздать клан|О нас|Добавить иконку|Соклановцы|Заявки|Казна|Пополнить казну|События|Покинуть клан|Все кланы Предела"), new ClanMenuList(80, 48, new String[0]), new ClanMembersMenuList(81, 80, new String[0]), new ClanMembersMenuList(51, 48, new String[0]), new ClanMenuList(94, 36, new String[0]), new ClanMenuList(95, 94, new String[0]), new LiederActionsMenuList(55, 51, "ɾɿК другу!|Дать из казны|Торговать|Выгнать|Звания|Переходы|Бои с монстрами|Бои с героями|Отдать власть!|Это ты"), new LiederActionsMenuList(57, 55, ""), new PartyMembersMenuList(87, 36, new String[0]), new PartyMemberActionList(88, 87, "Выгнать|Сделать лидером|Покинуть отряд"), new BattleListMenuList(64, 36, new String[0]), new TrustMenuList(43, 36, new String[0]), new SkillsMenuList(7, 4, new String[0]), new MarketMenuList(100, 36, "Предложения|Твои товары|События|Поиск"), new MarketMenuList(103, 100, "Продажи|Возвраты|Покупки"), new MarketMenuList(101, 100, ""), new MarketMenuList(102, 100, ""), new MarketMenuList(104, 100, ""), new ItemsMenuList(5, 0, ""), new ItemsMenuList(10, 5, ""), new ItemsMenuList(11, 10, "Надеть|Снять|Использовать|Заточить|Перековать|Открыть|Открыть до предела|Призвать|Обучить|Дать образ|Починить|Выставить на рынок|Сбыть"), new ItemsMenuList(207, 11, ""), new ItemsMenuList(97, 11, "Бег|Притягивание|Отдача|Усиление от союзников|Заморозка|Поджог|Дистанция|Здоровье"), new OtherMenuList(19, 71, ""), new OtherMenuList(60, 59, "5 минут|10 минут|1 час|3 часа"), new OtherMenuList(61, 71, "Малый|Средний|Большой"), new OtherMenuList(206, 71, "Всегда|При остановке|Никогда"), new OtherMenuList(209, 71, "Все|Приваты и Отряд|Клан и Отряд|Ничего"), new OtherMenuList(236, 71, "Всегда|Только текст|Только значки|Никогда"), new OtherMenuList(66, 37, ""), new OtherMenuList(67, 4, ""), new OtherMenuList(52, 63, ""), new OtherMenuList(68, 67, ""), new OtherMenuList(215, 48, ""), new OtherMenuList(69, 67, "Посмотреть|Переименовать|Удалить"), new OtherMenuList(190, -2, ""), new OtherMenuList(191, 190, ""), new OtherMenuList(196, -2, ""), new OtherMenuList(199, -2, ""), new OtherMenuList(200, 4, ""), new OtherMenuList(235, -2, "Следить|Отказаться"), new OtherMenuList(226, -2, "Оценить|Позже"), new OtherMenuList(227, -2, "Поделиться|Отлично!"), TradeMenuList.instance};
      instance = new MenuManager();
   }
}
