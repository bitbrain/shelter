package de.bitbrain.shelter.core.items;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public interface CollectEffect {

   boolean onCollect(Item item, GameObject player, GameContext2D context);
}
