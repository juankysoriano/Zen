package zenproject.meditation.android.sketch.performers.flowers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BranchesList implements Iterable<Branch> {

    private static final int MAX_BRANCHES = 1000;

    private final List<Branch> branchesList;

    BranchesList(List<Branch> branchesList) {
        this.branchesList = branchesList;
    }

    public static BranchesList newInstance() {
        return new BranchesList(new ArrayList<Branch>());
    }

    public void bloomFrom(Branch branch) {
        if (branchesList.size() < MAX_BRANCHES && branch.canBloom()) {
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
