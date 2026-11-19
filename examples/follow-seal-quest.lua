-- Accept and track a seal quest before running this script.
-- Quest Follower handles travel. Fights and quest choices are manual.

local quests = java.get(java.class("Moduls.quests.QuestStrateg"), "instance")
local quest = java.call(quests, "getCompassedQuest()LModuls/quests/QuestInPlayer;")
if quest == nil then
    print("Track a seal quest first.")
    return
end

local follower = java.get(java.class("bridge.movement.QuestFollower"), "INSTANCE")
java.set(follower, "enabled", true)
print("Quest Follower enabled.")
