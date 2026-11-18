package Moduls.chat;

import Base.NetRequest;
import Base.ChatWindow;
import Base.Com;
import Engine.Mystery;
import desktop.PlatformRuntime;
import Moduls.Strategist;
import Resources.StringResources;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import desktop.storage.RecordStore;
import desktop.storage.RecordStoreException;

public final class Chat {
   private static long[] TIMES_GROUPS_DELTA_MS = new long[]{1800000L, 3600000L, 10800000L, 28800000L, 86400000L, 172800000L, 345600000L, 604800000L, 1209600000L, 2592000000L, 31536000000L};
   private static String[] TIME_GROUPS_NAMES;
   public static final Chat instance;
   public static final ChatWindow chatWindow;
   private StringBuffer stringBufferForCreateFlow = new StringBuffer();
   private Vector commonThreadMessages = new Vector();
   private Vector clanMessages = new Vector();
   private Vector partyMessages = new Vector();
   private Vector privateMessages = new Vector();
   private boolean activeChatPrivateThread = false;
   private int activeChatPrivateThreadWithPlayerId = -1;
   private String activeChatPrivateThreadWithPlayerName = "";
   private boolean activeChatPrivateThreadWithPlayerOnline = false;
   private volatile Vector lastWritersIds = null;
   private final Object lastWritersLock = new Object();
   private volatile boolean lastWritersActiveSave = false;
   private volatile boolean lastWritersNeedSave = false;
   private final Object headMessagesLock = new Object();
   private Hashtable headMessagesHas = new Hashtable();
   private Vector headMessages = new Vector();
   private final Object markPrivatesLock = new Object();
   private boolean markPrivatesHas = false;
   private Vector markPrivatesList = new Vector();
   private volatile boolean unreadnCommon = false;
   private volatile boolean unreadnParty = false;
   private volatile boolean unreadnClan = false;
   private final Hashtable unreadnPrivatesList = new Hashtable();
   private long deltaTimeClientServer = 0L;
   private boolean[] showInCommonTypes = new boolean[]{true, true, true, true};
   private ChatMessage forBattleMessage = null;

   private Chat() {
   }

   public final void setDeltaTimeClientServer(long deltaTimeClientServer2) {
      this.deltaTimeClientServer = deltaTimeClientServer2;
   }

   public final void setShowInCommonFlags(boolean showPrivates, boolean showClan, boolean showParty) {
      this.showInCommonTypes[2] = showPrivates;
      this.showInCommonTypes[1] = showClan;
      this.showInCommonTypes[3] = showParty;
      if (chatWindow.isActive && chatWindow.getChannelType() == 0) {
         chatWindow.onVisibleChatContentChanged();
      }

   }

   public final ChatMessage pullForBattleMessage() {
      ChatMessage forBattleMessage2 = this.forBattleMessage;
      this.forBattleMessage = null;
      return forBattleMessage2;
   }

   public final boolean isUnreadnPrivates() {
      return this.unreadnPrivatesList.size() > 0;
   }

   public final boolean isUnreadnCommon() {
      return this.unreadnCommon;
   }

   public final boolean isUnreadnParty() {
      return this.unreadnParty;
   }

   public final boolean isUnreadnClan() {
      return this.unreadnClan;
   }

   public final void incomeUnreadnPrivates(int[] ids) {
      this.unreadnPrivatesList.clear();

      for(int idIndex = 0; idIndex < ids.length; ++idIndex) {
         int playerId = ids[idIndex];
         this.unreadnPrivatesList.put(playerId, new Object());
      }

      if (ids.length > 0) {
         blinkChatChannel(2);
      }

   }

   private void removeUnreadnPrivate(int id) {
      this.unreadnPrivatesList.remove(id);
      blinkChatChannel(-1);
   }

   private void addUnreadnPrivate(int id) {
      this.unreadnPrivatesList.put(id, new Object());
      blinkChatChannel(2);
   }

   public final void addHeadMessage(ChatMessage chatMessage) {
      synchronized(this.headMessagesLock) {
         if (this.headMessagesHas.get(chatMessage.playerId) != null) {
            ChatMessage chatMessage2 = (ChatMessage)this.headMessagesHas.remove(chatMessage.playerId);
            this.headMessages.removeElement(chatMessage2);
         }

         this.headMessagesHas.put(chatMessage.playerId, chatMessage);
         this.headMessages.addElement(chatMessage);
      }
   }

