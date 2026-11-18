package Base;

import Engine.Pair;
import java.io.DataInputStream;
import java.util.Vector;

public final class IndirectNetworkHandler {
   private byte curDataBigVersion = 0;
   private int curDataVersion = 0;
   private final Vector eventPulls = new Vector();
   private EventsStreamParser[] parsers = new EventsStreamParser[0];
   private final Object mainThreadProcessLock = new Object();
   private boolean isAllreadyActiveApplyingVersion = false;
   public static final IndirectNetworkHandler instance = new IndirectNetworkHandler();

   public final synchronized void clearParsers() {
      this.parsers = new EventsStreamParser[0];
   }

   public final synchronized void addParser(EventsStreamParser eventsStreamParser) {
      EventsStreamParser[] parsers2 = this.parsers;
      this.parsers = new EventsStreamParser[parsers2.length + 1];
      System.arraycopy(parsers2, 0, this.parsers, 0, parsers2.length);
      this.parsers[this.parsers.length - 1] = eventsStreamParser;
   }

   public final byte getCurDataBigVersion() {
      return this.curDataBigVersion;
   }

   public final int getCurDataVersion() {
      return this.curDataVersion;
   }

   public final void resetData() {
      this.curDataBigVersion = 0;
      this.curDataVersion = 0;
   }

   public final Pair loadFromStreamAndApply(DataInputStream dataInputStream, boolean mainThread, boolean waitUntilDoneApplying) {
      try {
         return this.loadFromStreamInner(dataInputStream, mainThread, waitUntilDoneApplying);
      } catch (Exception exception) {
         Com.SendMistace("Can't load indirect events", true, true, exception);
         return null;
      }
   }

   public final void loadFromStreamAndApply(DataInputStream dataInputStream) {
      this.loadFromStreamAndApply(dataInputStream, true, true);
   }

   public final boolean isDoneWithDataFromSecondThreadByMainThread(Pair pair) {
      if (pair == null) {
         return true;
      } else {
         synchronized(this.eventPulls) {
            return !this.eventPulls.contains(pair);
         }
      }
   }

