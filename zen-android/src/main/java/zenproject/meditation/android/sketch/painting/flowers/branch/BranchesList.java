package zenproject.meditation.android.sketch.painting.flowers.branch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zenproject.meditation.android.preferences.FlowerOptionPreferences;
import zenproject.meditation.android.sketch.painting.flowers.Flower;

public class BranchesList implements Iterable<Branch> {

    private static final int MAX_BRANCHES = 1000;

    private final List<Branch> list;

    private final FlowerOptionPreferences flowerOptionPreferences;

    BranchesList(List<Branch> list, FlowerOptionPreferences flowerOptionPreferences) {
        this.list = list;
        this.flowerOptionPreferences = flowerOptionPreferences;
    }

    public static BranchesList newInstance() {
        return new BranchesList(new ArrayList<Branch>(), FlowerOptionPreferences.newInstance());
    }

    public void bloomFrom(Branch branch) {
        if (list.size() < MAX_BRANCHES && branch.canBloom() && flowerOptionPreferences.getFlower() != Flower.NONE) {
            list.add(Branch.createFrom(branch));
        }
    }

    @Override
    public Iterator<Branch> iterator() {
        return list.iterator();
    }

    public void prune(Branch branch) {
        list.remove(branch);
    }

    public void clear() {
        list.clear();
    }

    public List<Branch> asList() {
        return list;
    }
}
