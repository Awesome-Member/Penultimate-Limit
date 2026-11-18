package Moduls.otherheroes;

import Base.GameWalkPathElement;
import Moduls.chat.Chat;
import Moduls.chat.ChatMessage;
import java.io.DataInputStream;
import java.util.Vector;

public final class HeroesOnMapManager {
   public int forMapInd = -1;
   public Vector heroes = new Vector();
   private boolean isOldHeroesTilNextLoad = false;
   private Vector cach = new Vector();
   private int incomeForMapInd;
   private volatile HeroOnMap[] incomeHeroes = null;
   private volatile boolean haveIncomeHeroes = false;
   public static HeroesOnMapManager instance = new HeroesOnMapManager();

   private HeroesOnMapManager() {
   }

   public final boolean isOldHeroesTilNextLoad() {
      return this.isOldHeroesTilNextLoad;
   }

   public final void markHeroesAsOldTilNextLoad() {
      this.isOldHeroesTilNextLoad = true;
   }

   private void free(HeroOnMap heroOnMap) {
      this.cach.addElement(heroOnMap);
   }

   public final boolean isFreeForLoad() {
      return !this.haveIncomeHeroes;
   }

   public final void loadHeroes(DataInputStream dataInputStream) {
      this.incomeForMapInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.incomeHeroes = new HeroOnMap[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      try {
         for(int incomeHeroeIndex = 0; incomeHeroeIndex < this.incomeHeroes.length; ++incomeHeroeIndex) {
            HeroOnMap heroOnMap;
            if (this.cach.size() > 0) {
               HeroOnMap heroOnMap2 = (HeroOnMap)this.cach.elementAt(this.cach.size() - 1);
               this.cach.removeElementAt(this.cach.size() - 1);
               heroOnMap2.clear();
               heroOnMap = heroOnMap2;
            } else {
               heroOnMap = new HeroOnMap();
            }

            HeroOnMap heroOnMap3 = heroOnMap;
            this.incomeHeroes[incomeHeroeIndex] = heroOnMap3;
            heroOnMap3.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            ChatMessage chatMessage;
            if ((chatMessage = Chat.instance.findHeadMessage(heroOnMap3.id)) != null) {
               heroOnMap3.headMessage = chatMessage;
            }

            byte incomeFlags = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
            heroOnMap3.incomeFlags = incomeFlags;
            if ((incomeFlags & 1) > 0) {
               heroOnMap3.path = new Vector();
               heroOnMap3.incomeIsFull = true;
               heroOnMap3.incomeResetPath = true;
               heroOnMap3.loadFromNet(dataInputStream);
            } else {
               byte byteCode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
               heroOnMap3.path = new Vector();

               for(int byteCodeIndex = 0; byteCodeIndex < byteCode; ++byteCodeIndex) {
                  GameWalkPathElement gameWalkPathElement;
                  if ((gameWalkPathElement = new GameWalkPathElement(decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readByte(dataInputStream))).watchDirection < 0) {
                     gameWalkPathElement.watchDirection = (byte)(-gameWalkPathElement.watchDirection);
                     heroOnMap3.incomeResetPath = true;
                     heroOnMap3.cx = Math.abs(gameWalkPathElement.x);
                     heroOnMap3.cy = Math.abs(gameWalkPathElement.y);
                     heroOnMap3.watchDirection = gameWalkPathElement.watchDirection;
                     if (heroOnMap3.path.size() > 0) {
                        heroOnMap3.path.removeAllElements();
                     }
                  }

                  heroOnMap3.path.addElement(gameWalkPathElement);
               }

               if ((incomeFlags & 2) > 0) {
                  heroOnMap3.incomeIsFull = true;
                  heroOnMap3.loadFromNet(dataInputStream);
               }
            }

            heroOnMap3.availAtack = (incomeFlags & 4) > 0;
            heroOnMap3.swords = (incomeFlags & 8) == 0 ? 0 : ((incomeFlags & 16) == 0 ? 2 : 1);
            if ((incomeFlags & 32) != 0) {
               heroOnMap3.incomeFightingData = true;
               heroOnMap3.fightingWithMobInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
            }
         }
      } finally {
         this.haveIncomeHeroes = true;
      }

   }

   public final void applyHeroes() {
      if (this.haveIncomeHeroes) {
         try {
            boolean resetPathBecouseMapChange = this.forMapInd != this.incomeForMapInd;
            this.forMapInd = this.incomeForMapInd;
            Vector heroes2 = this.heroes;
            this.heroes = new Vector();

            int incomeHeroeIndex;
            for(incomeHeroeIndex = 0; incomeHeroeIndex < this.incomeHeroes.length; ++incomeHeroeIndex) {
               HeroOnMap heroOnMap = this.incomeHeroes[incomeHeroeIndex];
               HeroOnMap heroOnMap2 = null;

               int sizeIndex;
               for(sizeIndex = 0; sizeIndex < heroes2.size(); ++sizeIndex) {
                  HeroOnMap heroOnMap3;
                  if ((heroOnMap3 = (HeroOnMap)heroes2.elementAt(sizeIndex)).id == heroOnMap.id) {
                     heroOnMap2 = heroOnMap3;
                     heroes2.removeElementAt(sizeIndex);
                     break;
                  }
               }

               if (heroOnMap2 == null) {
                  if (heroOnMap.incomeIsFull) {
                     this.heroes.addElement(heroOnMap);
                  } else {
                     this.free(heroOnMap);
                  }
               } else {
                  this.heroes.addElement(heroOnMap2);
                  this.free(heroOnMap);
                  heroOnMap2.headMessage = heroOnMap.headMessage;
                  heroOnMap2.availAtack = heroOnMap.availAtack;
                  heroOnMap2.swords = heroOnMap.swords;
                  if (heroOnMap.incomeFightingData) {
                     heroOnMap2.fightingWithMobInd = heroOnMap.fightingWithMobInd;
                  }

                  if (resetPathBecouseMapChange) {
                     heroOnMap2.clearPath();
                     if (heroOnMap.path.size() > 0) {
                        GameWalkPathElement gameWalkPathElement = (GameWalkPathElement)heroOnMap.path.elementAt(0);
                        heroOnMap2.cx = Math.abs(gameWalkPathElement.x);
                        heroOnMap2.cy = Math.abs(gameWalkPathElement.y);
                        heroOnMap2.watchDirection = gameWalkPathElement.watchDirection;
                     } else if (!heroOnMap.incomeIsFull) {
                        heroOnMap2.cx = -1;
                        heroOnMap2.cy = -1;
                     }
                  }

                  if (heroOnMap.incomeIsFull) {
                     heroOnMap2.copyFromIncomeToWasForFullIncome(heroOnMap);
                  }

                  heroOnMap2.onIncomeLeftPathCount = heroOnMap.path.size();

                  for(sizeIndex = 0; sizeIndex < heroOnMap.path.size(); ++sizeIndex) {
                     heroOnMap2.path.addElement(heroOnMap.path.elementAt(sizeIndex));
                  }
               }
            }

            for(incomeHeroeIndex = 0; incomeHeroeIndex < heroes2.size(); ++incomeHeroeIndex) {
               this.free((HeroOnMap)heroes2.elementAt(incomeHeroeIndex));
            }

            this.incomeHeroes = null;
         } finally {
            this.isOldHeroesTilNextLoad = false;
            this.haveIncomeHeroes = false;
         }
      }
   }
}
