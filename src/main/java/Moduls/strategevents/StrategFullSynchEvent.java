package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.Higher;
import Moduls.InventoryItem;
import Moduls.chat.Chat;
import Moduls.quests.QuestStrateg;
import java.io.DataInputStream;

public final class StrategFullSynchEvent extends StrategSendEvent {
   private Strategist strategist;

   protected StrategFullSynchEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.strategist = new Strategist(0, 0, 0, 0);
      Strategist strategist2 = this.strategist;
      DataInputStream dataInputStream2 = dataInputStream;
      Strategist strategist3 = strategist2;
      strategist2.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.GP_Name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      strategist3.GP_Race = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      strategist3.GP_Level = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.GP_Money = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.GP_Heads = 0;
      strategist3.GP_Exp = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.GP_SkillPoints = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.GP_Prisoners = 0;
      strategist3.cx = (short)decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.cy = (short)decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.curMapInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.curMapId = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      strategist3.GP_AboutMe = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      strategist3.GP_Crystals = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.avatarGraphic.readFromNet(dataInputStream);
      strategist3.atackSprite = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.atackBaseX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.atackBaseY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      strategist3.atackLatency = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.GP_Temperature = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      strategist3.GP_TemperTime = Integer.MAX_VALUE;
      strategist3.GP_AddTemperature = 0;
      strategist3.GP_NextLevelExp = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      strategist3.GP_SelfSkillsLevels = new int[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      for(int gP_SelfSkillsLevelIndex = 0; gP_SelfSkillsLevelIndex < strategist3.GP_SelfSkillsLevels.length; ++gP_SelfSkillsLevelIndex) {
         strategist3.GP_SelfSkillsLevels[gP_SelfSkillsLevelIndex] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream2);
      }

      strategist3.GP_Army.removeAllElements();
      byte byteCode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      strategist3.GP_Highers.removeAllElements();

      int gP_AddSkillsLevelIndex;
      for(gP_AddSkillsLevelIndex = 0; gP_AddSkillsLevelIndex < byteCode; ++gP_AddSkillsLevelIndex) {
         strategist3.GP_Highers.addElement(new Higher());
         ((Higher)strategist3.GP_Highers.lastElement()).LoadFromStream(dataInputStream2);
      }

