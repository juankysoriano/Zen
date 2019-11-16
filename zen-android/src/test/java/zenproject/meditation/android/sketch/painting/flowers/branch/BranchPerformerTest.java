package zenproject.meditation.android.sketch.painting.flowers.branch;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.PaintStepSkipper;
import zenproject.meditation.android.sketch.painting.flowers.FlowerDrawer;
import zenproject.meditation.android.sketch.painting.ink.InkDrop;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BranchPerformerTest extends ZenTestBase {

    private List<Branch> internalList;
    @Mock
    private BranchesList branchesList;
    @Mock
    private FlowerDrawer flowerDrawer;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private RainbowInputController rainbowInputController;
    @Mock
    private PaintStepSkipper paintStepSkipper;
    @Mock
    private Branch branch;
    @Mock
    private BrushOptionsPreferences brushOptionsPreferences;
    @Mock
    private InkDrop inkDrop;

    private BranchPerformer branchPerformer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        givenThatHasBranch();

        branchPerformer = new BranchPerformer(inkDrop,
                branchesList,
                flowerDrawer,
                paintStepSkipper,
                brushOptionsPreferences,
                rainbowDrawer,
                rainbowInputController);
    }

    @Test
    public void testThatInitConfiguresProperlyRainbowDrawer() {
        branchPerformer.init();

        verify(rainbowDrawer).noStroke();
        verify(rainbowDrawer).smooth();
    }

    @Test
    public void testThatInitDelegatesInFlowerDrawer() {
        branchPerformer.init();

        verify(flowerDrawer).init();
    }

    @Test
    public void testThatOnDoStepIfPerformerIsEnabledStepIsRecordedOnPaintStepSkipper() {
        givenThatPerformerIsEnabled();

        branchPerformer.doStep();

        verify(paintStepSkipper).recordStep();
    }

    @Test
    public void testThatOnDoStepIfPerformerIsDisabledStepIsNotRecordedOnPaintStepSkipper() {
        givenThatPerformerIsDisabled();

        branchPerformer.doStep();

        verify(paintStepSkipper, never()).recordStep();
    }

    @Test
    public void testGivenThatPerformerIsEnabledDoesNotHaveToSkipStepAndHasFlowerThenWhenDoingStepBranchesListIsPrunedIfBranchIsDead() {
        givenThatPerformerIsEnabled();
        givenThatHasNotToSkipStep();
        givenThatHasFlowers();
        givenThatBranchIsDead();

        branchPerformer.doStep();

        verify(branchesList).prune(branch);

    }

    @Test
    public void testGivenThatPerformerIsEnabledHaveToSkipStepAndHasFlowerThenWhenDoingStepBranchesListIsNotPrunedIfBranchIsDead() {
        givenThatPerformerIsEnabled();
        givenThatHasToSkipStep();
        givenThatHasFlowers();
        givenThatBranchIsDead();

        branchPerformer.doStep();

        verify(branchesList, never()).prune(any(Branch.class));
    }

    @Test
    public void testGivenThatPerformerIsEnabledDoesNotHaveToSkipStepAndHasNoFlowerThenWhenDoingStepBranchesListIsNotPrunedIfBranchIsDead() {
        givenThatHasNoFlowers();
        givenThatPerformerIsEnabled();
        givenThatHasNotToSkipStep();
        givenThatBranchIsDead();

        branchPerformer.doStep();

        verify(branchesList, never()).prune(any(Branch.class));
    }

    @Test
    public void testGivenThatPerformerIsEnabledDoesNotHaveToSkipStepThenWhenDoingStepBranchIsPainted() {
        givenThatPerformerIsEnabled();
        givenThatHasNotToSkipStep();
        givenThatBranchIsNotDead();

        branchPerformer.doStep();

        verify(rainbowDrawer).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testGivenThatPerformerIsEnabledDoesNotHaveToSkipStepAndHasFlowerThenWhenDoingStepBranchIsPainted() {
        givenThatPerformerIsEnabled();
        givenThatHasNotToSkipStep();
        givenThatHasFlowers();
        givenThatBranchIsNotDead();

        branchPerformer.doStep();

        verify(rainbowDrawer).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testGivenThatPerformerIsEnabledDoesNotHaveToSkipStepAndHasFlowerThenWhenDoingStepBranchIsPaintedWithBrushColor() {
        givenThatPerformerIsEnabled();
        givenThatHasNotToSkipStep();
        givenThatHasFlowers();
        givenThatBranchIsNotDead();

        branchPerformer.doStep();

        verify(brushOptionsPreferences).getBranchColor();
    }

    @Test
    public void testGivenThatPerformerIsEnabledHaveToSkipStepAndHasFlowerThenWhenDoingStepBranchIsNotPainted() {
        givenThatPerformerIsEnabled();
        givenThatHasToSkipStep();
        givenThatBranchIsNotDead();

        branchPerformer.doStep();

        verify(rainbowDrawer, never()).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatNewInstanceReturnsNotNullBranchPerformer() {
        assertThat(BranchPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        BranchPerformer firstInstance = BranchPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController);
        BranchPerformer secondInstance = BranchPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController);

        assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatPerformerIsEnabled() {
        branchPerformer.enable();
    }

    private void givenThatPerformerIsDisabled() {
        branchPerformer.disable();
    }

    private void givenThatHasNoFlowers() {
        branchPerformer = new BranchPerformer(inkDrop,
                branchesList,
                null,
                paintStepSkipper,
                brushOptionsPreferences,
                rainbowDrawer,
                rainbowInputController);
    }

    private void givenThatHasFlowers() {
        //no-op, just for legibility
    }

    private void givenThatHasBranch() {
        internalList = new ArrayList<>();
        internalList.add(branch);
        when(branchesList.asList()).thenReturn(internalList);
    }

    private void givenThatHasToSkipStep() {
        when(paintStepSkipper.hasToSkipStep()).thenReturn(true);
    }

    private void givenThatHasNotToSkipStep() {
        when(paintStepSkipper.hasToSkipStep()).thenReturn(false);
    }

    private void givenThatBranchIsDead() {
        when(branch.isDead()).thenReturn(true);
    }

    private void givenThatBranchIsNotDead() {
        when(branch.isDead()).thenReturn(false);
    }
}
