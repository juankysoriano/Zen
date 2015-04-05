package zenproject.meditation.android.sketch.painting.flowers.branch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.persistence.FlowerOptionPreferences;

public class BranchesList implements Iterable<Branch> {

    private static final int MAX_BRANCHES = 1000;

    private final List<Branch> branchesList;

    private final FlowerOptionPreferences flowerOptionPreferences;

    BranchesList(List<Branch> branchesList, FlowerOptionPreferences flowerOptionPreferences) {
        this.branchesList = branchesList;
        this.flowerOptionPreferences = flowerOptionPreferences;
    }

    public static BranchesList newInstance() {
        return new BranchesList(new ArrayList<Branch>(), FlowerOptionPreferences.newInstance());
    }

    public void bloomFrom(Branch branch) {
        if (branchesList.size() < MAX_BRANCHES && branch.canBloom() && flowerOptionPreferences.getFlower() != Flower.NONE) {
            branchesList.add(Branch.createFrom(branch));
        }
    }

    @Override
    public Iterator<Branch> iterator() {
        return branchesList.iterator();
    }

    public void prune(Branch branch) {
        branchesList.remove(branch);
    }

    public void clear() {
        branchesList.clear();
    }

    public List<Branch> getBranchesList() {
        return branchesList;
    }
}
