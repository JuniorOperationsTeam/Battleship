/**
 *
 */
package iscteiul.ista.battleship;

import java.util.List;

public interface IShip {
    String getCategory();

    Integer getSize();

    List<IPosition> getPositions();

    IPosition getPosition();

    Compass getBearing();

    boolean stillFloating();

    IPosition getTopMostPos();

    IPosition getBottomMostPos();

    IPosition getLeftMostPos();

    IPosition getRightMostPos();

    boolean occupies(IPosition pos);

    boolean tooCloseTo(IShip other);

    boolean tooCloseTo(IPosition pos);

    void shoot(IPosition pos);
}
