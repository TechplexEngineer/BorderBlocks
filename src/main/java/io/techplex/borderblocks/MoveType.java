/*
 * Copyright (C) 2016 techplex
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.techplex.borderblocks;

/**
 * Types of movements.
 *
 * <p>Used with {@link Session#testMoveTo(Location, MoveType)}.</p>
 */
public enum MoveType {

    RESPAWN(false, true),
    EMBARK(true, false),
    MOVE(true, false),
    TELEPORT(true, true),
    RIDE(true, false),
    OTHER_NON_CANCELLABLE(false, false),
    OTHER_CANCELLABLE(true, false);

    private final boolean cancellable;
    private final boolean teleport;

    MoveType(boolean cancellable, boolean teleport) {
        this.cancellable = cancellable;
        this.teleport = teleport;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public boolean isTeleport() {
        return teleport;
    }
}