   public final ChatMessage findHeadMessage(int playerId) {
      synchronized(this.headMessagesLock) {
         return (ChatMessage)this.headMessagesHas.get(playerId);
      }
   }

   public final void processHeadMessages() {
      synchronized(this.headMessagesLock) {
         while(this.headMessages.size() > 0 && Mystery.currentTimeMillis - ((ChatMessage)this.headMessages.elementAt(0)).creationTime >= 5000L) {
            ChatMessage chatMessage = (ChatMessage)this.headMessages.elementAt(0);
            this.headMessages.removeElementAt(0);
            this.headMessagesHas.remove(chatMessage.playerId);
         }

      }
   }

   private static RecordStore openCache(String name, boolean create) {
      try { return RecordStore.openRecordStore(name, create); }
      catch (RecordStoreException exception) { throw decompiledsupport.UncheckedDataInput.unchecked(exception); }
   }

   private void initLastWriters() {
      if (this.lastWritersIds == null) {
         synchronized(this.lastWritersLock) {
            if (this.lastWritersIds == null) {
               Vector vector3 = new Vector();
               RecordStore recordStore;
               if ((recordStore = openCache("ll_last_writers", true)).getNumRecords() > 0) {
                  byte[] bytes = recordStore.getRecord(1);
                  DataInputStream dataInputStream;
                  int integerCode = decompiledsupport.UncheckedDataInput.readInt((dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes))));

                  for(int integerCodeIndex = 0; integerCodeIndex < integerCode; ++integerCodeIndex) {
                     vector3.addElement(new Integer(decompiledsupport.UncheckedDataInput.readInt(dataInputStream)));
                  }
               }

               this.lastWritersIds = vector3;
            }
         }
      }
   }

   public final int[] getLastWriters() {
      this.initLastWriters();
      synchronized(this.lastWritersLock) {
         int[] res = new int[this.lastWritersIds.size()];

         for(int i = 0; i < this.lastWritersIds.size(); ++i) {
            Integer integer = (Integer)this.lastWritersIds.elementAt(i);
            res[i] = integer;
         }

         return res;
      }
   }

   public final void addLastWriterFromUncommonThread(int id) {
      this.initLastWriters();
      synchronized(this.lastWritersLock) {
         for(int i = 0; i < this.lastWritersIds.size(); ++i) {
            if ((Integer)this.lastWritersIds.elementAt(i) == id) {
               this.lastWritersIds.removeElementAt(i);
               break;
            }
         }

         this.lastWritersIds.addElement(id);
         if (this.lastWritersIds.size() > 30) {
            this.lastWritersIds.removeElementAt(0);
         }

         if (!this.lastWritersActiveSave) {
            this.lastWritersActiveSave = true;
            PlatformRuntime.withLowPriority(new Thread(new ChatHistorySaveTask(this))).start();
         } else {
            this.lastWritersNeedSave = true;
         }

      }
   }

   public final int[] pullMarkPrivatesList() {
      synchronized(this.markPrivatesLock) {
         if (!this.markPrivatesHas) {
            return null;
         } else {
            this.markPrivatesHas = false;
            int[] res = new int[this.markPrivatesList.size()];

            for(int i = 0; i < res.length; ++i) {
               res[i] = (Integer)this.markPrivatesList.elementAt(i);
            }

            this.markPrivatesList.removeAllElements();
            return res;
         }
      }
   }

   public final void incomeMessage(ChatMessage chatMessage) {
      boolean addToCommon = insertChatMessage(chatMessage, this.commonThreadMessages, 150);
      switch (chatMessage.channelType) {
         case 0:
            if (addToCommon) {
               if (chatWindow.isActive && chatWindow.getChannelType() == chatMessage.channelType) {
                  chatWindow.onVisibleChatContentChanged();
                  return;
               }

               this.unreadnCommon = true;
               blinkChatChannel(1);
               return;
            }
            break;
         case 1:
            if (insertChatMessage(chatMessage, this.clanMessages, 50)) {
               if (!chatWindow.isActive || chatWindow.getChannelType() != chatMessage.channelType && (chatWindow.getChannelType() != 0 || !this.showInCommonTypes[1])) {
                  this.unreadnClan = true;
                  blinkChatChannel(3);
                  return;
               }

               chatWindow.onVisibleChatContentChanged();
               return;
            }
            break;
         case 2:
            if (!this.activeChatPrivateThread || this.activeChatPrivateThreadWithPlayerId != chatMessage.channelId1 && this.activeChatPrivateThreadWithPlayerId != chatMessage.channelId2) {
               if (chatWindow.isActive && chatWindow.getChannelType() == 0 && this.showInCommonTypes[2]) {
                  this.tryMarkIncomePrivateMessageAsReadn(chatMessage);
                  chatWindow.onVisibleChatContentChanged();
                  return;
               }

               if (addToCommon && chatMessage.playerId != Strategist.instance.id) {
                  this.addUnreadnPrivate(chatMessage.getPrivateOponentId(Strategist.instance.id));
               }
            } else if (insertChatMessage(chatMessage, this.privateMessages, chatMessage.channelId1 != -1 && chatMessage.channelId2 != -1 ? 50 : 100)) {
               if (chatWindow.isActive && chatWindow.getChannelType() == chatMessage.channelType && (chatWindow.getChannelId() == chatMessage.channelId1 || chatWindow.getChannelId() == chatMessage.channelId2)) {
                  this.tryMarkIncomePrivateMessageAsReadn(chatMessage);
                  chatWindow.onVisibleChatContentChanged();
                  return;
               }

               if (chatMessage.playerId != Strategist.instance.id) {
                  this.addUnreadnPrivate(chatMessage.getPrivateOponentId(Strategist.instance.id));
                  return;
               }
            }
            break;
         case 3:
            if (insertChatMessage(chatMessage, this.partyMessages, 50)) {
               this.forBattleMessage = chatMessage;
               if (!chatWindow.isActive || chatWindow.getChannelType() != chatMessage.channelType && (chatWindow.getChannelType() != 0 || !this.showInCommonTypes[3])) {
                  this.unreadnParty = true;
                  blinkChatChannel(4);
                  return;
               }

               chatWindow.onVisibleChatContentChanged();
               return;
            }
      }

   }

   private void tryMarkIncomePrivateMessageAsReadn(ChatMessage chatMessage) {
      if (chatMessage.playerId != Strategist.instance.id) {
         synchronized(this.markPrivatesLock) {
            this.markPrivatesHas = true;
            Integer integer = new Integer(chatMessage.getPrivateOponentId(Strategist.instance.id));
            if (!this.markPrivatesList.contains(integer)) {
               this.markPrivatesList.addElement(integer);
            }

         }
      }
   }

   private static boolean insertChatMessage(ChatMessage chatMessage, Vector vector3, int maxCount) {
      for(int vector3Count = vector3.size() - 1; vector3Count >= 0; --vector3Count) {
         ChatMessage chatMessage2;
         if ((chatMessage2 = (ChatMessage)vector3.elementAt(vector3Count)).time == chatMessage.time && chatMessage2.channelType == chatMessage.channelType && chatMessage2.playerId == chatMessage.playerId && chatMessage2.channelId1 == chatMessage.channelId1 && chatMessage2.channelId2 == chatMessage.channelId2 && chatMessage2.message.equals(chatMessage.message)) {
            return false;
         }

         if (chatMessage2.time <= chatMessage.time) {
            vector3.insertElementAt(chatMessage, vector3Count + 1);
            if (vector3.size() > maxCount) {
               vector3.removeElementAt(0);
            }

            return true;
         }
      }

      vector3.insertElementAt(chatMessage, 0);
      if (vector3.size() > maxCount) {
         vector3.removeElementAt(0);
      }

      return true;
   }

   public static void sendMessage(String messageText, int channelType, int channelId) {
      try {
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1421000);
         netRequest.request.writeInt(channelType);
         netRequest.request.writeInt(channelId);
         netRequest.request.writeUTF(messageText);
         Com.sendRequest(netRequest);
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   private static void blinkChatChannel(int blinkPostInd) {
      if (blinkPostInd > 0) {
         switch (blinkPostInd) {
            case 1:
               Com.GameTh.postBlinkCommon();
               return;
            case 2:
               Com.GameTh.postBlinkPrivates();
               return;
            case 3:
               Com.GameTh.postBlinkClan();
               return;
            case 4:
               Com.GameTh.postBlinkParty();
         }
      }

   }

   public final void onCloseChatWindow() {
      this.activeChatPrivateThread = false;
      this.privateMessages.removeAllElements();
   }

   public static void refreshChatHeader() {
      if (chatWindow.isActive) {
         chatWindow.onHeaderDependentDataChanged();
      }

   }

   public final boolean startPrivateChat(String playerName) {
      boolean res = false;
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.request.writeInt(4500010);
         netRequest.request.writeUTF(playerName);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4500011) {
            int activeChatPrivateThreadWithPlayerId2 = netRequest.response.readInt();
            this.privateMessages.removeAllElements();
            int integerCode = netRequest.response.readInt();

            for(int integerCodeIndex = 0; integerCodeIndex < integerCode; ++integerCodeIndex) {
               this.privateMessages.addElement(new ChatMessage(netRequest.response));
            }

            this.activeChatPrivateThread = true;
            this.activeChatPrivateThreadWithPlayerId = activeChatPrivateThreadWithPlayerId2;
            this.activeChatPrivateThreadWithPlayerName = playerName;
            this.activeChatPrivateThreadWithPlayerOnline = netRequest.response.readBoolean();
            this.removeUnreadnPrivate(activeChatPrivateThreadWithPlayerId2);
            chatWindow.activate(1, 2, activeChatPrivateThreadWithPlayerId2);
            res = true;
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
      return res;
   }

   public final void startPrivateChat(int playerId) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.request.writeInt(4500020);
         netRequest.request.writeInt(playerId);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4500021) {
            String playerName = netRequest.response.readUTF();
            this.privateMessages.removeAllElements();
            int integerCode = netRequest.response.readInt();

            for(int integerCodeIndex = 0; integerCodeIndex < integerCode; ++integerCodeIndex) {
               this.privateMessages.addElement(new ChatMessage(netRequest.response));
            }

            this.activeChatPrivateThread = true;
            this.activeChatPrivateThreadWithPlayerId = playerId;
            this.activeChatPrivateThreadWithPlayerName = playerName;
            this.activeChatPrivateThreadWithPlayerOnline = netRequest.response.readBoolean();
            this.removeUnreadnPrivate(playerId);
            chatWindow.activate(1, 2, playerId);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
   }

   public final void setUnreadnParty() {
      this.unreadnParty = false;
      blinkChatChannel(-1);
      chatWindow.activate(3, 3, -1);
   }

   public final void startCommonChat() {
      this.unreadnCommon = false;
      blinkChatChannel(-1);
      chatWindow.activate(1, 0, -1);
   }

   public final void setUnreadnParty2() {
      this.unreadnParty = false;
      blinkChatChannel(-1);
      chatWindow.activate(1, 3, -1);
   }

   public final void startClanChat() {
      this.unreadnClan = false;
      blinkChatChannel(-1);
      chatWindow.activate(1, 1, -1);
   }

   public final String createHeader(int channelType) {
      switch (channelType) {
         case 0:
            return Mystery.stringFormat(Mystery.stringFormat("% ($ рядом)", Com.currentGameMap.nameLiteral, '%'), Com.getNearbyPlayerCountText(), '$');
         case 1:
            return Mystery.stringFormat(Mystery.stringFormat("% ($ героев из клана)", Com.currentGameMap.nameLiteral, '%'), String.valueOf(Com.playersInClanChat), '$');
         case 2:
            return Mystery.stringFormat("% ПРИВАТ (%):", new String[]{this.activeChatPrivateThreadWithPlayerName, this.activeChatPrivateThreadWithPlayerOnline ? "онлайн" : "оффлайн"});
         case 3:
            return Mystery.stringFormat(Mystery.stringFormat("% ($ героев в отряде)", Com.currentGameMap.nameLiteral, '%'), String.valueOf(Strategist.instance.partyInfo.length), '$');
         default:
            return "";
      }
   }

   public final String createChatFlowText(int channelType) {
      Vector vector3;
      switch (channelType) {
         case 0:
            vector3 = this.commonThreadMessages;
            break;
         case 1:
            vector3 = this.clanMessages;
            break;
         case 2:
            vector3 = this.privateMessages;
            break;
         case 3:
            vector3 = this.partyMessages;
            break;
         default:
            return "";
      }

      this.stringBufferForCreateFlow.setLength(0);
      int stringBufferForCreateFlow2 = 0;
      long timeGroupLimit = TIMES_GROUPS_DELTA_MS[0];
      long deltaTimeClientServer2 = (new Date()).getTime() - this.deltaTimeClientServer;
      ChatMessage chatMessage;
      long[] res;
      if (channelType == 0) {
         for(channelType = vector3.size() - 1; channelType >= 0; --channelType) {
            if ((chatMessage = (ChatMessage)vector3.elementAt(channelType)).channelType == 0) {
               stringBufferForCreateFlow2 = (int)(res = tryAddTimeMarkBeforeMessage(chatMessage, this.stringBufferForCreateFlow, stringBufferForCreateFlow2, timeGroupLimit, deltaTimeClientServer2))[0];
               timeGroupLimit = res[1];
               if (this.stringBufferForCreateFlow.length() > 0) {
                  this.stringBufferForCreateFlow.append("\n");
               }

               if (chatMessage.playerGender >= 0 && chatMessage.playerRace >= 0 && chatMessage.playerLevel >= 0) {
                  this.stringBufferForCreateFlow.append("[").append(StringResources.c_chat_sex_symb[chatMessage.playerGender]).append(StringResources.c_chat_class_symb[chatMessage.playerRace]).append(chatMessage.playerLevel).append("]");
               }

               this.stringBufferForCreateFlow.append(chatMessage.playerName).append("»").append(chatMessage.message);
            } else if (this.showInCommonTypes[chatMessage.channelType]) {
               stringBufferForCreateFlow2 = (int)(res = tryAddTimeMarkBeforeMessage(chatMessage, this.stringBufferForCreateFlow, stringBufferForCreateFlow2, timeGroupLimit, deltaTimeClientServer2))[0];
               timeGroupLimit = res[1];
               if (this.stringBufferForCreateFlow.length() > 0) {
                  this.stringBufferForCreateFlow.append("\n");
               }

               this.stringBufferForCreateFlow.append(StringResources.c_chat_channels_names_new[chatMessage.channelType]).append("»").append(chatMessage.playerName).append(" ").append(chatMessage.message);
            }
         }
      } else {
         for(channelType = vector3.size() - 1; channelType >= 0; --channelType) {
            stringBufferForCreateFlow2 = (int)(res = tryAddTimeMarkBeforeMessage(chatMessage = (ChatMessage)vector3.elementAt(channelType), this.stringBufferForCreateFlow, stringBufferForCreateFlow2, timeGroupLimit, deltaTimeClientServer2))[0];
            timeGroupLimit = res[1];
            if (this.stringBufferForCreateFlow.length() > 0) {
               this.stringBufferForCreateFlow.append("\n");
            }

            if (chatMessage.playerGender >= 0 && chatMessage.playerRace >= 0 && chatMessage.playerLevel >= 0) {
               this.stringBufferForCreateFlow.append("[").append(StringResources.c_chat_sex_symb[chatMessage.playerGender]).append(StringResources.c_chat_class_symb[chatMessage.playerRace]).append(chatMessage.playerLevel).append("]");
            }

            this.stringBufferForCreateFlow.append(chatMessage.playerName).append("»").append(chatMessage.message);
         }
      }

      return this.stringBufferForCreateFlow.toString();
   }

   private static long[] tryAddTimeMarkBeforeMessage(ChatMessage chatMessage, StringBuffer stringBuffer, int waitDeltaInd, long waitDeltaMs, long curTime) {
      if (waitDeltaInd >= 0 && curTime - chatMessage.time >= waitDeltaMs) {
         while(waitDeltaInd >= 0 && curTime - chatMessage.time >= waitDeltaMs) {
            ++waitDeltaInd;
            if (waitDeltaInd >= 11) {
               waitDeltaInd = -1;
            } else {
               waitDeltaMs = TIMES_GROUPS_DELTA_MS[waitDeltaInd];
            }
         }

         if (stringBuffer.length() > 0) {
            stringBuffer.append("\n");
         }

         stringBuffer.append(TIME_GROUPS_NAMES[waitDeltaInd > 0 ? waitDeltaInd - 1 : TIME_GROUPS_NAMES.length - 1]);
      }

      return new long[]{(long)waitDeltaInd, waitDeltaMs};
   }


   static Object getLastWritersLock(Chat chat2) {
      return chat2.lastWritersLock;
   }


   static Vector getLastWritersIds(Chat chat2) {
      return chat2.lastWritersIds;
   }


   static boolean isLastWritersSaveNeeded(Chat chat2) {
      return chat2.lastWritersNeedSave;
   }


   static boolean clearLastWritersSaveActive(Chat chat2, boolean ignoredState) {
      return chat2.lastWritersActiveSave = false;
   }


   static boolean clearLastWritersSaveNeeded(Chat chat2, boolean ignoredState) {
      return chat2.lastWritersNeedSave = false;
   }

   static {
      TIME_GROUPS_NAMES = StringResources.TIME_GROUPS_NAMES;
      instance = new Chat();
      chatWindow = new ChatWindow();
   }
}
