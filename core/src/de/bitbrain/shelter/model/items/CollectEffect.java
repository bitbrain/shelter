package de.bitbrain.shelter.model.items;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public interface CollectEffect {

   boolean onCollect(Item item, GameObject player, GameContext2D context);
}