      strategist3.GP_FireLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.GP_IceLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.GP_HeroLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.GP_Gender = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      strategist3.vipStatus = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      strategist3.vipStatusEndTime = decompiledsupport.UncheckedDataInput.readLong(dataInputStream2);
      strategist3.protectionStatus = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.protectionStatusEndTime = decompiledsupport.UncheckedDataInput.readLong(dataInputStream2);
      strategist3.buyedBagSize = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      DataInputStream dataInputStream3 = dataInputStream2;
      Strategist strategist4 = strategist3;
      strategist3.items.removeAllElements();
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream2);

      for(int shortCodeIndex = 0; shortCodeIndex < shortCode; ++shortCodeIndex) {
         InventoryItem inventoryItem2 = Strategist.loadIndventoryItem(dataInputStream3);
         strategist4.items.addElement(inventoryItem2);
      }

      strategist4.GP_MaxTroops = decompiledsupport.UncheckedDataInput.readInt(dataInputStream3);
      strategist3.avatarLife = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarHit = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarSpeed = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarDmg = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarLifeCur = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarHitCur = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarSpeedCur = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarDmgCur = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarArmor = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarAtack = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.maxIceLvl = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarWalkSpeed = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarAtackDistance = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.maxFireLvl = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.hasMaster = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.GP_Life = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.avatarRegeneration = decompiledsupport.UncheckedDataInput.readInt(dataInputStream2);
      strategist3.isModer = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.GP_AddSkillsLevels = new int[decompiledsupport.UncheckedDataInput.readByte(dataInputStream2)];

      for(gP_AddSkillsLevelIndex = 0; gP_AddSkillsLevelIndex < strategist3.GP_AddSkillsLevels.length; ++gP_AddSkillsLevelIndex) {
         strategist3.GP_AddSkillsLevels[gP_AddSkillsLevelIndex] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream2);
      }

      strategist3.isAutoCancelTrade = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.isAutoCancelFights = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.isAutoCancelParty = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.isAutoFixItems = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.commonChatShowTypes = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      strategist3.guiMode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      strategist3.guiDrawMiniMap = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2);
      strategist3.compassMode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream2);
      QuestStrateg.instance.loadFromStreamJustQuests(dataInputStream2, true);
      QuestStrateg.instance.loadFromStreamJustDone(dataInputStream2, true);
   }

   public final void applyToStrategist() {
      Strategist instance2 = Strategist.instance;
      Strategist strategist2 = this.strategist;
      instance2.curMapInd = strategist2.curMapInd;
      instance2.curMapId = strategist2.curMapId;
      instance2.id = strategist2.id;
      instance2.GP_Name = strategist2.GP_Name;
      instance2.GP_Race = strategist2.GP_Race;
      instance2.unstableCount = strategist2.unstableCount;
      instance2.GP_Level = strategist2.GP_Level;
      instance2.GP_Money = strategist2.GP_Money;
      instance2.GP_Heads = strategist2.GP_Heads;
      instance2.GP_Exp = strategist2.GP_Exp;
      instance2.GP_SkillPoints = strategist2.GP_SkillPoints;
      instance2.GP_Prisoners = strategist2.GP_Prisoners;
      instance2.cx = strategist2.cx;
      instance2.cy = strategist2.cy;
      instance2.avatarWalkSpeed = strategist2.avatarWalkSpeed;
      instance2.avatarGraphic = strategist2.avatarGraphic;
      instance2.atackSprite = strategist2.atackSprite;
      instance2.atackBaseX = strategist2.atackBaseX;
      instance2.atackBaseY = strategist2.atackBaseY;
      instance2.atackLatency = strategist2.atackLatency;
      instance2.GP_AboutMe = strategist2.GP_AboutMe;
      instance2.GP_Crystals = strategist2.GP_Crystals;
      instance2.GP_Temperature = strategist2.GP_Temperature;
      instance2.GP_TemperTime = strategist2.GP_TemperTime;
      instance2.GP_AddTemperature = strategist2.GP_AddTemperature;
      instance2.GP_NextLevelExp = strategist2.GP_NextLevelExp;
      instance2.GP_SelfSkillsLevels = strategist2.GP_SelfSkillsLevels;
      instance2.GP_AddSkillsLevels = strategist2.GP_AddSkillsLevels;
      instance2.GP_Army = strategist2.GP_Army;
      instance2.GP_Highers = strategist2.GP_Highers;
      instance2.GP_FireLevel = strategist2.GP_FireLevel;
      instance2.GP_IceLevel = strategist2.GP_IceLevel;
      instance2.GP_HeroLevel = strategist2.GP_HeroLevel;
      instance2.protectionStatus = strategist2.protectionStatus;
      instance2.protectionStatusEndTime = strategist2.protectionStatusEndTime;
      instance2.vipStatus = strategist2.vipStatus;
      instance2.vipStatusEndTime = strategist2.vipStatusEndTime;
      instance2.buyedBagSize = strategist2.buyedBagSize;
      instance2.GP_Gender = strategist2.GP_Gender;
      instance2.items = strategist2.items;
      instance2.GP_MaxTroops = strategist2.GP_MaxTroops;
      instance2.avatarLife = strategist2.avatarLife;
      instance2.avatarHit = strategist2.avatarHit;
      instance2.avatarSpeed = strategist2.avatarSpeed;
      instance2.avatarLifeCur = strategist2.avatarLifeCur;
      instance2.avatarHitCur = strategist2.avatarHitCur;
      instance2.avatarSpeedCur = strategist2.avatarSpeedCur;
      instance2.avatarArmor = strategist2.avatarArmor;
      instance2.maxIceLvl = strategist2.maxIceLvl;
      instance2.avatarAtack = strategist2.avatarAtack;
      instance2.avatarDmg = strategist2.avatarDmg;
      instance2.avatarDmgCur = strategist2.avatarDmgCur;
      instance2.hasMaster = strategist2.hasMaster;
      instance2.GP_Life = strategist2.GP_Life;
      instance2.avatarRegeneration = strategist2.avatarRegeneration;
      instance2.petH = strategist2.petH;
      instance2.petB = strategist2.petB;
      instance2.commonChatShowTypes = strategist2.commonChatShowTypes;
      instance2.isAutoFixItems = strategist2.isAutoFixItems;
      instance2.isAutoCancelFights = strategist2.isAutoCancelFights;
      instance2.isAutoCancelTrade = strategist2.isAutoCancelTrade;
      instance2.isAutoCancelParty = strategist2.isAutoCancelParty;
      instance2.guiMode = strategist2.guiMode;
      instance2.compassMode = strategist2.compassMode;
      instance2.guiDrawMiniMap = strategist2.guiDrawMiniMap;
      instance2.isModer = strategist2.isModer;
      instance2.trades = strategist2.trades;
      instance2.partyInfo = strategist2.partyInfo;
      instance2.avatarAtackDistance = strategist2.avatarAtackDistance;
      instance2.maxFireLvl = strategist2.maxFireLvl;
      instance2.CalculateSkills();
      Chat.instance.setShowInCommonFlags(Strategist.instance.commonChatShowTypes == 0 || Strategist.instance.commonChatShowTypes == 1 || Strategist.instance.commonChatShowTypes == 2, Strategist.instance.commonChatShowTypes == 0 || Strategist.instance.commonChatShowTypes == 2, Strategist.instance.commonChatShowTypes == 0 || Strategist.instance.commonChatShowTypes == 1);
   }
}
