package zenproject.meditation.android.sketch.painting.flowers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.painting.flowers.branch.Branch;

import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class FlowerDrawerTest extends ZenTestBase {
    private static final RainbowImage NO_IMAGE = null;
    private static final int BRANCH_X = 10;
    private static final int BRANCH_Y = 10;
    @Mock
    private RainbowImage rainbowImage;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private List<RainbowImage> loadedImagesMock;

    private Flower flowerToPaint;
    private FlowerDrawer flowerDrawer;
    private List<RainbowImage> loadedImages;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(rainbowDrawer.getContext()).thenReturn(RuntimeEnvironment.application.getApplicationContext());

        flowerToPaint = Flower.POPPY;
        loadedImages = new ArrayList<>();
        flowerDrawer = new FlowerDrawer(flowerToPaint, loadedImages, rainbowDrawer);
    }

    @Test
    public void testThatInitDoesLoadImageForEveryImageOnFlower() {
        flowerDrawer.init();

        for (Integer flowerImageId : flowerToPaint.getFlowerLeafRes()) {
            verify(rainbowDrawer).loadImage(flowerImageId, RainbowImage.LOAD_ORIGINAL_SIZE, flowerDrawer);
        }
    }

    @Test
    public void testThatOnLoadSuccessIncludesRainbowImageOnFlowerList() {
        flowerDrawer.onLoadSucceed(rainbowImage);

        Assertions.assertThat(loadedImages).containsExactly(rainbowImage);
    }

    @Test
    public void testThatOnLoadFailIncludesNullRainbowImageOnFlowerList() {
        flowerDrawer.onLoadFail();

        Assertions.assertThat(loadedImages).containsExactly(NO_IMAGE);
    }

    @Test
    public void testThatPaintFlowerPerformsImagePaintOnRainbowDrawer() {
        givenThatHasImages();
        Branch branch = givenABranch();
        flowerDrawer.paintFlowerFor(branch);

        verify(rainbowDrawer).image(any(RainbowImage.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatPaintFlowerPerformsTranslationToBranchLocation() {
        givenThatHasImages();
        Branch branch = givenABranch();
        flowerDrawer.paintFlowerFor(branch);

        verify(rainbowDrawer).translate(branch.getX(), branch.getY());
    }

    @Test
    public void testThatPaintFlowerPerformsTranslationBeforePaintingImage() {
        givenThatHasImages();
        Branch branch = givenABranch();
        flowerDrawer.paintFlowerFor(branch);

        InOrder drawingOperation = inOrder(rainbowDrawer);

        drawingOperation.verify(rainbowDrawer).translate(anyFloat(), anyFloat());
        drawingOperation.verify(rainbowDrawer).image(any(RainbowImage.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatPaintFlowerGetsAFlowerFromList() {
        givenThatHasImages();
        Branch branch = givenABranch();

        FlowerDrawer flowerDrawer = new FlowerDrawer(flowerToPaint, loadedImagesMock, rainbowDrawer);
        flowerDrawer.paintFlowerFor(branch);

        verify(loadedImagesMock).get(anyInt());
    }

    @Test
    public void testThatPaintFlowerForBranchDoesNothingIfHasNoFlowersToPaint() {
        flowerDrawer.paintFlowerFor(givenABranch());

        verify(rainbowDrawer, never()).image(any(RainbowImage.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatNewInstanceReturnsNotNullFlowerDrawer() {
        Assertions.assertThat(FlowerDrawer.newInstance(Flower.POPPY, rainbowDrawer)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        FlowerDrawer firstInstance = FlowerDrawer.newInstance(flowerToPaint, rainbowDrawer);
        FlowerDrawer secondInstance = FlowerDrawer.newInstance(flowerToPaint, rainbowDrawer);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private Branch givenABranch() {
        return Branch.createAt(BRANCH_X, BRANCH_Y);
    }

    private void givenThatHasImages() {
        flowerDrawer.onLoadSucceed(rainbowImage);
    }
}