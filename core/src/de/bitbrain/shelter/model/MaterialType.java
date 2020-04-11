package de.bitbrain.shelter.model;

import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;

public enum MaterialType {

   STEEL,
   FLESH(Assets.Sounds.IMPACT_01, Assets.Sounds.IMPACT_02, Assets.Sounds.IMPACT_03),
   ROCK;

   private final String[] impactSoundFx;

   MaterialType(String ... impactSoundFx) {
      this.impactSoundFx = impactSoundFx;
   }

   public JukeBox getImpactSoundFx(AudioManager audioManager) {
      return new JukeBox(audioManager, 200, impactSoundFx);
   }
}
