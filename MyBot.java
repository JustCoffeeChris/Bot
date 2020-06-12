import java.util.Iterator;
import java.util.List;

import de.acagamics.crushingrocks.logic.IPlayerController;
import de.acagamics.crushingrocks.logic.Map;
import de.acagamics.crushingrocks.logic.Mine;
import de.acagamics.crushingrocks.logic.Player;
import de.acagamics.crushingrocks.logic.Unit;
import de.acagamics.framework.interfaces.Student;

@Student(author = "Chris Riemer", matrikelnummer = 227225, name = "FunnyBotName")

public class MyBot implements IPlayerController {

	public Mine findMine(Unit getUnit, List<Mine> getMines) {
		Mine returnMine = getMines.get(0);

		for (int i = 1; i < getMines.size() - 1; i++) {
			if (getUnit.getPosition().distance(getMines.get(i).getPosition()) < getUnit.getPosition()
					.distance(returnMine.getPosition())) {
				returnMine = getMines.get(i);
			}
		}

		return returnMine;
	}

	@Override
	public void think(Map map, Player player, Player player1) {

		List<Mine> Mines = map.getMines();
		Mines.removeIf(mine -> mine.getOwnership(player) > 0.9f);
		Mines.sort(player.getBase().getPosition().sortDistanceTo());

		Iterator<Mine> itMine = Mines.iterator();

		if (player.getThinkCounter() == 0) {

			player.setHeroCreationOrder();

		} else {

			if (Unit.getUnitCost(3) <= player.getCreditPoints() && map.getMineIncome(player) > 30) {

				player.setUnitCreationOrder(3);

				for (int i = (player.getCreditPoints() / Unit.getUnitCost(3)); i != 0; i--) {
					player.setUnitCreationOrder(3);

				}

			} else if (map.getMineIncome(player) < 30 && map.getMineIncome(player) > 20
					&& Unit.getUnitCost(2) <= player.getCreditPoints()) {

				for (int i = (player.getCreditPoints() / Unit.getUnitCost(2)); i != 0; i--) {
					player.setUnitCreationOrder(2);

				}

			} else if (map.getMineIncome(player) < 20 && Unit.getUnitCost(1) <= player.getCreditPoints()) {
				for (int i = (player.getCreditPoints() / Unit.getUnitCost(1)); i != 0; i--) {
					player.setUnitCreationOrder(1);

				}
			}

			Iterator<Unit> itUnits = player.getUnits().iterator();

			if (player.hasHero()) {

				if (itMine.hasNext()) {
					player.getHero().setOrder(itMine.next().getPosition());
				}
				if (Mines.isEmpty()) {
					player.getHero().setOrder((player1.getBase().getPosition()));
				}

			}

			itUnits = player.getUnits().iterator();

			while (itUnits.hasNext()) {

				if (itMine.hasNext()) {

					Unit safeUnit = itUnits.next();

					safeUnit.setOrder(findMine(safeUnit, Mines).getPosition());

				} else {

					itUnits.next().setOrder(player1.getBase().getPosition());
					player.setAllUnitsOrder(player1.getBase().getPosition());
				}
			}

		}
	}

}