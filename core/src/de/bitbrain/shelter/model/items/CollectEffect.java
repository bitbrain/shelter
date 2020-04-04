package de.bitbrain.shelter.model.items;

import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.world.GameObject;

public interface CollectEffect {

   void onCollect(Item item, GameObject player, GameContext context);
}
