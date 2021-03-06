/*
   JAdventure - A Java-based RPG
   Copyright (C) 2017  TehGuy

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package logic.quests;

import logic.core.World;
import logic.item.InventoryItem;

import java.util.List;

public class QuestLogic {

    private QuestLogic(){}

    private static void addQuestToList(PlayerQuest quest, List<PlayerQuest> questList) {
        questList.add(quest);
        World.sendObserverNotification("plr_quest");
    }

    public static Boolean hasThisQuest(Quest quest, List<PlayerQuest> questList) {
        for (PlayerQuest pq : questList) {
            if (pq.getDetails().getID() == quest.getID()) {
                return true;
            }
        }

        return false;
    }

    public static Boolean completedThisQuest(Quest quest, List<PlayerQuest> questList) {
        for (PlayerQuest pq : questList) {
            if (pq.getDetails().getID() == quest.getID()) {
                return pq.isCompleted();
            }
        }

        return false;
    }

    public static Boolean hasAllQuestCompletionItems(Quest quest, List<InventoryItem> playerInventory) {
        for (QuestCompletionItem qci : quest.getQuestCompletionItems()) {
            boolean foundItemInPlayerInv = false;

            for (InventoryItem item : playerInventory) {
                if (item.getDetails().getID() == qci.getDetails().getID()) {
                    foundItemInPlayerInv = true;

                    if (item.getQuantity() < qci.getQuantity()) {
                        return false;
                    }
                }
            }

            if (!foundItemInPlayerInv) {
                return false;
            }
        }

        return true;
    }

    private static void removeQuestCompletionItems(Quest quest, List<InventoryItem> playerInventory) {
        for (QuestCompletionItem qci : quest.getQuestCompletionItems()) {
            for (InventoryItem item : playerInventory) {
                if (item.getDetails().getID() == qci.getDetails().getID()) {
                    item.setQuantity(item.getQuantity() - qci.getQuantity());
                    break;
                }
            }
        }

        World.sendObserverNotification("plr_inv_additem");
    }

    private static void markQuestAsCompleted(Quest quest, List<PlayerQuest> questList) {
        for (PlayerQuest pq : questList) {
            if (pq.getDetails().getID() == quest.getID()) {
                pq.setCompleted();
                return;
            }
        }
    }

    public static void completeQuest(Quest quest) {
        World.sendMessengerObserverNotification("message", "You complete the \'"
                + quest.getName() + "\' quest.\n");
        removeQuestCompletionItems(quest, World.getPlayer().getInventory());

        World.sendMessengerObserverNotification("message", "You receive: \n"
                + Integer.toString(quest.getRewardExperiencePoints())
                + " XP points\n"
                + Integer.toString(quest.getRewardGold()) + " gold\n"
                + quest.getRewardItem().getName() + "\n\n");

        World.getPlayer().addExperiencePoints(quest.getRewardExperiencePoints());
        World.getPlayer().addGold(quest.getRewardGold());

        World.getPlayer().addItemToInventory(quest.getRewardItem());

        markQuestAsCompleted(quest, World.getPlayer().getQuests());
    }

    public static void giveQuestToPlayer(Quest quest) {
        World.sendMessengerObserverNotification("message", "You receive the \'"
                + quest.getName() + "\' quest.\n"
                + "To complete it, return with:\n");

        for (QuestCompletionItem qci : quest.getQuestCompletionItems()) {
            if (qci.getQuantity() == 1) {
                World.sendMessengerObserverNotification("message",
                        Integer.toString(qci.getQuantity()) + " " + qci.getDetails().getName() + "\n");
            } else {
                World.sendMessengerObserverNotification("message",
                        Integer.toString(qci.getQuantity()) + " " + qci.getDetails().getNamePlural()
                                + "\n");
            }
        }
        World.sendMessengerObserverNotification("message", "\n");

        QuestLogic.addQuestToList(new PlayerQuest(quest.getID()), World.getPlayer().getQuests());
    }
}