   private Pair loadFromStreamInner(DataInputStream dataInputStream, boolean mainThread, boolean waitUntilDoneApplying) {
      try {
      byte byteCode = dataInputStream.readByte();
      int integerCode = dataInputStream.readInt();
      Pair pair;
      if (dataInputStream.readBoolean()) {
         int integerCode2;
         if ((integerCode2 = dataInputStream.readInt()) > 120) {
            System.out.println("WARNING load many indirect events " + integerCode2);
         }

         IndirectNetworkEvent[] indirectNetworkEvents = new IndirectNetworkEvent[integerCode2];

         for(int indirectNetworkEventIndex = 0; indirectNetworkEventIndex < integerCode2; ++indirectNetworkEventIndex) {
            byte byteCode2 = dataInputStream.readByte();
            boolean hasImmediateEvents = false;

            for(int parsersCount = this.parsers.length - 1; parsersCount >= 0; --parsersCount) {
               IndirectNetworkEvent indirectNetworkEvent;
               if ((indirectNetworkEvent = this.parsers[parsersCount].parseEvent(byteCode2, dataInputStream)) != null) {
                  hasImmediateEvents = true;
                  indirectNetworkEvent.loadFromStream(dataInputStream);
                  indirectNetworkEvents[indirectNetworkEventIndex] = indirectNetworkEvent;
                  break;
               }
            }

            if (!hasImmediateEvents) {
               throw new Exception("Can't pars indirect event " + byteCode2);
            }
         }

         synchronized(this.eventPulls) {
            pair = new Pair(new Integer[]{Integer.valueOf(byteCode), integerCode}, indirectNetworkEvents);
            this.eventPulls.addElement(pair);
         }
      } else {
         synchronized(this.eventPulls) {
            pair = new Pair(new Integer[]{Integer.valueOf(byteCode), integerCode}, (Object)null);
            this.eventPulls.addElement(pair);
         }
      }

      if (integerCode == 1 && this.curDataVersion > 0) {
         System.out.println("WARNING income full indirect data " + this.curDataVersion + " -> " + integerCode);
      }

      long startTime = System.currentTimeMillis();
      boolean enqueueEvents = true;

      while(enqueueEvents) {
         if (mainThread) {
            this.processEventsPullsFromMainThread();
         }

         synchronized(this.eventPulls) {
            enqueueEvents = this.eventPulls.contains(pair);
         }

         if (enqueueEvents && System.currentTimeMillis() - startTime >= 30000L) {
            throw new Exception("Can't load indirect events, waiting correct version is too long " + this.curDataVersion + " -> " + integerCode);
         }

         Thread.yield();
         if (!waitUntilDoneApplying && enqueueEvents && !mainThread) {
            break;
         }
      }

      return enqueueEvents ? pair : null;
   
      } catch (Exception exception) {
         IndirectNetworkHandler.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public final void processEventsInMainThreadWithThrow() {
      try {
         this.processEventsPullsFromMainThread();
      } catch (Exception exception) {
         Com.SendMistace("Error in processStrategPullsWithThrow", true, true, exception);
      }
   }

   private void processEventsPullsFromMainThread() {
      try {
      synchronized(this.mainThreadProcessLock) {
         if (this.isAllreadyActiveApplyingVersion) {
            throw new Exception("Error double call of processEventsPullsFromMainThread");
         } else {
            this.isAllreadyActiveApplyingVersion = true;

            IndirectNetworkEvent[] indirectNetworkEvents;
            try {
               Pair pair = null;
               int curDataVersion2;
               synchronized(this.eventPulls) {
                  if (this.eventPulls.size() <= 0) {
                     return;
                  }

                  for(curDataVersion2 = 0; curDataVersion2 < this.eventPulls.size(); ++curDataVersion2) {
                     Pair pair2;
                     Integer[] integers = (Integer[])(pair2 = (Pair)this.eventPulls.elementAt(curDataVersion2)).first;
                     if (pair == null || integers[1] <= ((Integer[])pair.first)[1]) {
                        pair = pair2;
                     }
                  }

                  if (pair == null) {
                     System.out.println("WARNING forApplyEvents = null");
                     return;
                  }
               }

               byte curDataBigVersion2 = ((Integer)((Integer[])pair.first)[0]).byteValue();
               if ((curDataVersion2 = ((Integer[])pair.first)[1]) != 1 && (curDataBigVersion2 != this.curDataBigVersion || curDataVersion2 != this.curDataVersion + 1)) {
                  return;
               }

               int sizeIndex;
               if ((indirectNetworkEvents = (IndirectNetworkEvent[])pair.second) != null) {
                  int indirectNetworkEventsCount = indirectNetworkEvents.length - 1;
                  int indirectNetworkEventIndex2 = 0;

                  label423:
                  while(true) {
                     if (indirectNetworkEventIndex2 >= indirectNetworkEventsCount) {
                        indirectNetworkEventIndex2 = 0;

                        while(true) {
                           if (indirectNetworkEventIndex2 >= indirectNetworkEvents.length) {
                              break label423;
                           }

                           indirectNetworkEvents[indirectNetworkEventIndex2].apply();
                           ++indirectNetworkEventIndex2;
                        }
                     }

                     sizeIndex = indirectNetworkEvents.length - indirectNetworkEventIndex2 - 1;

                     for(int indirectNetworkEventIndex = 0; indirectNetworkEventIndex < sizeIndex; ++indirectNetworkEventIndex) {
                        int nextEventIndex = indirectNetworkEventIndex + 1;
                        if (indirectNetworkEvents[indirectNetworkEventIndex].getApplyOrder() > indirectNetworkEvents[nextEventIndex].getApplyOrder()) {
                           IndirectNetworkEvent indirectNetworkEvent = indirectNetworkEvents[indirectNetworkEventIndex];
                           indirectNetworkEvents[indirectNetworkEventIndex] = indirectNetworkEvents[nextEventIndex];
                           indirectNetworkEvents[nextEventIndex] = indirectNetworkEvent;
                        }
                     }

                     ++indirectNetworkEventIndex2;
                  }
               }

               synchronized(this.eventPulls) {
                  try {
                     if (curDataVersion2 == 1) {
                        for(sizeIndex = 0; sizeIndex < this.eventPulls.size(); ++sizeIndex) {
                           if (((Integer)((Integer[])((Pair)this.eventPulls.elementAt(sizeIndex)).first)[0]).byteValue() != curDataBigVersion2) {
                              this.eventPulls.removeElementAt(sizeIndex);
                              --sizeIndex;
                           }
                        }

                        this.curDataBigVersion = curDataBigVersion2;
                     }

                     this.curDataVersion = curDataVersion2;
                  } finally {
                     this.eventPulls.removeElement(pair);
                  }
               }
            } finally {
               this.isAllreadyActiveApplyingVersion = false;
            }

            if (indirectNetworkEvents != null) {
               for(int lengthIndex = 0; lengthIndex < indirectNetworkEvents.length; ++lengthIndex) {
                  indirectNetworkEvents[lengthIndex].afterApply();
               }
            }

         }
      }
   
      } catch (Exception exception) {
         IndirectNetworkHandler.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
}
