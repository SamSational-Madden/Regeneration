package com.afg.regeneration.traits.negative;

import lucraft.mods.lucraftcore.abilities.Ability;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public interface INegativeTrait
{
	Class<? extends Ability> getPositiveTrait();
}