package zenproject.meditation.android.sketch.performers;

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

    public void sproudFrom(Branch branch) {
        if (branchesList.size() < MAX_BRANCHES && branch.canSproud()) {
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

    public List<Branch> getBranchesList() {
        return branchesList;
    }
}
