part of 'main.dart';

enum BrushColor {
  dark('Dark', ZenColors.darkBrush),
  amber('Amber', ZenColors.amberBrush),
  primary('Blue', ZenColors.primary),
  accent('Red', ZenColors.accent),
  erase('Erase', ZenColors.paper);

  const BrushColor(this.label, this.color);

  final String label;
  final Color color;
}

enum BrushSize {
  small('Small', 54),
  medium('Medium', 84),
  large('Large', 120);

  const BrushSize(this.label, this.radius);

  final String label;
  final double radius;

  double get minRadius => math.max(0.75, radius * 0.18);

  double get previewDiameter => math.max(12, radius / 3);
}

enum Flower {
  none('None', null, 18, 25),
  birdsfoot('Birdsfoot', 'assets/images/birdsfoot.png', 18 * 1.2, 25 * 1.2),
  poppy('Poppy', 'assets/images/poppy.png', 18, 25),
  meconopsis('Meconopsis', 'assets/images/meconopsis.png', 18 * 0.8, 25 * 0.8),
  cherry('Cherry', 'assets/images/cherry.png', 18, 25),
  blueDahlia('Blue dahlia', 'assets/images/blue_flower_commons.png', 20, 32),
  addisonia('Addisonia', 'assets/images/addisonia_commons.png', 24, 38);

  const Flower(this.label, this.previewAsset, this.minSize, this.maxSize);

  final String label;
  final String? previewAsset;
  final double minSize;
  final double maxSize;
}

enum ZenTrack {
  original('Original Zen', 'assets/audio/zen.mp3'),
  reachingTheSky('Reaching The Sky', 'assets/audio/reaching_the_sky.m4a'),
  placidAmbient('Placid Ambient', 'assets/audio/placid_ambient.m4a');

  const ZenTrack(this.label, this.asset);

  final String label;
  final String asset;
}

class ZenColors {
  static const accent = Color(0xFFFF5252);
  static const primary = Color(0xFF3F51B5);
  static const paper = Color(0xFFFFFDE7);
  static const panel = Color(0xF8FFFFFF);
  static const panelBorder = Color(0x1F212121);
  static const darkBrush = Color(0xFF212121);
  static const amberBrush = Color(0xFFFFC107);
}

class SketchMetrics {
  static const absoluteMinRadius = 0.75;
  static const inkDropStep = 0.5;
  static const velocityThreshold = 16.0;
  static const inputDivisions = 3;
  static const restingMusicVolume = 0.05;
  static const paintingMusicVolume = 1.0;
  static const musicStep = 0.005;
  static const musicStepInterval = Duration(milliseconds: 16);
  static const branchMinRadius = 0.25;
  static const branchMinBloomRadius = 1.5;
  static const branchDefaultRadius = 3.0;
}

class Prefs {
  static const brushColor = 'brush_color';
  static const brushSize = 'brush_size';
  static const flower = 'flower';
  static const musicEnabled = 'music_enabled';
  static const track = 'track';
}
