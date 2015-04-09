package zenproject.meditation.android.sketch.painting.flowers.branch;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.persistence.FlowerOptionPreferences;
import zenproject.meditation.android.sketch.painting.flowers.Flower;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class BranchesListTest extends ZenTestBase {
    private static final int MAX_BRANCHES = 1000;

    @Mock
    private FlowerOptionPreferences flowerOptionPreferences;
    @Mock
    private List<Branch> internalListMock;
    private List<Branch> internalBranchList;
    private BranchesList branchesList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        internalBranchList = new ArrayList<>();
        branchesList = new BranchesList(internalBranchList, flowerOptionPreferences);
    }

    @Test
    public void testThatBranchListIteratorIsTheIteratorOfTheInternalList() {
        branchesList = new BranchesList(internalListMock, flowerOptionPreferences);
        branchesList.iterator();

        verify(internalListMock).iterator();
    }

    @Test
    public void testThatGetBranchesReturnsInternalList() {
        Assertions.assertThat(branchesList.asList()).isEqualTo(internalBranchList);
    }

    @Test
    public void testThatWhenFlowerIsOtherThanNoneAndNotEnoughOnListThenFlowerIsIncludedIntoList() {
        givenThatHasBranch();

        Assertions.assertThat(branchesList).hasSize(1);
    }

    @Test
    public void testThatWhenFlowerIsNoneAndNotEnoughOnListThenFlowerIsNotIncludedIntoList() {
        givenThatSelectedFlowerIsNone();

        branchesList.bloomFrom(Branch.createAt(0, 0));

        Assertions.assertThat(branchesList).hasSize(0);
    }

    @Test
    public void testThatWhenFlowerIsOtherThanNoneAndEnoughOnListThenFlowerIsNotIncludedIntoList() {
        givenThatHasEnoughBranches();
        Assertions.assertThat(branchesList).hasSize(MAX_BRANCHES);

        branchesList.bloomFrom(Branch.createAt(0, 0));

        Assertions.assertThat(branchesList).hasSize(MAX_BRANCHES);
    }

    @Test
    public void testThatPruneRemovesBranch() {
        Branch branch = givenThatHasBranch();
        branchesList.prune(branch);

        Assertions.assertThat(branchesList).isEmpty();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInkDropSizeLimiter() {
        Assertions.assertThat(BranchesList.newInstance()).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        BranchesList firstInstance = BranchesList.newInstance();
        BranchesList secondInstance = BranchesList.newInstance();

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatSelectedFlowerIsPoppy() {
        when(flowerOptionPreferences.getFlower()).thenReturn(Flower.POPPY);
    }

    private void givenThatSelectedFlowerIsNone() {
        when(flowerOptionPreferences.getFlower()).thenReturn(Flower.NONE);
    }

    private Branch givenThatHasBranch() {
        givenThatSelectedFlowerIsPoppy();

        Branch branch = Branch.createAt(0, 0);
        branchesList.bloomFrom(branch);

        return branch;
    }

    private void givenThatHasEnoughBranches() {
        givenThatSelectedFlowerIsPoppy();

        for(int i = 0 ; i < MAX_BRANCHES; i++) {
            Branch branch = Branch.createAt(0, 0);
            branchesList.bloomFrom(branch);
        }
    }
}