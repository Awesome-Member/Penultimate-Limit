package Moduls.quests;

import Engine.Mystery;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.EnemysMap;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public final class QuestInPlayer {
   public QuestSingle questSingle;
   public long timeLeft = 0L;
   public long timeOnLoad = 0L;
   public QuestInPlayerKillPlayerDesc needToKillPlayer = null;
   public QuestInPlayerKillPlayerPosition needToKillPlayerPos = new QuestInPlayerKillPlayerPosition();
   public boolean isTargetPlayerKilled = false;
   public Hashtable killedMonsters = new Hashtable();
   public Hashtable usedDecorators = new Hashtable();
   public Hashtable usedChests = new Hashtable();
   public Hashtable usedQuesters = new Hashtable();
   private Hashtable visitedQuesters = new Hashtable();
   private int[][] waysItemsDrop = new int[0][];
   private int[][] waysNeedMonsters = new int[0][];
   private int[][] waysNeedDecors = new int[0][];
   private int[][] waysNeedChests = new int[0][];
   private int[][] waysNeedQuesters = new int[0][];
   private int[][] waysNeedItems = new int[0][];
   private int[] waysQuester = new int[0];
   public int[] calcWaysTowns = new int[0];
   public int[] calcWaysEnemys = new int[0];
   public int[] calcWaysDirects = new int[0];
   public short[][] calcWaysToMaps = new short[0][];
   public int calcWaysPlayerId = -1;
   public int calcWaysPlayerPosXServer = -1;
   public int calcWaysPlayerPosYServer = -1;
   public int calcWaysPlayerPosXClient = -1;
   public int calcWaysPlayerPosYClient = -1;
   public boolean calcWaysPlayerVisibleClient = false;
   public long lastTimeTryToFinishWithError = -1L;
   public boolean isDoneCach = false;

   private static long readLongUnchecked(DataInputStream input) {
      try {
         return input.readLong();
      } catch (IOException exception) {
         QuestInPlayer.<RuntimeException>throwUnchecked(exception);
         return 0L;
      }
   }

   private static boolean readBooleanUnchecked(DataInputStream input) {
      try {
         return input.readBoolean();
      } catch (IOException exception) {
         QuestInPlayer.<RuntimeException>throwUnchecked(exception);
         return false;
      }
   }

   private static short readShortUnchecked(DataInputStream input) {
      try {
         return input.readShort();
      } catch (IOException exception) {
         QuestInPlayer.<RuntimeException>throwUnchecked(exception);
         return 0;
      }
   }

   private static String readUTFUnchecked(DataInputStream input) {
      try {
         return input.readUTF();
      } catch (IOException exception) {
         QuestInPlayer.<RuntimeException>throwUnchecked(exception);
         return null;
      }
   }

   private static byte readByteUnchecked(DataInputStream input) {
      try {
         return input.readByte();
      } catch (IOException exception) {
         QuestInPlayer.<RuntimeException>throwUnchecked(exception);
         return 0;
      }
   }

   @SuppressWarnings("unchecked")
   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
   public QuestInPlayer(DataInputStream dataInputStream) {
      this.loadFromStream2(dataInputStream);
   }

   public final boolean tryAddChestUse(String mapName, short townInd, String chestName) {
      return recordRequiredObjectUse((String)mapName, (short)townInd, chestName, (Hashtable)null, (Hashtable)this.usedChests, (QuestSupTextIdsNeed[])this.questSingle.needChests);
   }

   public final boolean tryAddDecoratorUse(String mapName, short townInd, String decoratorName) {
      return recordRequiredObjectUse((String)mapName, (short)townInd, decoratorName, (Hashtable)null, (Hashtable)this.usedDecorators, (QuestSupTextIdsNeed[])this.questSingle.needDecorators);
   }

   public final boolean tryAddQuesterUse(String mapName, short townInd, String questerName) {
      return recordRequiredObjectUse(mapName, townInd, questerName, this.visitedQuesters, this.usedQuesters, this.questSingle.needQuesters);
   }

   private static boolean recordRequiredObjectUse(String mapName, short townInd, String nameId, Hashtable hashtable, Hashtable hashtable2, QuestSupTextIdsNeed[] questNeeds) {
      if (nameId != null && nameId.length() != 0) {
         Vector vector3 = hashtable == null ? null : (Vector)hashtable.get(mapName);
         Vector vector4 = vector3;
         if (vector3 != null && vector4.contains(townInd)) {
            return false;
         } else {
            boolean foundMatchingObject = false;

            int questNeedIndex;
            for(questNeedIndex = 0; questNeedIndex < questNeeds.length; ++questNeedIndex) {
               QuestSupTextIdsNeed questSupTextIdsNeed;
               if ((questSupTextIdsNeed = questNeeds[questNeedIndex]).name.equals(nameId) && Mystery.isContainsStringInArray(questSupTextIdsNeed.maps, mapName)) {
                  foundMatchingObject = true;
               }
            }

            if (!foundMatchingObject) {
               return false;
            } else {
               if (hashtable2.containsKey(nameId)) {
                  questNeedIndex = (Integer)hashtable2.get(nameId) + 1;
                  hashtable2.put(nameId, questNeedIndex);
               } else {
                  hashtable2.put(nameId, 1);
               }

               if (hashtable != null) {
                  if (vector4 == null) {
                     vector4 = new Vector();
                     hashtable.put(mapName, vector4);
                  }

                  vector4.addElement(townInd);
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private void loadFromStream2(DataInputStream dataInputStream) {
      this.questSingle = new QuestSingle(dataInputStream);
      this.timeLeft = readLongUnchecked(dataInputStream);
      this.timeOnLoad = Mystery.currentTimeMillis;
      this.isTargetPlayerKilled = readBooleanUnchecked(dataInputStream);
      this.needToKillPlayer = new QuestInPlayerKillPlayerDesc();
      this.needToKillPlayer.loadFromStream(dataInputStream);
      if (this.needToKillPlayer.needToKillPlayerId >= 0 && !this.isTargetPlayerKilled) {
         this.needToKillPlayerPos.loadFromStream(dataInputStream);
      }

      this.killedMonsters.clear();
      this.usedChests.clear();
      this.usedDecorators.clear();
      this.usedQuesters.clear();
      this.visitedQuesters.clear();
      int shortUncheckedIndex = 0;

      short shortUnchecked;
      for(shortUnchecked = readShortUnchecked(dataInputStream); shortUncheckedIndex < shortUnchecked; ++shortUncheckedIndex) {
         this.killedMonsters.put(readUTFUnchecked(dataInputStream), new Integer(readShortUnchecked(dataInputStream)));
      }

      shortUncheckedIndex = 0;

      for(shortUnchecked = readShortUnchecked(dataInputStream); shortUncheckedIndex < shortUnchecked; ++shortUncheckedIndex) {
         this.usedDecorators.put(readUTFUnchecked(dataInputStream), new Integer(readShortUnchecked(dataInputStream)));
      }

      shortUncheckedIndex = 0;

      for(shortUnchecked = readShortUnchecked(dataInputStream); shortUncheckedIndex < shortUnchecked; ++shortUncheckedIndex) {
         this.usedQuesters.put(readUTFUnchecked(dataInputStream), new Integer(readShortUnchecked(dataInputStream)));
      }

      shortUncheckedIndex = 0;

      for(shortUnchecked = readShortUnchecked(dataInputStream); shortUncheckedIndex < shortUnchecked; ++shortUncheckedIndex) {
         Vector vector3 = new Vector();
         this.visitedQuesters.put(readUTFUnchecked(dataInputStream), vector3);
         int shortUnchecked2Index = 0;

         for(short shortUnchecked2 = readShortUnchecked(dataInputStream); shortUnchecked2Index < shortUnchecked2; ++shortUnchecked2Index) {
            vector3.addElement(new Short(readShortUnchecked(dataInputStream)));
         }
      }

      shortUncheckedIndex = 0;

      for(shortUnchecked = readShortUnchecked(dataInputStream); shortUncheckedIndex < shortUnchecked; ++shortUncheckedIndex) {
         this.usedChests.put(readUTFUnchecked(dataInputStream), new Integer(readShortUnchecked(dataInputStream)));
      }

      this.loadWays(dataInputStream);
   }

   public final void loadWays(DataInputStream dataInputStream) {
      this.waysItemsDrop = loadFromStream3(dataInputStream);
      this.waysNeedMonsters = loadFromStream3(dataInputStream);
      this.waysNeedDecors = loadFromStream3(dataInputStream);
      this.waysNeedChests = loadFromStream3(dataInputStream);
      this.waysNeedQuesters = loadFromStream3(dataInputStream);
      this.waysNeedItems = loadFromStream3(dataInputStream);
      this.waysQuester = loadFromStream(dataInputStream);
   }

   private static int[][] loadFromStream3(DataInputStream dataInputStream) {
      byte byteUnchecked;
      int[][] fromStream3 = new int[byteUnchecked = readByteUnchecked(dataInputStream)][];

      for(int fromStream3Index = 0; fromStream3Index < byteUnchecked; ++fromStream3Index) {
         fromStream3[fromStream3Index] = loadFromStream(dataInputStream);
      }

      return fromStream3;
   }

   public static int[] loadFromStream(DataInputStream dataInputStream) {
      byte byteUnchecked;
      int[] fromStream = new int[byteUnchecked = readByteUnchecked(dataInputStream)];

      for(int fromStreamIndex = 0; fromStreamIndex < byteUnchecked; ++fromStreamIndex) {
         byte byteUnchecked2;
         if ((byteUnchecked2 = readByteUnchecked(dataInputStream)) == 0) {
            fromStream[fromStreamIndex] = readShortUnchecked(dataInputStream) * 10;
         } else {
            fromStream[fromStreamIndex] = byteUnchecked2;
         }
      }

      return fromStream;
   }

   public static int getFullCountForNameMapCountMap(String name, Hashtable hashtable) {
      int count = 0;
      Integer integer;
      if ((integer = (Integer)hashtable.get(name)) != null) {
         count = 0 + integer;
      }

      return count;
   }

   public final void calculateWays(TileMap tileMap, EnemysMap enemysMap, Strategist strategist2) {
      Vector vector3 = new Vector();
      Vector vector4 = new Vector();
      Vector vector5 = new Vector();
      Vector vector6 = new Vector();
      this.calcWaysPlayerId = -1;
      this.isDoneCach = this.questSingle.isDone(strategist2, this);
      int waysQuesterIndex;
      int questerDirectionCount;
      int questerDirection;
      int maximum;
      if (this.isDoneCach) {
         waysQuesterIndex = 0;

         for(questerDirectionCount = this.waysQuester.length; waysQuesterIndex < questerDirectionCount; ++waysQuesterIndex) {
            switch (questerDirection = this.waysQuester[waysQuesterIndex]) {
               case 1:
               case 2:
               case 3:
               case 4:
                  addUniqueDirection(vector5, questerDirection);
                  break;
               case 5:
               default:
                  addUniqueDirection(vector3, questerDirection / 10);
                  break;
               case 6:
                  for(maximum = 0; maximum < tileMap.towns.length; ++maximum) {
                     Town town;
                     if ((town = tileMap.towns[maximum]).type == 409) {
                        if (this.questSingle.backToOtherQuester.length() > 0) {
                           if (town.typeIdName.equals(this.questSingle.backToOtherQuester)) {
                              addUniqueDirection(vector3, maximum);
                           }
                        } else if (town.typeInd == this.questSingle.inMapQuesterInd) {
                           addUniqueDirection(vector3, maximum);
                        }
                     }
                  }
            }
         }

         if (this.questSingle.questerMapWorldCoord != null) {
            addUniqueWorldCoordinate(vector6, this.questSingle.questerMapWorldCoord);
         }

         this.calcWaysTowns = vectorToIntArray(vector3);
         this.calcWaysEnemys = vectorToIntArray(vector4);
         this.calcWaysDirects = vectorToIntArray(vector5);
         this.calcWaysToMaps = vectorToShortShortArray(vector6);
      } else {
         if (this.needToKillPlayerPos != null) {
            waysQuesterIndex = 0;

            for(questerDirectionCount = this.needToKillPlayerPos.ways.length; waysQuesterIndex < questerDirectionCount; ++waysQuesterIndex) {
               switch (questerDirection = this.needToKillPlayerPos.ways[waysQuesterIndex]) {
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                     addUniqueDirection(vector5, questerDirection);
                     break;
                  case 5:
                  default:
                     addUniqueDirection(vector3, questerDirection / 10);
                     break;
                  case 6:
                     this.calcWaysPlayerId = this.needToKillPlayer.needToKillPlayerId;
                     this.calcWaysPlayerPosXServer = this.needToKillPlayerPos.posX;
                     this.calcWaysPlayerPosYServer = this.needToKillPlayerPos.posY;
                     this.calcWaysPlayerPosXClient = -1;
                     this.calcWaysPlayerPosYClient = -1;
               }
            }

            if (this.needToKillPlayerPos.mapWorldCoord != null) {
               addUniqueWorldCoordinate(vector6, this.needToKillPlayerPos.mapWorldCoord);
            }
         }

         waysQuesterIndex = 0;

         int needItemIndex;
         int onMapIndex;
         int itemDropDirectionCount;
         for(questerDirectionCount = this.questSingle.startDropItems.length; waysQuesterIndex < questerDirectionCount; ++waysQuesterIndex) {
            QuestSupStartDropItem questSupStartDropItem = this.questSingle.startDropItems[waysQuesterIndex];
            maximum = -1;
            if (questSupStartDropItem.count > 0) {
               maximum = Math.max(-1, questSupStartDropItem.count);
            } else {
               for(needItemIndex = 0; needItemIndex < this.questSingle.needItems.length; ++needItemIndex) {
                  QuestSupItemNeed questSupItemNeed;
                  if ((questSupItemNeed = this.questSingle.needItems[needItemIndex]).typeId == questSupStartDropItem.typeId) {
                     maximum = Math.max(maximum, questSupItemNeed.count);
                  }
               }
            }

            if (maximum < 0 || strategist2.getItemsTotalCount(questSupStartDropItem.typeId) < maximum) {
               needItemIndex = 0;

               for(itemDropDirectionCount = this.waysItemsDrop[waysQuesterIndex].length; needItemIndex < itemDropDirectionCount; ++needItemIndex) {
                  onMapIndex = this.waysItemsDrop[waysQuesterIndex][needItemIndex];
                  this.collectEnemyQuestDirections(enemysMap, onMapIndex, questSupStartDropItem.string2, vector5, vector3, vector4);
               }

               needItemIndex = 0;

               for(itemDropDirectionCount = questSupStartDropItem.mapsWorldCoords.length; needItemIndex < itemDropDirectionCount; ++needItemIndex) {
                  short[] shorts;
                  if ((shorts = questSupStartDropItem.mapsWorldCoords[needItemIndex]) != null) {
                     addUniqueWorldCoordinate(vector6, shorts);
                  }
               }
            }
         }

         waysQuesterIndex = 0;

         for(questerDirectionCount = this.questSingle.needItems.length; waysQuesterIndex < questerDirectionCount; ++waysQuesterIndex) {
            QuestSupItemNeed questSupItemNeed2 = this.questSingle.needItems[waysQuesterIndex];
            if (strategist2.getItemsTotalCount(questSupItemNeed2.typeId) < questSupItemNeed2.count) {
               maximum = 0;

               QuestSupMapWithCoords questSupMapWithCoords;
               for(needItemIndex = this.waysNeedItems[waysQuesterIndex].length; maximum < needItemIndex; ++maximum) {
                  switch (itemDropDirectionCount = this.waysNeedItems[waysQuesterIndex][maximum]) {
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                        addUniqueDirection(vector5, itemDropDirectionCount);
                        continue;
                     case 5:
                     default:
                        addUniqueDirection(vector3, itemDropDirectionCount / 10);
                        continue;
                     case 6:
                        onMapIndex = 0;
                  }

                  for(; onMapIndex < questSupItemNeed2.onMaps.length; ++onMapIndex) {
                     if ((questSupMapWithCoords = questSupItemNeed2.onMaps[onMapIndex]).map.equals(tileMap.nameId)) {
                        for(int coordIndex = 0; coordIndex < questSupMapWithCoords.coords.length; ++coordIndex) {
                           try {
                              short townIndex;
                              if ((townIndex = tileMap.cells[questSupMapWithCoords.coords[coordIndex][0]][questSupMapWithCoords.coords[coordIndex][1]].townInd) >= 0 && tileMap.towns[townIndex].state == 0) {
                                 addUniqueDirection(vector3, townIndex);
                              }
                           } catch (Exception exception) {
                              exception.printStackTrace();
                           }
                        }
                     }
                  }
               }

               maximum = 0;

               for(needItemIndex = questSupItemNeed2.onMaps.length; maximum < needItemIndex; ++maximum) {
                  if ((questSupMapWithCoords = questSupItemNeed2.onMaps[maximum]).mapWorldCoord != null) {
                     addUniqueWorldCoordinate(vector6, questSupMapWithCoords.mapWorldCoord);
                  }
               }
            }
         }

         waysQuesterIndex = 0;

         for(questerDirectionCount = this.questSingle.needMonsters.length; waysQuesterIndex < questerDirectionCount; ++waysQuesterIndex) {
            QuestSupMonsterTarget questSupMonsterTarget;
            if (getFullCountForNameMapCountMap((questSupMonsterTarget = this.questSingle.needMonsters[waysQuesterIndex]).monsterName, this.killedMonsters) < questSupMonsterTarget.count) {
               maximum = 0;

               for(needItemIndex = this.waysNeedMonsters[waysQuesterIndex].length; maximum < needItemIndex; ++maximum) {
                  itemDropDirectionCount = this.waysNeedMonsters[waysQuesterIndex][maximum];
                  this.collectEnemyQuestDirections(enemysMap, itemDropDirectionCount, questSupMonsterTarget.monsterName, vector5, vector3, vector4);
               }

               maximum = 0;

               for(needItemIndex = questSupMonsterTarget.mapsWorldCoords.length; maximum < needItemIndex; ++maximum) {
                  short[] shorts2;
                  if ((shorts2 = questSupMonsterTarget.mapsWorldCoords[maximum]) != null) {
                     addUniqueWorldCoordinate(vector6, shorts2);
                  }
               }
            }
         }

         this.tryAddWaysToNeedIdsList(tileMap, this.questSingle.needDecorators, this.usedDecorators, (Hashtable)null, this.waysNeedDecors, 406, vector5, vector3, vector6);
         this.tryAddWaysToNeedIdsList(tileMap, this.questSingle.needChests, this.usedChests, (Hashtable)null, this.waysNeedChests, 401, vector5, vector3, vector6);
         this.tryAddWaysToNeedIdsList(tileMap, this.questSingle.needQuesters, this.usedQuesters, this.visitedQuesters, this.waysNeedQuesters, 409, vector5, vector3, vector6);
         this.calcWaysTowns = vectorToIntArray(vector3);
         this.calcWaysEnemys = vectorToIntArray(vector4);
         this.calcWaysDirects = vectorToIntArray(vector5);
         this.calcWaysToMaps = vectorToShortShortArray(vector6);
      }
   }

   private static int[] vectorToIntArray(Vector vector3) {
      int[] vector3Count = new int[vector3.size()];

      for(int sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
         vector3Count[sizeIndex] = (Integer)vector3.elementAt(sizeIndex);
      }

      return vector3Count;
   }

   private static short[][] vectorToShortShortArray(Vector vector3) {
      short[][] vector3Count = new short[vector3.size()][];

      for(int sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
         vector3Count[sizeIndex] = (short[])vector3.elementAt(sizeIndex);
      }

      return vector3Count;
   }

   private void tryAddWaysToNeedIdsList(TileMap tileMap, QuestSupTextIdsNeed[] idsNeeds, Hashtable hashtable, Hashtable hashtable2, int[][] waysNeed, int townTypeReq, Vector vector3, Vector vector4, Vector vector5) {
      int idsNeedIndex = 0;

      for(int length = idsNeeds.length; idsNeedIndex < length; ++idsNeedIndex) {
         QuestSupTextIdsNeed questSupTextIdsNeed;
         if (getFullCountForNameMapCountMap((questSupTextIdsNeed = idsNeeds[idsNeedIndex]).name, hashtable) < questSupTextIdsNeed.count || questSupTextIdsNeed.count == -1) {
            int mapsWorldCoordIndex = 0;

            int mapsWorldCoords;
            for(mapsWorldCoords = waysNeed[idsNeedIndex].length; mapsWorldCoordIndex < mapsWorldCoords; ++mapsWorldCoordIndex) {
               int townIndex = waysNeed[idsNeedIndex][mapsWorldCoordIndex];
               String name2 = questSupTextIdsNeed.name;
               TileMap tileMap2;
               int requiredTownType;
               String messageText;
               Vector vector6;
               int towns;
               Hashtable hashtable3;
               switch (townIndex) {
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                     addUniqueDirection(vector3, townIndex);
                     continue;
                  case 5:
                  default:
                     addUniqueDirection(vector4, townIndex / 10);
                     continue;
                  case 6:
                     vector6 = vector4;
                     hashtable3 = hashtable2;
                     messageText = name2;
                     requiredTownType = townTypeReq;
                     tileMap2 = tileMap;
                     townIndex = 0;
                     towns = tileMap.towns.length;
               }

               for(; townIndex < towns; ++townIndex) {
                  Vector vector7;
                  if (tileMap2.towns[townIndex].type == requiredTownType && tileMap2.towns[townIndex].typeIdName.equals(messageText) && ((vector7 = hashtable3 == null ? null : (Vector)hashtable3.get(tileMap2.nameId)) == null || !vector7.contains(new Short((short)townIndex)))) {
                     addUniqueDirection(vector6, townIndex);
                  }
               }
            }

            mapsWorldCoordIndex = 0;

            for(mapsWorldCoords = questSupTextIdsNeed.mapsWorldCoords.length; mapsWorldCoordIndex < mapsWorldCoords; ++mapsWorldCoordIndex) {
               short[] el2;
               if ((el2 = questSupTextIdsNeed.mapsWorldCoords[mapsWorldCoordIndex]) != null) {
                  addUniqueWorldCoordinate(vector5, el2);
               }
            }
         }
      }

   }

   private void collectEnemyQuestDirections(EnemysMap enemysMap, int direction, String messageText, Vector vector3, Vector vector4, Vector vector5) {
      switch (direction) {
         case 1:
         case 2:
         case 3:
         case 4:
            addUniqueDirection(vector3, direction);
            return;
         case 5:
         default:
            addUniqueDirection(vector4, direction / 10);
            return;
         case 6:
            this.addWaysToEnemyByName(enemysMap, messageText, vector5);
      }
   }

   private void addWaysToEnemyByName(EnemysMap enemysMap, String monsterName, Vector vector3) {
      int enemyIndex = 0;

      for(int enemysCount = enemysMap.Enemys.length; enemyIndex < enemysCount; ++enemyIndex) {
         if (enemysMap.Enemys[enemyIndex].nameId.equals(monsterName)) {
            addUniqueDirection(vector3, enemyIndex);
         }
      }

   }

   private static void addUniqueDirection(Vector vector3, int direction) {
      Integer integer = direction;
      if (!vector3.contains(integer)) {
         vector3.addElement(integer);
      }
   }

   private static void addUniqueWorldCoordinate(Vector vector3, short[] shorts) {
      int vector3Index = 0;

      for(int vector3Count = vector3.size(); vector3Index < vector3Count; ++vector3Index) {
         short[] shorts2;
         if (((shorts2 = (short[])vector3.elementAt(vector3Index)) != null || shorts != null) && shorts2 != null && shorts != null && shorts2[0] == shorts[0] && shorts2[1] == shorts[1]) {
            return;
         }
      }

      vector3.addElement(shorts);
   }
}


